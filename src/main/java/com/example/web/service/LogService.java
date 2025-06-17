package com.example.web.service;

import com.example.web.dto.request.LogRequest;
import com.example.web.entity.Log;
import com.example.web.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class LogService {
    @Autowired
    LogRepository logRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "error_serializing";
        }
    }

    public void addLog(LogRequest request) {
        Log log = new Log();
        log.setIp(request.getIp());
        log.setUser(request.getUser());
        log.setDate(request.getDate());
        log.setAction(request.getAction());
        log.setDataOut(toJson(request.getDataOut()));
        log.setDataIn(toJson(request.getDataIn()));
        log.setLevel(request.getLevel());
        log.setResource(request.getResource());
        logRepository.save(log);

    }

    public List<Log> getAllLog() {
        return logRepository.findAll();
    }
}
