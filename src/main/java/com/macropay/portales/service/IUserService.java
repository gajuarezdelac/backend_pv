package com.macropay.portales.service;

import java.util.List;

import com.macropay.portales.domain.User;
import com.macropay.portales.exception.EmailExistException;
import com.macropay.portales.exception.UserNotFoundException;
import com.macropay.portales.exception.UsernameExistsException;

public interface IUserService {
	
	User register(String firstName, String lastName, String username, String email) throws UserNotFoundException, UsernameExistsException, EmailExistException;
	
	List<User> getUsers();
	
	User findUserByUsername(String username);
	
	User findUserByEmail(String email);
	
	
	

}
