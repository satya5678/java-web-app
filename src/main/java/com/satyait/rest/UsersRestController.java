package com.satyait.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.satyait.request.SearchRequest;
import com.satyait.response.SearchResponse;
import com.satyait.service.UsersService;

@RestController
public class UsersRestController {
	
	@Autowired
	private UsersService usersService;
	
	
	@GetMapping("/plans")
	public ResponseEntity<List<String>> getPlanNames(){
		List<String> uniqueNames = usersService.getUniqueNames();
		return new ResponseEntity<>(uniqueNames, HttpStatus.OK);
		
	}
	

	@GetMapping("/status")
	public ResponseEntity<List<String>> getPlanStatus(){
		List<String> uniqueStatus = usersService.getUniqueStatus();
		return new ResponseEntity<>(uniqueStatus, HttpStatus.OK);
		
	}
	

	
	@PostMapping("/search")
	public ResponseEntity<List<SearchResponse>> search(@RequestBody SearchRequest request){
		List<SearchResponse> search = usersService.search(request);
		return new ResponseEntity<>(search, HttpStatus.OK);
		
	}
	
	
	@GetMapping("/excel")
	public void excelReport(HttpServletResponse response) throws Exception {
		response.setContentType("application/octet-stream");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment;fileName=data.xls";
		
		response.setHeader(headerKey, headerValue);
		
		usersService.generateExcel(response);
		
	}
	
	

	@GetMapping("/pdf")
	public void pdfReport(HttpServletResponse response) throws Exception {
		response.setContentType("application/pdf");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment;fileName=data.pdf";
		
		response.setHeader(headerKey, headerValue);
		
		usersService.generatePdf(response);
		
		
	}
	
	}


