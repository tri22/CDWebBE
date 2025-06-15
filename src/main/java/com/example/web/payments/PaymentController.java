package com.example.web.payments;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment/vnpay")
public class PaymentController {
    @Autowired
    private VNPayService vnPayService;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody PaymentRequestDTO request) {
        PaymentResponseDTO response = vnPayService.createPaymentUrl(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/return")
    public ResponseEntity<PaymentResultDTO> verifyPayment(@RequestParam Map<String, String> queryParams) {
        PaymentResultDTO result = vnPayService.verifyPayment(queryParams);
        return ResponseEntity.ok(result);
    }
}