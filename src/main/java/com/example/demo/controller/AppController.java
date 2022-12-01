package com.example.demo.controller;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.dao.AppDao;
import com.example.demo.exception.UserDefinedExceptions;
import com.example.demo.model.Error;
import com.example.demo.model.Login;
import com.example.demo.model.MyUser;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.MyUserDetailsService;

@RestController
@CrossOrigin(origins={"http://localhost:4200/","*"})

public class AppController {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	AuthenticationManager manager;
	
	@Autowired
	MyUserDetailsService userDetailsService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AppDao appdao;
	
	
	@PostMapping("validate")
	public ResponseEntity<Boolean> doValidate(HttpServletRequest request){
		String authToken=request.getHeader("Authorization");
		String token=authToken.substring("Bearer ".length());
	
		try{
			System.out.println(token);
		Algorithm al=Algorithm.HMAC256("helloworld".getBytes());
		JWTVerifier verifier=JWT.require(al).build();
		DecodedJWT decode=verifier.verify(token);
		System.out.println(decode.getSubject());
		System.out.println(decode.getClaim("roles"));
		return new ResponseEntity<Boolean>(true,HttpStatus.OK);
	}catch(Exception e) {
		e.printStackTrace();
		throw new UserDefinedExceptions("Resource not found");
	}
	}
	
	
	
	
	
@PostMapping("authenticate")
	public ResponseEntity validate(@RequestBody Login login) {
		try {
			manager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(),login.getPassword()));
			UserDetails u=userDetailsService.loadUserByUsername(login.getUsername());
			
			MyUser mu=(MyUser) u;
			System.out.println(u.getUsername());
			System.out.println(login.getUsername());
			System.out.println(u.getPassword());
			System.out.println(login.getPassword());
			String token=JWT.create()
						.withSubject(u.getUsername())
						.withIssuedAt(new Date(System.currentTimeMillis()))
						.withExpiresAt(new Date(System.currentTimeMillis() +(500*60*2)))
						.sign(Algorithm.HMAC256("helloworld"));
				
				HttpHeaders headers=new HttpHeaders();
				headers.set("Authorization", "Bearer "+token);
				headers.set("Access-Control-Expose-Headers", "Authorization");
		       	return new ResponseEntity<>(mu,headers,HttpStatus.OK);
}catch(Exception e) {
			System.out.println(e);
			throw new UserDefinedExceptions("Resource not found");
			
		}
	}
	




@PostMapping("registerUser")
public ResponseEntity doregister(@RequestBody User user) {
	try {
	   User u= new User();
	   
	  u.setUsername(user.getUsername());
		u.setPassword(passwordEncoder.encode(user.getPassword()));
	  u.setMobileNo(user.getMobileNo());
	  u.setEmail(user.getEmail());
	  u.setRoles(user.getRoles());
	  u.setAccountNonExpired(true); 
	  u.setAccountNonLocked(true);
	  u.setCredentialsNonExpired(true);
	  u.setEnabled(true);
	  u.setAuthorities(Arrays.asList("ROLE_USER"));
	  userRepo.save(u);
	   
	   String token=JWT.create()
					.withSubject(u.getUsername())
					.withIssuedAt(new Date(System.currentTimeMillis()))
					.withExpiresAt(new Date(System.currentTimeMillis() +(1000*60*2)))
					.sign(Algorithm.HMAC256("helloworld"));
			
			HttpHeaders headers=new HttpHeaders();
			headers.set("Authorization", "Bearer "+token);
		
			return new ResponseEntity<>(u,headers,HttpStatus.OK);
		
	   }catch(Exception e) {
		   throw new UserDefinedExceptions("Resource not found");
	   }
}

@GetMapping("/")
public String getHome() {
	return "welcome home";
}

//@PostMapping("updateUser")
//public ResponseEntity updateUser(@RequestBody User user) {
//	try {
//		System.out.println(user.getUserid());
//	appdao.modifyUser(user);
//	
//		return new ResponseEntity<>(user,HttpStatus.OK);
//		}catch(Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity(HttpStatus.NOT_FOUND);
//		}
//}
	
@PutMapping("update")
public  ResponseEntity updatepass(@RequestBody User user) {
	try {
		System.out.println("helloworld"+user.getUserid());
	
	return new ResponseEntity(appdao.modifypassword(user),HttpStatus.OK);
	}catch(Exception e) {
		
		e.printStackTrace();
		throw new UserDefinedExceptions("Resource not found");
	}	
}

@GetMapping("getUserById/{userId}")
public ResponseEntity getUserById(@PathVariable int userId) {
	try {
		User u=appdao.searchUserById(userId);
		return new ResponseEntity<>(u,HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Userid not found",HttpStatus.NOT_FOUND);
		}
	}

@ExceptionHandler(UserDefinedExceptions.class)
public ResponseEntity existUser(Exception e) {
	Error er=new Error();
	er.setErCode(HttpStatus.NOT_FOUND.toString());
	er.setErMsg(e.getMessage());
	return new ResponseEntity(er,HttpStatus.NOT_FOUND);
}


}
