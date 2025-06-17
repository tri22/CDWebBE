package com.example.web;

import com.example.web.service.OrderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CdWebApplication {


public static void main(String[] args) {
	ApplicationContext context = SpringApplication.run(CdWebApplication.class, args);
	OrderService orderService = context.getBean(OrderService.class);
	orderService.testMapping();
}

}
