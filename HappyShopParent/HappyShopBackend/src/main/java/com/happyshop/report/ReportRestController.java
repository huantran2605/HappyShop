package com.happyshop.report;

import java.text.ParseException;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportRestController {
    @Autowired
    MasterOrderReportService masterOrderReportService;
    @Autowired
    OrderDetailReportService orderDetailReportService;
    
    @GetMapping("/reports/sale_by_date/{period}")
    public List<ReportItem> getReportDateByDate(@PathVariable("period") String period) {
        switch (period) {
            case "last_7_days":
                return masterOrderReportService.getReportDataLast7Days(ReportType.DAY);
            case "last_28_days":
                return masterOrderReportService.getReportDataLast28Days(ReportType.DAY);
            case "last_6_months":
                return masterOrderReportService.getReportDataLast6Months(ReportType.MONTH);
            case "last_year":
                return masterOrderReportService.getReportDataLastYear(ReportType.MONTH);
            default:
                return masterOrderReportService.getReportDataLast7Days(ReportType.DAY);
                
        }
        
    }
    
    @GetMapping("/reports/sale_by_date/{startTime}/{endTime}")
    public List<ReportItem> getReportDateByDate(@PathVariable("startTime") String startTime,
            @PathVariable("endTime") String endTime) throws ParseException {
 
        return masterOrderReportService.getReportDataCustomTime(startTime, endTime, ReportType.DAY);
    
    }
    
    @GetMapping("/reports/{groupBy}/{period}")
    public List<ReportItem> getReportDataByCategoryOrProduct(@PathVariable("period") String period,
            @PathVariable("groupBy") String groupBy) {
        
        ReportType reportType = ReportType.valueOf(groupBy.toUpperCase());
        switch (period) {
            case "last_7_days":   
                return orderDetailReportService.getReportDataLast7Days(reportType);
            case "last_28_days":
                return orderDetailReportService.getReportDataLast28Days(reportType);
            case "last_6_months":
                return orderDetailReportService.getReportDataLast6Months(reportType);
            case "last_year":
                return orderDetailReportService.getReportDataLastYear(reportType);
            default:
                return orderDetailReportService.getReportDataLast7Days(reportType);
                
        }
        
    }
    
    @GetMapping("/reports/{groupBy}/{startTime}/{endTime}")
    public List<ReportItem> getReportDateByCategory(@PathVariable("startTime") String startTime,
            @PathVariable("endTime") String endTime,
            @PathVariable("groupBy") String groupBy) throws ParseException {
        ReportType reportType = ReportType.valueOf(groupBy.toUpperCase());
        
        return orderDetailReportService.getReportDataCustomTime(startTime, endTime, reportType);            
  
    }
    
}
