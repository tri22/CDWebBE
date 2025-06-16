package com.example.web.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.web.entity.enums.OrderStatus;
import com.example.web.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    @Autowired
    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @GetMapping("/vnpay")
    public ResponseObject<PaymentDTO.VNPayResponse> pay(
            @RequestParam("orderId") Long orderId,
            @RequestParam("amount") double amount,
            HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success",
                paymentService.createVNPayPayment(orderId, amount, request));
    }

    @GetMapping("/vnpay/return")
    public String payCallbackHandler(HttpServletRequest request, Model model) {
        String status = request.getParameter("vnp_ResponseCode");
        String transactionStatus = request.getParameter("vnp_TransactionStatus");
        String txnRef = request.getParameter("vnp_TxnRef");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String bankCode = request.getParameter("vnp_BankCode");
        String amount = request.getParameter("vnp_Amount");
        String transactionDate = request.getParameter("vnp_PayDate");

        // Tách orderId từ vnp_TxnRef
        Long orderId = Long.parseLong(txnRef.split("-")[0]);
        if ("00".equals(status) && "00".equals(transactionStatus)) {
            // Thanh toán thành công
            orderService.updateOrderPaymentStatus(orderId, transactionId, bankCode, OrderStatus.PAID);

            double formattedAmount = Double.parseDouble(amount) / 100;
            String formattedDate = transactionDate.substring(0, 4) + "-" +
                    transactionDate.substring(4, 6) + "-" +
                    transactionDate.substring(6, 8) + " " +
                    transactionDate.substring(8, 10) + ":" +
                    transactionDate.substring(10, 12) + ":" +
                    transactionDate.substring(12);

            model.addAttribute("transactionId", transactionId);
            model.addAttribute("bankCode", bankCode);
            model.addAttribute("formattedDate", formattedDate);
            model.addAttribute("formattedAmount", formattedAmount);

            return "success"; // Trang Thymeleaf hiển thị kết quả thành công
        } else {
            // Thanh toán thất bại
            orderService.updateOrderPaymentStatus(orderId, transactionId, bankCode, OrderStatus.FAILED);
            return "failure"; // Trang Thymeleaf hiển thị kết quả thất bại
        }
    }
}