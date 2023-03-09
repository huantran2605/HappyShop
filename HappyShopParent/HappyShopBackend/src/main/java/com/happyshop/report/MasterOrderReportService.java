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
public class MasterOrderReportService extends AbstractReportService{
    
    @Autowired
    OrderRepository orderRepository;
    
    
    @Override
    public List<ReportItem> getReportDataByDateRangeInternal(Date startTime, Date endTime, ReportType reportType) {
        List<Order> listOrder = orderRepository.findByOrderTimeBetween(startTime, endTime);
        
//        printRawData(listOrder);
        
        List<ReportItem> listReportItem = createReportData(startTime, endTime, reportType);
               
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
                reportItem.addGrossSales(order.getSubtotal());
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

    private List<ReportItem> createReportData(Date startTime, Date endTime, ReportType reportType) {
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
            if(reportType.equals(ReportType.DAY)) {
                startDate.add(Calendar.DAY_OF_MONTH, 1);                
            }
            else if(reportType.equals(ReportType.MONTH)) {
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
