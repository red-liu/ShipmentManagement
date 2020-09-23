package com.redliu.shipmentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class ShipmentManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShipmentManagementApplication.class, args);
	}

}
