package com.satyait.service;

import java.awt.Color;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.satyait.entity.Users;
import com.satyait.repo.UsersRepository;
import com.satyait.request.SearchRequest;
import com.satyait.response.SearchResponse;

@Service
public class UsersServiceImpl implements UsersService {

	@Autowired
	private UsersRepository usersRepo;

	@Override
	public List<String> getUniqueNames() {
		return usersRepo.getPlanNames();

	}

	@Override
	public List<String> getUniqueStatus() {
		return usersRepo.getPlanStatus();
	}

	@Override
	public List<SearchResponse> search(SearchRequest request) {
		List<SearchResponse> response = new ArrayList<>();

		Users queryBuilder = new Users();

		String planName = request.getName();

		if (planName != null && !planName.equals("")) {
			queryBuilder.setPlanName(planName);
		}

		String status = request.getStatus();
		if (status != null && !status.equals("")) {
			queryBuilder.setPlanStatus(status);
		}

		LocalDate planStartDate = request.getPlanStartDate();

		if (planStartDate != null) {
			queryBuilder.setPlanStartDate(planStartDate);
		}

		LocalDate planEndDate = request.getPlanEndDate();

		if (planEndDate != null) {
			queryBuilder.setPlanEndDate(planEndDate);
		}

		Example<Users> example = Example.of(queryBuilder);

		List<Users> entities = usersRepo.findAll(example);

		for (Users entity : entities) {
			SearchResponse sr = new SearchResponse();
			BeanUtils.copyProperties(entity, sr);
			response.add(sr);
		}
		return response;
	}

	@Override
	public void generateExcel(HttpServletRequest response) {

		List<Users> entities = usersRepo.findAll();
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet();
		HSSFRow headerRow = sheet.createRow(0);

		headerRow.createCell(0).setCellValue("Name");
		headerRow.createCell(1).setCellValue("Mobile");
		headerRow.createCell(2).setCellValue("Gender");
		headerRow.createCell(3).setCellValue("SSN");

		int i = 1;
		for(Users entity : entities) {
			HSSFRow dataRow = sheet.createRow(i);
			dataRow.createCell(0).setCellValue(entity.getName());
			dataRow.createCell(1).setCellValue(entity.getMobile());
			dataRow.createCell(2).setCellValue(String.valueOf(entity.getGender()));
			dataRow.createCell(3).setCellValue(entity.getSsn());
			i++;
		}
		
		
		ServletOutputStream outputStream = response.getOutputStream();
		workBook.write(outputStream);
		workBook.close();
		outputStream.close();
		
	}

	@Override
	public void generatePdf(HttpServletRequest response) throws Exception {
		List<Users> entities = usersRepo.findAll();

		Document document = new Document(PageSize.A4);

		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);

		Paragraph p = new Paragraph("Search Report", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(p);

		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.5f, 3.5f, 3.0f, 3.0f, 1.5f });
		table.setSpacingBefore(10);
         
		
	   PdfPCell cell = new PdfPCell();		
	   cell.setBackgroundColor(Color.BLUE);
	   cell.setPadding(5);
	   
	   font = FontFactory.getFont(FontFactory.HELVETICA);
	   font.setColor(Color.WHITE);
	   
	   cell.setPhrase(new Phrase("Name", font));
	   table.addCell(cell);
	   
	   cell.setPhrase(new Phrase("E-mail", font));
	   table.addCell(cell);
	   
	   cell.setPhrase(new Phrase("Phno", font));
	   table.addCell(cell);
	   
	   cell.setPhrase(new Phrase("Gender", font));
	   table.addCell(cell);
	   
	   cell.setPhrase(new Phrase("SSN", font));
	   table.addCell(cell);
	   
	   
		document.add(table);
		document.close();
	}

}
