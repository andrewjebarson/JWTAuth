package com.example.demo;

import java.net.InetAddress;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.MyUserDetailsService;

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
	public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) { 
	EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils); 
	String ip = null; 
	try { 
	ip = InetAddress.getLocalHost().getHostAddress(); 
	} catch (Exception e) { 
	System.out.println("Exception"); 
	} 
	config.setNonSecurePort(8081); 
	config.setIpAddress(ip); 
	config.setPreferIpAddress(true); 
	return config; 
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
		u.setUsername("User");
		u.setPassword(getPasswordEncoder().encode("User@123"));
		
		u.setMobileNo("9876543210");
		u.setEmail("usertravelly@gmail.com");
		u.setRoles("User");
		u.setAccountNonExpired(true);
		u.setAccountNonLocked(true);
		u.setCredentialsNonExpired(true); 
		u.setEnabled(true);
		u.setAuthorities(Arrays.asList("ROLE_USER"));
		userrepo.save(u);
		
		User u1 =new User();
		u1.setUsername("Admin");
		u1.setPassword(getPasswordEncoder().encode("Admin@123"));
		
		u1.setMobileNo("9876545607");
		u1.setEmail("admintravelly@gmail.com");
		u1.setRoles("Admin");
		u1.setAccountNonExpired(true);
		u1.setAccountNonLocked(true);
		u1.setCredentialsNonExpired(true); 
		u1.setEnabled(true);
		u1.setAuthorities(Arrays.asList("ROLE_ADMIN"));
		userrepo.save(u1);
		
		
			
		}
		
	}
	
	
	

