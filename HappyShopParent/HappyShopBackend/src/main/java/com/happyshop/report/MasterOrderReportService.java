package com.happyshop.report;

import java.text.ParseException;
import java.util.List;

public interface MasterOrderReportService {
    public List<ReportItem> getReportDataLast7Days();
    
    public List<ReportItem> getReportDataLast28Days();

    public List<ReportItem> getReportDataLast6Months();
    
    public List<ReportItem> getReportDataLastYear();
    
    public List<ReportItem> getReportDataCustomTime(String startDate, String endDate) throws ParseException;
}
