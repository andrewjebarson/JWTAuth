package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;


@SpringBootTest

class TokenGenerationApplicationTests {

	@Autowired
	UserRepository userrepo;

//	@Test
public void testUpdateUser() {
	
		User u=userrepo.findById(1).get();
		u.setUsername("priya");
		userrepo.save(u);
		assertNotEquals("test",userrepo.findById(1).get().getUsername());
		}
}
