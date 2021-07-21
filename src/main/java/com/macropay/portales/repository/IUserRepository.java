package com.macropay.portales.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.macropay.portales.domain.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
	
	// Find username
	User findUserByUsername(String username);
	
	// Find Email
	User findUserByEmail(String email);
	
	
	 
	

}
