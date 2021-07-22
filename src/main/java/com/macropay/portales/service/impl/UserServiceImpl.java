package com.macropay.portales.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.macropay.portales.domain.User;
import com.macropay.portales.domain.UserPrincipal;
import com.macropay.portales.enumeration.Role;
import com.macropay.portales.exception.EmailExistException;
import com.macropay.portales.exception.UserNotFoundException;
import com.macropay.portales.exception.UsernameExistsException;
import com.macropay.portales.repository.IUserRepository;
import com.macropay.portales.service.IUserService;

import static com.macropay.portales.constant.UserImplConstant.*;
import static com.macropay.portales.constant.FileConstant.*;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements IUserService, UserDetailsService{

	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	private IUserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UserServiceImpl(IUserRepository userRepository,BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		 User user = userRepository.findUserByUsername(username);
		 
		 if(user == null) {
			  LOGGER.error(NO_USER_FOUND_BY_USERNAME + username);
	            throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
		 }else {
		     
			 // TODO:  En esta sección se realiza el guardado de los campos referentes a cuando se logueo etc.
			 user.setGetLastLoginDateDisplay(user.getLastLoginDate());
			 user.setLastLoginDate(new Date());
			 userRepository.save(user);
			 UserPrincipal userPrincipal = new UserPrincipal(user);
			 LOGGER.info(FOUND_USER_BY_USERNAME +  username);
			 return userPrincipal; 
		 }
	}

	@Override
	public User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UsernameExistsException, EmailExistException{ 
		validateNewUsernameAndEmail(StringUtils.EMPTY, username, email);
		
		User user = new User();
		user.setUserId(generateUserId());
		String password = generatePassword();
		user.setFirstName(firstName);
	    user.setLastName(lastName);
	    user.setUsername(username);
	    user.setEmail(email);
	    user.setJoinDate(new Date());
	    user.setPassword(encodePassword(password));
        user.setActive(true);
        user.setNotLocked(true);
		user.setRole(Role.ROLE_USER.name()); 
		user.setAuthorities(Role.ROLE_USER.getAuthorities());
		user.setProfileImageUrl(getTemporaryProfileImageUrl(username));
	    userRepository.save(user);
	    LOGGER.info("New user password: " + password);
		
	    return user;
	}
	
	

	private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistsException, EmailExistException{
		
		  User userByNewUsername = findUserByUsername(newUsername);
	        User userByNewEmail = findUserByEmail(newEmail);
		 
	   
	  if(StringUtils.isNotBlank(currentUsername)) { 
		 User currentUser = findUserByUsername(currentUsername);
	     if(currentUser == null) {
	    	 throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
	     }
	     
	 
	     if(userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
	    	 throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
	     }
	      
	     if(userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
	    	 throw new EmailExistException(EMAIL_ALREADY_EXISTS);
	     }
	     
	     return currentUser;
	  } else {
		  
	      if(userByNewUsername != null) {
		    	 throw new UsernameExistsException(USERNAME_ALREADY_EXISTS);
		  }
		  
		 
		  if(userByNewEmail != null) {
		    	 throw new EmailExistException(EMAIL_ALREADY_EXISTS);
		  }
		  
		  return null;
	  }

	  
	  
	}
	
	

	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public User findUserByUsername(String username) {
		// TODO Auto-generated method stub
		return userRepository.findUserByUsername(username);
	}

	@Override
	public User findUserByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findUserByEmail(email);
	}
	
	
	/*
	 * Métodos genericos
	*/ 

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }
    
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String getTemporaryProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_IMAGE_PATH).toUriString();
    }
    
    
	
	
}
