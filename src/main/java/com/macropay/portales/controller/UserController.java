package com.macropay.portales.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.macropay.portales.constant.SecurityConstant;
import com.macropay.portales.domain.User;
import com.macropay.portales.domain.UserPrincipal;
import com.macropay.portales.exception.EmailExistException;
import com.macropay.portales.exception.ExceptionHandling;
import com.macropay.portales.exception.UserNotFoundException;
import com.macropay.portales.exception.UsernameExistsException;
import com.macropay.portales.service.IUserService;
import com.macropay.portales.utility.JWTTokenProvider;

@RestController
@RequestMapping(path = {"/user"})
public class UserController extends ExceptionHandling{
	
	
	private IUserService userService;
	private AuthenticationManager authenticationManager;
	private JWTTokenProvider jwtTokenProvider;
	
	public UserController(IUserService userService,AuthenticationManager authenticationManager,JWTTokenProvider jwtTokenProvider) {
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider =jwtTokenProvider; 
	}
	
	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User user) {
		
		authenticate(user.getUsername(), user.getPassword());
		
		User loginUser = userService.findUserByUsername(user.getUsername());
		UserPrincipal userPrincipal = new UserPrincipal(loginUser);
	 	HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
		
		return new ResponseEntity<>(loginUser,jwtHeader, HttpStatus.OK);
	}
		
	
	// Controller encargado de llevar a cabo el registro de la aplicación
	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, UsernameExistsException, EmailExistException {
		
		User newUser  = userService.register(user.getFirsName(), user.getLastName(), user.getUsername(), user.getEmail());
		
	    return new ResponseEntity<>(newUser, HttpStatus.OK);	
	}
	
	// Método que nos permite generar el JWT
	private HttpHeaders getJwtHeader(UserPrincipal user) {
			// TODO Auto-generated method stub
			HttpHeaders headers = new HttpHeaders();
			headers.add(SecurityConstant.JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
					
			return headers;
	}
	    
		
		// Método encargado de la autenticación por parte del usuario
	private void authenticate(String username, String password) {
			// TODO Auto-generated method stub
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	}
	
	
	
	
	
	
	
	
	

}
