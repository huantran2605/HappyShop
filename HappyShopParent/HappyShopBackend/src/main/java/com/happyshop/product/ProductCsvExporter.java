package com.happyshop.product;

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
import com.happyshop.common.entity.product.Product;

public class ProductCsvExporter extends AbstractExporter{

	public void export(List<Product> listProduct, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response,"text/csv", ".csv", "product_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = 
		    {"ID","Name","Alias","Enable", "In Stock","Cost","Price",
		            "Discount"};
		String[] fieldMapping = {"id", "name","alias",
		        "enable","inStock","cost","price","discountPercent"};
		csvWriter.writeHeader(csvHeader);
		for (Product product : listProduct) {
			csvWriter.write(product, fieldMapping);
		}
		csvWriter.close();
	}
}
