package com.example.web.payments;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.web.entity.Order;
import com.example.web.entity.enums.OrderStatus;
import com.example.web.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VNPayService {
    @Autowired
    private VNPayConfig vnpayConfig;

    @Autowired
    private OrderRepository orderRepository;

    private static final Logger logger = LoggerFactory.getLogger(VNPayService.class);

    public PaymentResponseDTO createPaymentUrl(PaymentRequestDTO request) {
        // Kiểm tra đơn hàng
        logger.info("Nhận yêu cầu thanh toán: {}", request);
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        if (order.getStatus() != OrderStatus.NO_PAID) {
            throw new RuntimeException("Đơn hàng không ở trạng thái chưa thanh toán");
        }

        // Tạo tham số cho VNPay
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnpayConfig.getVnp_TmnCode());
        vnpParams.put("vnp_Amount", String.valueOf((long) (request.getAmount() * 100))); // Nhân 100 vì VNPay yêu cầu
                                                                                         // đơn vị là VND
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", String.valueOf(order.getId()));
        vnpParams.put("vnp_OrderInfo", request.getOrderInfo());
        vnpParams.put("vnp_OrderType", "billpayment"); // Loại hàng hóa, tùy chỉnh theo nhu cầu
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", request.getReturnUrl());
        vnpParams.put("vnp_IpAddr", "192.168.1.3"); // IP client, có thể lấy từ request
        vnpParams.put("vnp_CreateDate", VNPayUtil.getCurrentTimeString());
        // vnpParams.put("vnp_BankCode", "NCB");

        // Tạo chữ ký bảo mật
        String queryString = VNPayUtil.buildQueryString(vnpParams);
        String secureHash = VNPayUtil.hmacSHA512(vnpayConfig.getVnp_HashSecret(), queryString);
        vnpParams.put("vnp_SecureHash", secureHash);

        // Tạo URL thanh toán
        String paymentUrl = vnpayConfig.getVnp_Url() + "?" + VNPayUtil.buildQueryString(vnpParams);
        return new PaymentResponseDTO(paymentUrl);
    }

    public PaymentResultDTO verifyPayment(Map<String, String> queryParams) {
        String vnp_SecureHash = queryParams.get("vnp_SecureHash");
        queryParams.remove("vnp_SecureHash"); // Loại bỏ secureHash để tạo lại chữ ký

        // Tạo lại chữ ký từ các tham số nhận được
        String queryString = VNPayUtil.buildQueryString(queryParams);
        String computedHash = VNPayUtil.hmacSHA512(vnpayConfig.getVnp_HashSecret(), queryString);

        // Kiểm tra chữ ký
        if (!computedHash.equalsIgnoreCase(vnp_SecureHash)) {
            System.out.println("==========================================================");
            return new PaymentResultDTO(false, "Chữ ký không hợp lệ", null);
        }

        // Kiểm tra mã giao dịch và trạng thái thanh toán
        String orderIdStr = queryParams.get("vnp_TxnRef");
        String responseCode = queryParams.get("vnp_ResponseCode");
        System.out.println(responseCode);
        Long orderId = Long.parseLong(orderIdStr);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        if (order.getStatus() != OrderStatus.NO_PAID) {
            return new PaymentResultDTO(false, "Đơn hàng đã được xử lý", orderId);
        }

        if ("00".equals(responseCode)) {
            // Thanh toán thành công
            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
            return new PaymentResultDTO(true, "Thanh toán thành công", orderId);
        } else {
            // Tránh lỗi null
            String errorMessage;
            if ("76".equals(responseCode)) {
                errorMessage = "Ngân hàng thanh toán không được hỗ trợ";
            } else if (responseCode == null) {
                errorMessage = "Không nhận được mã phản hồi từ VNPay";
            } else {
                errorMessage = "Mã lỗi VNPay: " + responseCode;
            }
            return new PaymentResultDTO(false, errorMessage, orderId);
        }
    }

    public static void main(String[] args) {
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Amount", "44800000");
        params.put("vnp_Command", "pay");
        params.put("vnp_CreateDate", "20250615125516");
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_IpAddr", "127.0.0.1");
        params.put("vnp_Locale", "vn");
        params.put("vnp_OrderInfo", "Thanh toán đơn hàng #41");
        params.put("vnp_OrderType", "250000");
        params.put("vnp_ReturnUrl", "http://localhost:5173/payment-result");
        params.put("vnp_SecureHash",
                "4a7b304a45a2027510ec8063aa379b55ab06a79980c40c362fbc8ab68e28182c0c38f9b3bd5666df5af769553e125ec687c886e007afc4abfb7b228769f97920");
        params.put("vnp_TmnCode", "Z7DJFUUY");
        params.put("vnp_TxnRef", "41");
        params.put("vnp_Version", "2.1.0");
        // params.put("vnp_bankcode", "");
        String queryString = VNPayUtil.buildQueryString(params);
        System.out.println("Query string dùng để hash: " + queryString);
        String hashSecret = "ZHANQUIFQ1GU5RIJ9VCFN8BYRR2AMFX8"; // Thay bằng vnp_HashSecret thực tế
        String computedHash = VNPayUtil.hmacSHA512(hashSecret, queryString);

        System.out.println("Computed Hash: " + computedHash);
    }
}