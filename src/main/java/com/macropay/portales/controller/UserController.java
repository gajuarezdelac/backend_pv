package com.macropay.portales.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.macropay.portales.domain.User;
import com.macropay.portales.exception.EmailExistException;
import com.macropay.portales.exception.ExceptionHandling;
import com.macropay.portales.exception.UserNotFoundException;
import com.macropay.portales.exception.UsernameExistsException;
import com.macropay.portales.service.IUserService;

@RestController
@RequestMapping(path = {"/user"})
public class UserController extends ExceptionHandling{
	
	
	private IUserService userService;
	
	public UserController(IUserService userService) {
		this.userService = userService;
	}
	
	
	// Controller
	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, UsernameExistsException, EmailExistException {
		
		User loginUser  = userService.register(user.getFirsName(), user.getLastName(), user.getUsername(), user.getEmail());
		
	    return new ResponseEntity<>(loginUser, HttpStatus.OK);	
	}
	
	
	
	
	
	
	
	
	

}
