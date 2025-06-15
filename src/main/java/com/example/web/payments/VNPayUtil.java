package com.example.web.payments;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import jakarta.servlet.http.HttpServletRequest;

public class VNPayUtil {
    public static String hmacSHA512(final String key, final String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo chữ ký HMAC-SHA512", e);
        }
    }

    // Lấy thời gian hiện tại định dạng chuẩn giao dịch
    public static String getCurrentTimeString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        return formatter.format(new Date());
    }

    // Tạo query string từ map
    public static String buildQueryString(Map<String, String> params) {
        // List<String> keys = new ArrayList<>(params.keySet());
        // Collections.sort(keys);
        // StringBuilder sb = new StringBuilder();
        // for (String key : keys) {
        // String value = params.get(key);
        // if (value != null && !value.isEmpty()) {
        // try {
        // // Mã hóa URL cho giá trị tham số
        // String encodedValue = URLEncoder.encode(value,
        // StandardCharsets.UTF_8.toString());
        // sb.append(key).append("=").append(encodedValue).append("&");
        // } catch (Exception e) {
        // throw new RuntimeException("Lỗi khi mã hóa tham số: " + key, e);
        // }
        // }
        // }
        // return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
        return params.entrySet().stream()
                .filter(e -> e.getValue() != null && !e.getValue().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }

    // Sinh số ngẫu nhiên (Dùng để tạo mã đơn hàng, mã giao dịch, OTP, hoặc các mã
    // định danh ngẫu nhiên.)
    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // Tạo URL thanh toán với mã hóa dữ liệu
    public static String getPaymentURL(Map<String, String> paramsMap, boolean encodeKey) {
        return paramsMap.entrySet().stream().filter(entry -> entry.getValue() != null && !entry.getValue().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> (encodeKey ? URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII)
                        : entry.getKey()) + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                .collect(Collectors.joining("&"));
    }

    // Lấy địa chỉ IP của client từ HTTP request
    public static String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }
}
