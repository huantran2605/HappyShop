package com.seafoodshop.user.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.seafoodshop.AbstractExporter;
import com.seafoodshop.common.entity.User;

public class UserCsvExporter extends AbstractExporter{

	public void export(List<User> listUser, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response,"text/csv", ".csv", "user_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"User ID", "Email","First Name", "Last Name", "Enable", "Roles"};
		String[] fieldMapping = {"id", "email", "firstName", "lastName", "enable", "roles"};
		csvWriter.writeHeader(csvHeader);
		for (User user : listUser) {
			csvWriter.write(user, fieldMapping);
		}
		csvWriter.close();
	}
}
