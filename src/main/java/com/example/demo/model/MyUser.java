package com.example.demo.model;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class MyUser implements UserDetails {
	private int userid;
	private String username;
	private String password;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;
	private String mobileNo;
     private String email;
	 private String roles;
	 private Collection<GrantedAuthority> authorities;
	
	  public MyUser() {
	    	
	    }
	    
	    public MyUser(User user) {
	    	this.username=user.getUsername();
	    	this.password=user.getPassword();
	    	this.userid=user.getUserid();
	    	
	    	this.accountNonExpired=user.isAccountNonExpired();
	    	this.accountNonLocked=user.isAccountNonLocked();
	    	this.enabled=user.isEnabled();
	    	this.credentialsNonExpired=user.isCredentialsNonExpired();
	    	this.mobileNo=user.getMobileNo();
	    	this.email=user.getEmail();
	    	this.roles=user.getRoles();
	    	
	    	
	 
	    	
	    	
	    	
	    	
	    	this.authorities=user.getAuthorities().stream().map(new Function<String,GrantedAuthority>(){

				@Override
				public GrantedAuthority apply(String t) {
					// TODO Auto-generated method stub
					return new GrantedAuthority() {

						@Override
						public String getAuthority() {
							// TODO Auto-generated method stub
							return t;
						}
						
					};
				}
	    		
	    	}).collect(Collectors.toList());
	    }
	    
	
	
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.enabled;
	}
	
	

}
