package com.happyshop.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.order.Order;
import com.happyshop.common.entity.order.OrderDetail;
import com.happyshop.order.OrderDetailRepository;

@Service
public class OrderDetailReportService extends AbstractReportService {
    @Autowired
    private OrderDetailRepository orderDetailRepo;
    
    
    @Override
    protected List<ReportItem> getReportDataByDateRangeInternal(Date startDate, Date endDate, ReportType reportType) {

        List<OrderDetail> orderDetails = new ArrayList<>();
        List<ReportItem> listReportItem = new ArrayList<>();
        if(reportType.equals(ReportType.CATEGORY)) {
            orderDetails = orderDetailRepo.findWithCategoryAndTimeBetween(startDate, endDate);
            listReportItem = createReportDataForCategory(orderDetails);
            calculateDataReportForCategory(listReportItem, orderDetails);
        }
        else if(reportType.equals(ReportType.PRODUCT)) {
            orderDetails = orderDetailRepo.findWithProductAndTimeBetween(startDate, endDate);
            listReportItem = createReportDataForProduct(orderDetails);
            calculateDataReportForProduct(listReportItem, orderDetails);
        }              
//        System.out.println("raw data:----------");
//        printRawData(orderDetails);
////        System.out.println("report:---------");
//        printReport(listReportItem);
        
        return listReportItem;
    }


    private void printRawData(List<OrderDetail> orderDetails) {
        for (OrderDetail orderDetail : orderDetails) {
            System.out.println( orderDetail.getQuantity()+"  " +orderDetail.getProduct().getName() + "  " + orderDetail.getSubtotal() + "  "+
                    orderDetail.getProductCost() + "  " + orderDetail.getShippingCost());
        }
    }
    
    private List<ReportItem> createReportDataForCategory(List<OrderDetail> orderDetails) {
        List<ReportItem> listReportItem = new ArrayList<>();
        for (OrderDetail oD : orderDetails) {
            String identifier = oD.getProduct().getCategoryName();
            ReportItem reportItem = new ReportItem(identifier);
            if(!listReportItem.contains(reportItem)) {
                listReportItem.add(reportItem);
            }           
        }
        
        
        return listReportItem;       
    }
    
    private List<ReportItem> createReportDataForProduct(List<OrderDetail> orderDetails) {
        List<ReportItem> listReportItem = new ArrayList<>();
        for (OrderDetail oD : orderDetails) {
            String identifier = oD.getProduct().getName();
            ReportItem reportItem = new ReportItem(identifier);
            if(!listReportItem.contains(reportItem)) {
                listReportItem.add(reportItem);
            }           
        }
        
        
        return listReportItem;       
    }
    
    private void printReport( List<ReportItem> listReportItem) {
        System.out.println("-------------------");
        for (ReportItem reportItem : listReportItem) {
            System.out.println(reportItem.getIdentifier() + "  " +reportItem.getGrossSales() + 
                    "  " + reportItem.getNetSales() + "  " + reportItem.getProductsCount());
        }
    }
    
    private void calculateDataReportForCategory(List<ReportItem> listReportItem, List<OrderDetail> listOrderDetail) {
       for (OrderDetail oD : listOrderDetail) {
           String identifier = oD.getProduct().getCategoryName();
           ReportItem rI = new ReportItem(identifier);
           int index = listReportItem.indexOf(rI);
           ReportItem reportItem = listReportItem.get(index);//reportItem in listReportItem
           
           float grossSale = oD.getSubtotal(); 
           float netSale = oD.getSubtotal() - oD.getProductCost();
           reportItem.addGrossSales(grossSale);
           reportItem.addNetSales(netSale);
           reportItem.plusCountProduct(oD.getQuantity());
       }
    }
    
    private void calculateDataReportForProduct(List<ReportItem> listReportItem, List<OrderDetail> listOrderDetail) {
        for (OrderDetail oD : listOrderDetail) {
            String identifier = oD.getProduct().getName();
            ReportItem rI = new ReportItem(identifier);
            int index = listReportItem.indexOf(rI);
            ReportItem reportItem = listReportItem.get(index);//reportItem in listReportItem
            
            float grossSale = oD.getSubtotal(); 
            float netSale = oD.getSubtotal() - oD.getProductCost();
            reportItem.addGrossSales(grossSale);
            reportItem.addNetSales(netSale);
            reportItem.plusCountProduct(oD.getQuantity());
        }
     }

}
