package com.macropay.portales.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.macropay.portales.exception.EmailExistException;
import com.macropay.portales.exception.ExceptionHandling;

@RestController
@RequestMapping(value = "/user")
public class UserController extends ExceptionHandling{
	
	
	// Controller
	@GetMapping("/home")
	public String showUser() throws EmailExistException {
		throw new EmailExistException("Email no existe :o!!!!!");
	}
	
	
	
	
	
	
	
	
	

}
