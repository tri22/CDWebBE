package com.example.web.controller;

import com.example.web.configuration.JwtAuthenticationFilter;
import com.example.web.dto.request.LogRequest;
import com.example.web.dto.request.VoucherRequest;
import com.example.web.dto.response.ApiResponse;
import com.example.web.entity.User;
import com.example.web.entity.Voucher;
import com.example.web.service.LogService;
import com.example.web.service.VoucherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public ApiResponse<Voucher> creatVoucher(@RequestBody @Valid VoucherRequest req,
                                             HttpServletRequest request) {
        ApiResponse<Voucher> apiResponse = new ApiResponse<>();
        User user = jwtAuthenticationFilter.extractUser(request);
        String ip = request.getRemoteAddr();

        try {
            Voucher result = voucherService.creatVoucher(req);
            logService.addLog(LogRequest.builder()
                    .action("CREATE_VOUCHER_SUCCESS")
                    .user(user)
                    .ip(ip)
                    .level("INFO")
                    .dataIn(req)
                    .dataOut(result)
                    .date(new Date())
                    .resource("VOUCHER MANAGEMENT")
                    .build());
            apiResponse.setResult(result);
        } catch (Exception e) {
            logService.addLog(LogRequest.builder()
                    .action("CREATE_VOUCHER_FAILED")
                    .user(user)
                    .ip(ip)
                    .level("ERROR")
                    .dataIn(req)
                    .dataOut(e.getMessage())
                    .date(new Date())
                    .resource("VOUCHER MANAGEMENT")
                    .build());
            throw e;
        }

        return apiResponse;
    }


    @GetMapping("/all")
    public ApiResponse<List<Voucher>> getAllVouchers() {
        return ApiResponse.<List<Voucher>>builder()
                .result(voucherService.getAllVoucher())
                .build();

    }

    @PutMapping("/update/{voucherId}")
    public ApiResponse<Voucher> updateVoucher(@PathVariable("voucherId") long id,
                                              @RequestBody VoucherRequest req,
                                              HttpServletRequest request) {
        ApiResponse<Voucher> apiResponse = new ApiResponse<>();
        User user = jwtAuthenticationFilter.extractUser(request);
        String ip = request.getRemoteAddr();

        try {
            Voucher result = voucherService.updateVoucher(id, req);
            logService.addLog(LogRequest.builder()
                    .action("UPDATE_VOUCHER_SUCCESS")
                    .user(user)
                    .ip(ip)
                    .level("INFO")
                    .dataIn(req)
                    .dataOut(result)
                    .date(new Date())
                    .resource("VOUCHER MANAGEMENT")
                    .build());
            apiResponse.setResult(result);
        } catch (Exception e) {
            logService.addLog(LogRequest.builder()
                    .action("UPDATE_VOUCHER_FAILED")
                    .user(user)
                    .ip(ip)
                    .level("ERROR")
                    .dataIn(req)
                    .dataOut(e.getMessage())
                    .date(new Date())
                    .resource("VOUCHER MANAGEMENT")
                    .build());
            throw e;
        }

        return apiResponse;
    }


    @DeleteMapping("/delete/{voucherId}")
    public void deleteVoucher(@PathVariable("voucherId") long id,
                              HttpServletRequest request) {
        User user = jwtAuthenticationFilter.extractUser(request);
        String ip = request.getRemoteAddr();

        try {
            voucherService.deleteVoucher(id);
            logService.addLog(LogRequest.builder()
                    .action("DELETE_VOUCHER_SUCCESS")
                    .user(user)
                    .ip(ip)
                    .level("INFO")
                    .dataIn(id)
                    .dataOut("Voucher deleted")
                    .date(new Date())
                    .resource("VOUCHER MANAGEMENT")
                    .build());
        } catch (Exception e) {
            logService.addLog(LogRequest.builder()
                    .action("DELETE_VOUCHER_FAILED")
                    .user(user)
                    .ip(ip)
                    .level("ERROR")
                    .dataIn(id)
                    .dataOut(e.getMessage())
                    .date(new Date())
                    .resource("VOUCHER MANAGEMENT")
                    .build());
            throw e;
        }
    }
}
