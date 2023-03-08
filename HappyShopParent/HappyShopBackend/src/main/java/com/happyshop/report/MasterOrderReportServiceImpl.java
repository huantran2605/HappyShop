package com.happyshop.report;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.order.Order;
import com.happyshop.order.OrderRepository;
import com.happyshop.order.OrderService;

@Service
public class MasterOrderReportServiceImpl implements MasterOrderReportService {
    
    @Autowired
    OrderRepository orderRepository;
    private DateFormat dateFormat;
    
    public List<ReportItem> getReportDataLast7Days() {
        return getReportDataLastXDays(7);
    }
    
    public List<ReportItem> getReportDataLast28Days() {
        return getReportDataLastXDays(28);
    }
    
    public List<ReportItem> getReportDataLast6Months() {
        return getReportDataLastXMonths(6);
    }
    
    public List<ReportItem> getReportDataLastYear() {
        return getReportDataLastXMonths(12);
    }
    
    public List<ReportItem> getReportDataCustomTime(String startDate, String endDate) throws ParseException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        Date startTime = dateFormat.parse(startDate);
        Date endTime = dateFormat.parse(endDate);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTime);

        // Add one day to the Calendar object
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        // Get the day after the start date
        Date dayAfterEndTime = calendar.getTime();
        
        List<Order> listOrder = orderRepository.findByOrderTimeBetween(startTime, dayAfterEndTime);
//        printRawData(listOrder);

        List<ReportItem> listReportItem = createReportData(startTime, dayAfterEndTime, "days");

        calculateSalesForReportData(listReportItem, listOrder);
//        printReport(listReportItem);
        return listReportItem;
        
    }
    
    public List<ReportItem> getReportDataLastXDays(int days) {
        
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate daysAgo = tomorrow.minusDays(days);
        
        Date startTime = java.sql.Date.valueOf(daysAgo);
        Date endTime = java.sql.Date.valueOf(tomorrow);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
             
        return findByOrderTimeBetween(startTime, endTime, "days");
    }
    
    public List<ReportItem> getReportDataLastXMonths(int months){
        LocalDate endMonth = LocalDate.now().plusMonths(1);
        LocalDate startMonth = endMonth.minusMonths(months);
        
        Date startTime = java.sql.Date.valueOf(startMonth);
        Date endTime = java.sql.Date.valueOf(endMonth);
        dateFormat = new SimpleDateFormat("yyyy-MM");
             
        return findByOrderTimeBetween(startTime, endTime, "months");
    }

    public List<ReportItem> findByOrderTimeBetween(Date startTime, Date endTime, String period) {
        List<Order> listOrder = orderRepository.findByOrderTimeBetween(startTime, endTime);
        
//        printRawData(listOrder);
        
        List<ReportItem> listReportItem = createReportData(startTime, endTime, period);
               
        calculateSalesForReportData(listReportItem,listOrder);
//        printReport( listReportItem);
        
        
        return listReportItem;
    }

    private void calculateSalesForReportData(List<ReportItem> listReportItem, List<Order> listOrder) {
        for (Order order : listOrder) {
            String dateString = dateFormat.format(order.getOrderTime());
            ReportItem rI = new ReportItem(dateString);
            if(listReportItem.contains(rI)){
                int index = listReportItem.indexOf(rI);
                ReportItem reportItem = listReportItem.get(index);
                reportItem.addGrowwSales(order.getSubtotal());
                reportItem.addNetSales(order.getSubtotal() - order.getProductCost());
                reportItem.plusCountOrder();
            }
        }
    }

    private void printReport( List<ReportItem> listReportItem) {
        System.out.println("-------------------");
        for (ReportItem reportItem : listReportItem) {
            System.out.println(reportItem.getIdentifier() + "   " + reportItem.getGrossSales() + "   "+
                    reportItem.getNetSales() + "   " + reportItem.getOrdersCount());
        }
    }

    private List<ReportItem> createReportData(Date startTime, Date endTime, String period) {
        List<ReportItem> listReportItem = new ArrayList<>();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        startDate.setTime(startTime);
        endDate.setTime(endTime);
        
        Date currentDate = startDate.getTime();
        String dateString = "";
        
        while(startDate.before(endDate)) {
            currentDate = startDate.getTime();
            dateString = dateFormat.format(currentDate);
            
            listReportItem.add(new ReportItem(dateString));
            if(period.equals("days")) {
                startDate.add(Calendar.DAY_OF_MONTH, 1);                
            }
            else {
                startDate.add(Calendar.MONTH, 1);
            }
        }
       
         
        return listReportItem;
        
        
    }

    private void printRawData(List<Order> listOrder) {
        
        for (Order order : listOrder) {
            System.out.println(order.getId()+" "+order.getOrderTime()+" "+order.getProductCost()+" " + 
        order.getSubtotal()+" "+order.getTotal());
        }
    }
    
    
    
    
}   
