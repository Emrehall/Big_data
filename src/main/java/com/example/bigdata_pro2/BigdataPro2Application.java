package com.example.bigdata_pro2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching; // <-- Bunu import ettiğinizden emin olun

@EnableCaching
@SpringBootApplication
public class BigdataPro2Application {

	public static void main(String[] args) {
		SpringApplication.run(BigdataPro2Application.class, args);
	}

}
