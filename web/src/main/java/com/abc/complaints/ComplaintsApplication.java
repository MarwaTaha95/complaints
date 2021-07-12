package com.abc.complaints;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableAspectJAutoProxy
@EnableMongoRepositories("com.abc.complaints")
@SpringBootApplication
@EnableCaching
public class ComplaintsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComplaintsApplication.class, args);
	}

}
