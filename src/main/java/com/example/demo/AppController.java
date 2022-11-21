package com.example.demo;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@RestController
@CrossOrigin(origins={"http://localhost:4200","http://localhost:3000"})

public class AppController {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	AuthenticationManager manager;
	
	@Autowired
	MyUserDetailsService userDetailsService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	
	@GetMapping("validate")
	public ResponseEntity<Boolean> doValidate(HttpServletRequest request){
		String authToken=request.getHeader("Authorization");
		String token=authToken.substring("Bearer ".length());
		try{
		Algorithm al=Algorithm.HMAC256("helloworld".getBytes());
		JWTVerifier verifier=JWT.require(al).build();
		DecodedJWT decode=verifier.verify(token);
		System.out.println(decode.getSubject());
		System.out.println(decode.getClaim("roles"));
		return new ResponseEntity<Boolean>(true,HttpStatus.OK);
	}catch(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<Boolean>(false,HttpStatus.NOT_FOUND);
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
						.withExpiresAt(new Date(System.currentTimeMillis() +(1000*60*10)))
						.sign(Algorithm.HMAC256("helloworld"));
				
				HttpHeaders headers=new HttpHeaders();
				headers.set("Authorization", "Bearer "+token);
		       	return new ResponseEntity<>(mu,headers,HttpStatus.OK);
}catch(Exception e) {
			System.out.println(e);
			return new ResponseEntity<String>("false",HttpStatus.NOT_FOUND);
			
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
	  u.setAccountNonExpired(user.isAccountNonExpired());
	  u.setAccountNonLocked(user.isAccountNonLocked());
	  u.setCredentialsNonExpired(user.isCredentialsNonExpired());
	  u.setEnabled(user.isEnabled());
	  u.setAuthorities(user.getAuthorities());
	  userRepo.save(u);
	   
	   String token=JWT.create()
					.withSubject(u.getUsername())
					.withIssuedAt(new Date(System.currentTimeMillis()))
					.withExpiresAt(new Date(System.currentTimeMillis() +(1000*60*10)))
					.sign(Algorithm.HMAC256("helloworld"));
			
			HttpHeaders headers=new HttpHeaders();
			headers.set("Authorization", "Bearer "+token);
		
			return new ResponseEntity<>(u,headers,HttpStatus.OK);
		
	   }catch(Exception e) {
		   return new ResponseEntity<String>("false",HttpStatus.NOT_FOUND); 
	   }
	
}
	

}
