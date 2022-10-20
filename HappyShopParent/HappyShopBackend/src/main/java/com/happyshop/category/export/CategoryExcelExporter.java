package com.happyshop.category.export;

import java.io.IOException;


import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.happyshop.AbstractExporter;
import com.happyshop.common.entity.Category;

public class CategoryExcelExporter  extends AbstractExporter {
	private org.apache.poi.xssf.usermodel.XSSFWorkbook workbook;
	private org.apache.poi.xssf.usermodel.XSSFSheet sheet;
	
	public CategoryExcelExporter() {
		workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
	}
	
	private void writeHeaderLine() {
		sheet = workbook.createSheet("Categories");
		org.apache.poi.xssf.usermodel.XSSFRow row = sheet.createRow(0);
		
		org.apache.poi.xssf.usermodel.XSSFCellStyle cellStyle = workbook.createCellStyle();
		org.apache.poi.xssf.usermodel.XSSFFont font = workbook.createFont();
		
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);  
		
		createCell (row, 0, "Category ID", cellStyle);
		createCell (row, 1, "Category name", cellStyle);
		createCell (row, 2, "Alias", cellStyle);
		createCell (row, 3, "Category name parent", cellStyle);
		createCell (row, 4, "Enable", cellStyle);
		

	}
	private void createCell(org.apache.poi.xssf.usermodel.XSSFRow row, int columnIndex, Object value, org.apache.poi.ss.usermodel.CellStyle style) {
		org.apache.poi.xssf.usermodel.XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);
		
		if(value instanceof Long ) {
			cell.setCellValue((Long) value);
		}
		else if(value instanceof Integer ) {
			cell.setCellValue((Integer) value);
		}
		else if(value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		}
		else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}
	
	private void writeDateLine(List<Category> listCategory) {
		org.apache.poi.xssf.usermodel.XSSFCellStyle cellStyle = workbook.createCellStyle();
		org.apache.poi.xssf.usermodel.XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		cellStyle.setFont(font);
		int rowIndex = 1;
		for (Category category : listCategory) {
			org.apache.poi.xssf.usermodel.XSSFRow row = sheet.createRow(rowIndex++);
			int columnIndex = 0;
			
			createCell(row, columnIndex++, category.getId(), cellStyle);
			
			createCell(row, columnIndex++, category.getName(), cellStyle);
			createCell(row, columnIndex++, category.getAlias(), cellStyle);
			createCell(row, columnIndex++, category.getCategoryNameParent(), cellStyle);
			createCell(row, columnIndex++, category.isEnable(), cellStyle);		
		}

	}
	
	
	
	public void export(List<Category> listCategory, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/octet-stream", ".xlsx", "category_");
		
		writeHeaderLine();
		writeDateLine(listCategory);
		ServletOutputStream outputStream = response.getOutputStream();
		
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
}
