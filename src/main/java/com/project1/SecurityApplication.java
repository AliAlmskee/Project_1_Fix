package com.project1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class SecurityApplication {

	public static void main(String[] args) {

		SpringApplication.run(SecurityApplication.class, args);

//		ApplicationContext context =SpringApplication.run(SecurityApplication.class, args);
//		AuditorAware<Integer> auditorAware = context.getBean(AuditorAware.class);
//		System.out.println(auditorAware.getCurrentAuditor());
	}


}
