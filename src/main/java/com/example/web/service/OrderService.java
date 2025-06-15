package com.example.web.service;

import com.example.web.controller.OrderController;
import com.example.web.dto.request.OrderRequest;
import com.example.web.dto.response.OrderResponse;
import com.example.web.dto.response.ProductResponse;
import com.example.web.entity.*;
import com.example.web.mapper.IOrderMapper;
import com.example.web.repository.OrderDetailRepository;
import com.example.web.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private IOrderMapper orderMapper;

    public OrderResponse createOrder(OrderRequest request) {
        User user = userService.getCurrentUser();
        Order order = orderMapper.toOrder(request);
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    public void cancelOrder(long orderId) {
        orderRepository.deleteById(orderId);
    }

    public List<OrderResponse> getAllOrder() {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            Set<OrderDetail> detail = orderDetailRepository.getAllByOrderId(order.getId());
            order.setDetails(detail);
        }
        return orders.stream().map(order -> orderMapper.toOrderResponse(order)).toList();
    }

    @Transactional
    public void testMapping() {
        List<Order> orders = orderRepository.findAllById(Arrays.asList(3L, 4L));
        for (Order order : orders) {
            OrderResponse response = orderMapper.toOrderResponse(order);
            System.out.println(response);
        }
    }

    public OrderResponse updateOrder(Long orderId, OrderRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new RuntimeException("Order not found with id: " + orderId));
        // Gán user nếu có
        if (request.getUser() != null) {
            order.setUser(request.getUser());
        }

        // Gán các thuộc tính nếu có
        if (request.getNote() != null) {
            order.setNote(request.getNote());
        }

        if (request.getPaymentMethod() != null) {
            order.setPaymentMethod(request.getPaymentMethod());
        }

        if (request.getTotalPrice() != 0) {
            order.setTotalPrice(request.getTotalPrice());
        }

        if (request.getTotalQuantity() != 0) {
            order.setTotalQuantity(request.getTotalQuantity());
        }

        if (request.getShippingFee() != 0) {
            order.setShippingFee(request.getShippingFee());
        }

        if (request.getOrderDate() != null) {
            order.setOrderDate(request.getOrderDate());
        }

        if (request.getStatus() != null) {
            order.setStatus(request.getStatus());
        }

        if (request.getDetails() != null && !request.getDetails().isEmpty()) {
            order.setDetails(request.getDetails());
        }
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    private LocalDate[] getWeekRange(LocalDate date) {
        // Lùi về thứ Hai đầu tuần
        LocalDate weekStart = date.with(DayOfWeek.MONDAY);
        // Tiến đến Chủ Nhật cuối tuần
        LocalDate weekEnd = date.with(DayOfWeek.SUNDAY);

        return new LocalDate[]{weekStart, weekEnd};
    }

    private LocalDate[] getMonthRange(LocalDate date) {
        LocalDate monthStart = date.withDayOfMonth(1);
        LocalDate monthEnd = date.withDayOfMonth(date.lengthOfMonth());
        return new LocalDate[]{monthStart, monthEnd};
    }

    private LocalDate[] getYearRange(LocalDate date) {
        LocalDate yearStart = date.withDayOfYear(1); // 01/01/yyyy
        LocalDate yearEnd = date.withDayOfYear(date.lengthOfYear()); // 31/12/yyyy hoặc 30/12/yyyy (năm nhuận)
        return new LocalDate[]{yearStart, yearEnd};
    }

    public  Product getWeekBestSelling(LocalDate date) {
        LocalDate[] weekRange = getWeekRange(date);
        LocalDate startOfWeek = weekRange[0];
        LocalDate endOfWeek = weekRange[1];

        List<Order> orders = orderRepository.findAllByOrderDateBetween(startOfWeek, endOfWeek);
        Map<Product, Integer> sold = new HashMap<>();

        for (Order order : orders) {
            Set<OrderDetail> details = orderDetailRepository.getAllByOrderId(order.getId());
            for (OrderDetail orderDetail : details) {
                Product product = orderDetail.getProduct();
                int currentQty = sold.getOrDefault(product, 0);
                sold.put(product, currentQty + orderDetail.getQuantity());
            }
        }

        // Tìm sản phẩm bán chạy nhất
        return sold.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey) // ✅ Lấy ra sản phẩm
                .orElse(null); // Nếu không có đơn hàng nào

    }


    public int getWeekTotal(LocalDate date) {
        LocalDate[] weekRange = getWeekRange(date);
        LocalDate startOfWeek = weekRange[0];
        LocalDate endOfWeek = weekRange[1];
        System.out.println("Start: " + startOfWeek);
        System.out.println("End: " + endOfWeek);
        List<Order> orders = orderRepository.findAllByOrderDateBetween(startOfWeek,endOfWeek);
        return orders.size();
    }

    public double getWeekSale(LocalDate date) {
        LocalDate[] weekRange = getWeekRange(date);
        LocalDate startOfWeek = weekRange[0];
        LocalDate endOfWeek = weekRange[1];
        System.out.println("Start: " + startOfWeek);
        System.out.println("End: " + endOfWeek);
        double totalSale =0;
        List<Order> orders = orderRepository.findAllByOrderDateBetween(startOfWeek,endOfWeek);
        for(Order order : orders) {
            totalSale+=order.getTotalPrice();
        }
        return totalSale;
    }

    public int getWeekCancelledOrder(LocalDate date) {
        LocalDate[] weekRange = getWeekRange(date);
        LocalDate startOfWeek = weekRange[0];
        LocalDate endOfWeek = weekRange[1];
        System.out.println("Start: " + startOfWeek);
        System.out.println("End: " + endOfWeek);
        List<Order> orders = orderRepository.findAllByOrderDateBetweenAndStatus(startOfWeek,endOfWeek,"CANCELLED");
        return orders.size();
    }

    public List<SaleDataPoint> getWeeklyRevenue(LocalDate date) {
        LocalDate[] weekRange = getWeekRange(date);
        List<SaleDataPoint> result = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate day = weekRange[0].plusDays(i);
            List<Order> orders = orderRepository.getOrdersByOrderDate(day);
            double total = orders.stream()
                    .mapToDouble(Order::getTotalPrice)
                    .sum();

            // Bạn có thể rút gọn tên ngày nếu cần:
            String label = day.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH); // "Mon", "Tue", ...

            result.add(new SaleDataPoint(label, total));
        }

        return result;
    }

    public List<SaleDataPoint> getMonthlyRevenue(LocalDate date) {
        LocalDate[] monthRange = getMonthRange(date);
        List<SaleDataPoint> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (int i = 0; i < 30; i++) {
            LocalDate day = monthRange[0].plusDays(i);
            List<Order> orders = orderRepository.getOrdersByOrderDate(day);
            double total = orders.stream().mapToDouble(Order::getTotalPrice).sum();
            // Bạn có thể rút gọn tên ngày nếu cần:
            String label = day.format(formatter); // Chỉ lấy MM-dd

            result.add(new SaleDataPoint(label, total));
        }

        return result;
    }

    public List<SaleDataPoint> getYearlyRevenue(LocalDate date) {
        int year = date.getYear();
        List<SaleDataPoint> result = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            LocalDate startOfMonth = LocalDate.of(year, month, 1);
            LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

            List<Order> orders = orderRepository.findAllByOrderDateBetween(startOfMonth, endOfMonth);
            double total = orders.stream().mapToDouble(Order::getTotalPrice).sum();

            String label = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH); // "Jan", "Feb", ...
            result.add(new SaleDataPoint(label, total));
        }

        return result;
    }


    public record SaleDataPoint(String label, double value) {}

}




