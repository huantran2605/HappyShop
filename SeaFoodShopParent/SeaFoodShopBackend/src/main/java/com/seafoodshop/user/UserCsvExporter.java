package com.seafoodshop.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.seafoodshop.common.entity.User;

public class UserCsvExporter {

//	private void export(List<User> listUser, HttpServletResponse response) throws IOException {
//		DateFormat dateFomart = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
//		String time = dateFomart.format(new Date());
//		String fileName = "user_" + time + ".csv";
//		
//		response.setContentType("text/csv");
//		
//		String headerKey = "Content-Disposition";
//		String headerValue = "attachment; filename="+fileName;
//		response.setHeader(headerKey, headerValue);
//		
//		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
//		
//		String[] csvHeader = {"User ID", "Email","First Name", "Last Name", "Enable", "Roles"};
//		
//	}
}
