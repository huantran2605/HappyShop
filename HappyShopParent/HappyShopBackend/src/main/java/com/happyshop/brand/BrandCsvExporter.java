package com.happyshop.brand;

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
import com.happyshop.common.entity.Brand;
import com.happyshop.common.entity.Category;

public class BrandCsvExporter extends AbstractExporter{

	public void export(List<Brand> listBrand, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response,"text/csv", ".csv", "brand_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"Brand ID","Brand Name"};
		String[] fieldMapping = {"id", "name"};
		csvWriter.writeHeader(csvHeader);
		for (Brand brand : listBrand) {
			csvWriter.write(brand, fieldMapping);
		}
		csvWriter.close();
	}
}
