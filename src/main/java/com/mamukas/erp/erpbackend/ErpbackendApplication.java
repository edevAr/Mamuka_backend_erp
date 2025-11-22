package com.mamukas.erp.erpbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableMethodSecurity(prePostEnabled = true)
public class ErpbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ErpbackendApplication.class, args);
	}

}
