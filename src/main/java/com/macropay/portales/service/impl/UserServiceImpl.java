package com.macropay.portales.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.macropay.portales.domain.User;
import com.macropay.portales.domain.UserPrincipal;
import com.macropay.portales.repository.IUserRepository;
import com.macropay.portales.service.IUserService;



@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements IUserService, UserDetailsService{

	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	private IUserRepository userRepository;
	
	@Autowired
	public UserServiceImpl(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		 User user = userRepository.findUserByUsername(username);
		 
		 if(user == null) {
			 LOGGER.error("User not found by Username" + username);
			 throw new UsernameNotFoundException("User not found by username" + username);
		 }else {
		     
			 // TODO:  En esta sección se realiza el guardado de los campos referentes a cuando se logueo etc.
			 user.setGetLastLoginDateDisplay(user.getLastLoginDate());
			 user.setLastLoginDate(new Date());
			 userRepository.save(user);
			 UserPrincipal userPrincipal = new UserPrincipal(user);
			 LOGGER.info("Returning found user by username" +  username);
			 return userPrincipal; 
		 }
	}
}
