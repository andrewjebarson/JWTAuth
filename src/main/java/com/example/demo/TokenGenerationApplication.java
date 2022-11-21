package com.example.demo;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
@EnableEurekaClient
public class TokenGenerationApplication  implements CommandLineRunner{
	
	@Autowired
	UserRepository userrepo;
	
	@Autowired
	MyUserDetailsService userDetailsService;

	public static void main(String[] args) {
		SpringApplication.run(TokenGenerationApplication.class, args);
	}

	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		 return config.getAuthenticationManager();
	}

	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable() //cross site request forgery
		.authorizeRequests()
		.antMatchers("/authenticate","/validate")
		.permitAll()
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		
		http.userDetailsService(userDetailsService);
		
		return http.build();
	}
	
	
	
	
	
	
	

	@Override
	public void run(String... args) throws Exception {
		User u =new User();
		u.setUsername("anand");
		u.setPassword(getPasswordEncoder().encode("2222"));
		
		u.setMobileNo("9566579119");
		u.setEmail("indhurodeo29@gmail.com");
		u.setRoles("user");
		u.setAccountNonExpired(true); u.setAccountNonLocked(true);
		  u.setCredentialsNonExpired(true); u.setEnabled(true);
		  u.setAuthorities(Arrays.asList("ROLE_USER"));
		userrepo.save(u);
		
		
			
		}
		
	}
	
	
	

