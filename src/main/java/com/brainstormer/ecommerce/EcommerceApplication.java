package com.brainstormer.ecommerce;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // automatically enable auditing in jpa
public class EcommerceApplication implements CommandLineRunner {
	public static void main(String[] args) {
		// Setting up environment variables from .env file
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach(entry -> {
			System.setProperty(entry.getKey(), entry.getValue());
		});

		SpringApplication.run(EcommerceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Ecommerce application is live......");
	}
}
