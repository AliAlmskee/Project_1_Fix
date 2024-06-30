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

//	@Bean
//	public CommandLineRunner commandLineRunner(
//			AuthenticationService service
//	) {
//		return args -> {
//			var admin = RegisterRequest.builder()
//					.firstname("Admin")
//					.lastname("Admin")
//					.email("admin@mail.com")
//					.password("password")
//					.role(ADMIN)
//					.build();
//			System.out.println("Admin token: " + service.register(admin).getAccessToken());
//
//			var manager = RegisterRequest.builder()
//					.firstname("Admin")
//					.lastname("Admin")
//					.email("manager@mail.com")
//					.password("password")
//					.role(MANAGER)
//					.build();
//			System.out.println("Manager token: " + service.register(manager).getAccessToken());
//
//			var admin_manger = RegisterRequest.builder()
//					.firstname("Admin2")
//					.lastname("Admin2")
//					.email("admin@mail.com2")
//					.password("password2")
//					.role(CLIENT_WORKER)
//					.build();
//			System.out.println("admin_manger token: " + service.register(admin_manger).getAccessToken());
//
//		};
//	}
}
