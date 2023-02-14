package com.happyshop.order;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.order.Order;
import com.happyshop.common.entity.order.OrderStatus;
import com.happyshop.common.entity.order.OrderTrack;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository repo;
    
    @Override
    public Page<Order> findAll(String keyword, Pageable pageable){
        if (!keyword.isBlank()) {
            return repo.searchOrder(keyword, pageable);
        }
        return repo.findAll(pageable);
    }

    public Optional<Order> findById(Integer id) {
        return repo.findById(id);
    }

    public <S extends Order> S save(S entity) {
        return repo.save(entity);
    }

    public void deleteById(Integer id) {
        repo.deleteById(id);
    }
       
    public void updateStatus(Integer orderId, String status) {
        Order orderInDB = repo.findById(orderId).get();
        OrderStatus statusToUpdate = OrderStatus.valueOf(status);
        if(!orderInDB.hasStatus(statusToUpdate)) {
            List<OrderTrack> tracks =  orderInDB.getOrderTracks();
            
            OrderTrack newTrack = new OrderTrack();
            newTrack.setOrder(orderInDB);
            newTrack.setStatus(statusToUpdate);
            newTrack.setUpdatedTime(new Date());
            newTrack.setNote(statusToUpdate.defaultDescription());
            
            tracks.add(newTrack);
            
            orderInDB.setStatus(statusToUpdate);
            
            repo.save(orderInDB);          
        }
    }
}
