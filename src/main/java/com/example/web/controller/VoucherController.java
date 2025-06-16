package com.example.web.controller;

import com.example.web.dto.request.VoucherRequest;
import com.example.web.dto.response.ApiResponse;
import com.example.web.entity.Voucher;
import com.example.web.service.VoucherService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vouchers")
@Slf4j
public class VoucherController {
    @Autowired
    VoucherService voucherService;


    @Autowired
    private LogService logService;
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;


    @PostMapping("/add")
    public ApiResponse<Voucher> creatVoucher(@RequestBody @Valid VoucherRequest req) {
        ApiResponse<Voucher> apiResponse = new ApiResponse<>();
        apiResponse.setResult(voucherService.creatVoucher(req));
        return apiResponse;
    }

    @GetMapping("/all")
    public ApiResponse<List<Voucher>> getAllVouchers() {
        return ApiResponse.<List<Voucher>>builder()
                .result(voucherService.getAllVoucher())
                .build();

    }

    @PutMapping("/update/{voucherId}")
    public ApiResponse<Voucher> updateVoucher(@PathVariable("voucherId") long id, @RequestBody VoucherRequest req) {
        ApiResponse<Voucher> apiResponse = new ApiResponse<>();
        apiResponse.setResult(voucherService.updateVoucher(id, req));
        return apiResponse;
    }

    @DeleteMapping("/delete/{voucherId}")
    public void deleteVoucher(@PathVariable("voucherId") long id) {
        voucherService.deleteVoucher(id);
    }

}
