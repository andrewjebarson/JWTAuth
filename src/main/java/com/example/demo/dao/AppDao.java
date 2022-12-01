package com.example.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Repository
public class AppDao {
	
	@Autowired
	UserRepository userrepo;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
//	public User modifyUser(User user) {
//		User u=userrepo.findById(user.getUserid()).get();
//		u.setUsername(user.getUsername());
//		u.setPassword(user.getPassword());
//		u.setMobileNo(user.getMobileNo());
//		u.setEmail(user.getEmail());
//		u.setRoles(user.getRoles());
//		return userrepo.save(u);
//		}
//	
	
	public User modifypassword(User user) {
		User u=userrepo.findById(user.getUserid()).get();
		u.setUsername(user.getUsername());
		u.setPassword(passwordEncoder.encode(user.getPassword()));
		u.setMobileNo(user.getMobileNo());
		u.setEmail(user.getEmail());
		return userrepo.save(u);
	}
	
	public User searchUserById(int userId) {
		User u=userrepo.findById(userId).get();
		return u;
	}

}
