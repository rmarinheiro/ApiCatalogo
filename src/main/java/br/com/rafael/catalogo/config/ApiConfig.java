package br.com.rafael.catalogo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApiConfig {
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	
		return new BCryptPasswordEncoder();
	}

}
