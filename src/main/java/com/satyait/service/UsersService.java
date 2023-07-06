package com.satyait.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.satyait.request.SearchRequest;
import com.satyait.response.SearchResponse;

public interface UsersService {

	public List<String> getUniqueNames();

	public List<String> getUniqueStatus();

	public List<SearchResponse> search(SearchRequest request);

	public void generateExcel(HttpServletRequest response) throws Exception;

	public void generatePdf(HttpServletRequest response) throws Exception;

}
