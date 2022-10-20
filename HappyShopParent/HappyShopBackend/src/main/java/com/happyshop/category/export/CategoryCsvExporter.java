package com.happyshop.category.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.happyshop.AbstractExporter;
import com.happyshop.common.entity.Category;

public class CategoryCsvExporter extends AbstractExporter{

	public void export(List<Category> listCategory, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response,"text/csv", ".csv", "category_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"Category ID","Category Name", "Alias", "Enable"};
		String[] fieldMapping = {"id", "name", "alias", "enable"};
		csvWriter.writeHeader(csvHeader);
		for (Category category : listCategory) {
			csvWriter.write(category, fieldMapping);
		}
		csvWriter.close();
	}
}
