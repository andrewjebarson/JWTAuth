package com.example.demo;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import lombok.Data;

@Entity
@Data
public class User {
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
	private int userid;
	   @Column(unique=true)
		private String username;
		private String password;
		private String mobileNo;
		@Column(unique=true)
		private String email;
		private String roles;
		private boolean accountNonExpired;
		private boolean accountNonLocked;
		private boolean credentialsNonExpired;
		private boolean enabled;
		@ElementCollection(fetch=FetchType.EAGER)
		@CollectionTable(name="authority",joinColumns=@JoinColumn(name="username"))
	    private Collection<String> authorities=new ArrayList<String>();
	
}
