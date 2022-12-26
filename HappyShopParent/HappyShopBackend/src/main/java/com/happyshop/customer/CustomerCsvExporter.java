package com.happyshop.customer;

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
import com.happyshop.common.entity.Customer;

public class CustomerCsvExporter extends AbstractExporter{

	public void export(List<Customer> listCustomer, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response,"text/csv", ".csv", "customer_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = 
		    {"ID","Email","First Name","Last Name", "Phone Number","AddressLine1","AddressLine2",
		            "City","State","PostalCode", "Enable", "Created Time"
		            ,"Authentication Type"};
		String[] fieldMapping = {"id", "email","firstName","lastName","phoneNumber"
		        ,"addressLine1","addressLine2","city","state","postalCode", "enabled", "createdTime"
		        ,"authenticationType"};
		csvWriter.writeHeader(csvHeader);
		for (Customer customer : listCustomer) {
			csvWriter.write(customer, fieldMapping);
		}
		csvWriter.close();
	}
}
