package com.example.web.payment;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.web.entity.Order;
import com.example.web.entity.enums.OrderStatus;
import com.example.web.repository.OrderRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class PaymentService {

    private final VNPayConfig vnPayConfig;

    @Autowired
    public PaymentService(VNPayConfig vnPayConfig, OrderRepository orderRepository) {
        this.vnPayConfig = vnPayConfig;
        this.orderRepository = orderRepository;
    }

    @Autowired
    private final OrderRepository orderRepository;

    public PaymentDTO.VNPayResponse createVNPayPayment(Long orderId, double amount, HttpServletRequest request) {
        // Kiểm tra đơn hàng tồn tại và trạng thái
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        if (order.getStatus() != OrderStatus.NO_PAID) {
            throw new RuntimeException("Order is not in NO_PAID status, cannot proceed with payment.");
        }

        long vnpAmount = (long) (amount * 100); // VNPay yêu cầu số tiền nhân 100
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(orderId);
        vnpParamsMap.put("vnp_Amount", String.valueOf(vnpAmount));
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;

        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return PaymentDTO.VNPayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl)
                .build();
    }
}