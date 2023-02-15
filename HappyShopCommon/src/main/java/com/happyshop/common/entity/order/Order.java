package com.happyshop.common.entity.order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;


import com.happyshop.common.entity.Address;
import com.happyshop.common.entity.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;
    
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;
    
    @Column(name = "address_line_1", nullable = false, length = 64)
    private String addressLine1;
    
    @Column(name = "address_line_2", length = 64)
    private String addressLine2;
    
    @Column(nullable = false, length = 45)
    private String city;
    
    @Column(nullable = false, length = 45)
    private String state;
    
    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;
    
    @Column(nullable = false, length = 45)
    private String country;
    
    private Date orderTime;
    
    private float shippingCost;
    private float productCost;
    private float subtotal;
    private float tax;      
    private float total;
    
    private int deliverDays;
    private Date deliverDate;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("updatedTime ASC")
    private List<OrderTrack> orderTracks = new ArrayList<>();
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails = new HashSet<>();
    //-----------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public float getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(float shippingCost) {
        this.shippingCost = shippingCost;
    }

    public float getProductCost() {
        return productCost;
    }

    public void setProductCost(float productCost) {
        this.productCost = productCost;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getDeliverDays() {
        return deliverDays;
    }

    public void setDeliverDays(int deliverDays) {
        this.deliverDays = deliverDays;
    }

    public Date getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Set<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
    
    
    public List<OrderTrack> getOrderTracks() {
        return orderTracks;
    }

    public void setOrderTracks(List<OrderTrack> orderTracks) {
        this.orderTracks = orderTracks;
    }

    //----------------
    public void copyAddressFromCustomer() {
        setFirstName(customer.getFirstName());
        setLastName(customer.getLastName());
        setPhoneNumber(customer.getPhoneNumber());
        setAddressLine1(customer.getAddressLine1());
        setAddressLine2(customer.getAddressLine2());
        setCity(customer.getCity());
        setCountry(customer.getCountry().getName());
        setPostalCode(customer.getPostalCode());
        setState(customer.getState());      
    }
    
    @Transient
    public String getDestination() {
        String destination = "";
        if(!city.isEmpty() &&  city != null) destination += city;
        if(!state.isEmpty() &&  state != null) destination += ", " + state;       
        destination += ", " + country;       
        return destination;
    }
 
    public void copyShippingAddress(Address address) {  
        setFirstName(address.getFirstName());
        setLastName(address.getLastName());
        setPhoneNumber(address.getPhoneNumber());
        setAddressLine1(address.getAddressLine1());
        setAddressLine2(address.getAddressLine2());
        setCity(address.getCity());
        setCountry(address.getCountry().getName());
        setPostalCode(address.getPostalCode());
        setState(address.getState());      
    }
    
    @Transient
    public String getShippingAddress() {
        String address = "Full Name: " + firstName;

        if (!lastName.isEmpty() && lastName != null)
            address += " " + lastName;
        if (!addressLine1.isEmpty() && addressLine1 != null)
            address += ". Address: " + addressLine1;
        if (addressLine2 != null && !addressLine2.isEmpty())
            address += ", " + addressLine2;
        if (!city.isEmpty() && city != null)
            address += ", " + city;
        if (!state.isEmpty() && state != null)
            address += ", " + state;

        address += ", " + country;
        if (!postalCode.isEmpty() && postalCode != null)
            address += ". Postal Code: " + postalCode;
        if (!phoneNumber.isEmpty() && phoneNumber != null)
            address += ". Phone number: " + phoneNumber;

        return address;
    }
    
    @Transient
    public String getDeliverDateOnForm() {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormatter.format(this.deliverDate);
    }
    
    @Transient
    public String getRecipientName() {
        String name = firstName;
        if (!lastName.isEmpty() && lastName != null) {
            name += " " + lastName;
        }
        return name;
    }
    
    @Transient
    public String getRecipientAddress() {
        String address = "";

        if (!addressLine1.isEmpty() && addressLine1 != null)
            address += ". Address: " + addressLine1;
        if (addressLine2 != null && !addressLine2.isEmpty())
            address += ", " + addressLine2;
        if (!city.isEmpty() && city != null)
            address += ", " + city;
        if (!state.isEmpty() && state != null)
            address += ", " + state;

        address += ", " + country;
        if (!postalCode.isEmpty() && postalCode != null)
            address += ". Postal Code: " + postalCode;

        return address;
    }
    
    @Transient
    public boolean isCOD() {
        if(!paymentMethod.equals(PaymentMethod.COD)) {
            return false; 
        }
        return true;
    }
    
    @Transient
    public boolean isPicked() {
        return hasStatus(OrderStatus.PICKED);
    }
    @Transient
    public boolean isShipping() {
        return hasStatus(OrderStatus.SHIPPING);
    }
    @Transient
    public boolean isDelivered() {
        return hasStatus(OrderStatus.DELIVERED);
    }    
    @Transient
    public boolean isReturned() {
        return hasStatus(OrderStatus.RETURNED);
    }  
    @Transient
    public boolean isProcessing() {
        return hasStatus(OrderStatus.PROCESSING);
    }  
    @Transient
    public boolean isReturnRequested() {
        return hasStatus(OrderStatus.RETURN_REQUESTED);
    }  
    
    public boolean hasStatus (OrderStatus status) {
        for (OrderTrack orderTrack : orderTracks) {
            if(orderTrack.getStatus().equals(status)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Transient
    public List<String> getProductNames() {
        List<String> names = new ArrayList<>();
        for (OrderDetail detail : orderDetails) {
            names.add(detail.getProduct().shortName());
        }
        
        return names;
    }  
    
}

