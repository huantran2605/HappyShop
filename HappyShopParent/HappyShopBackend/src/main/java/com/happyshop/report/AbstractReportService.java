package com.happyshop.report;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public abstract class AbstractReportService {
    protected DateFormat dateFormat;

    public List<ReportItem> getReportDataLast7Days(ReportType reportType) {
        return getReportDataLastXDays(7, reportType);
    }

    public List<ReportItem> getReportDataLast28Days(ReportType reportType) {
        return getReportDataLastXDays(28, reportType);
    }

    public List<ReportItem> getReportDataLast6Months(ReportType reportType) {
        return getReportDataLastXMonths(6, reportType);
    }

    public List<ReportItem> getReportDataLastYear(ReportType reportType) {
        return getReportDataLastXMonths(12, reportType);
    }

    protected List<ReportItem> getReportDataLastXDays(int days, ReportType reportType) {

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate daysAgo = tomorrow.minusDays(days);

        Date startTime = java.sql.Date.valueOf(daysAgo);
        Date endTime = java.sql.Date.valueOf(tomorrow);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return getReportDataByDateRangeInternal(startTime, endTime, reportType);
    }

    protected List<ReportItem> getReportDataLastXMonths(int months, ReportType reportType) {
        LocalDate endMonth = LocalDate.now().plusMonths(1);
        LocalDate startMonth = endMonth.minusMonths(months);

        Date startTime = java.sql.Date.valueOf(startMonth);
        Date endTime = java.sql.Date.valueOf(endMonth);
        dateFormat = new SimpleDateFormat("yyyy-MM");

        return getReportDataByDateRangeInternal(startTime, endTime, reportType);
    }
    
    public List<ReportItem> getReportDataCustomTime(String startDate, String endDate, ReportType reportType) throws ParseException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        Date startTime = dateFormat.parse(startDate);
        Date endTime = dateFormat.parse(endDate);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTime);

        // Add one day to the Calendar object
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        // Get the day after the start date
        Date dayAfterEndTime = calendar.getTime();
        
        
        return getReportDataByDateRangeInternal(startTime, dayAfterEndTime, reportType);
        
    }
   
    
    protected abstract List<ReportItem> getReportDataByDateRangeInternal(
            Date startDate, Date endDate, ReportType reportType);

}
