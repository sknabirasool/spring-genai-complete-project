package com.mdl.ai_excel_agent;



import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;


@SpringBootApplication
@EnableKafka
public class AiExcelAgentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiExcelAgentApplication.class, args);


	}

}
