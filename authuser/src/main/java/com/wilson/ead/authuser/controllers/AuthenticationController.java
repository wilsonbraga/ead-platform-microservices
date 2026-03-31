package com.wilson.ead.authuser.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.wilson.ead.authuser.dto.UserDto;
import com.wilson.ead.authuser.dto.UserDto.UserView;
import com.wilson.ead.authuser.enums.UserStatus;
import com.wilson.ead.authuser.enums.UserType;
import com.wilson.ead.authuser.models.UserModel;
import com.wilson.ead.authuser.services.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/auth")
public class AuthenticationController {

	
	@Autowired
	private UserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<Object> registerUser(@RequestBody
			@Validated(UserDto.UserView.RegistrationPost.class)
			@JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto) {
		
		if(userService.existsByUserName(userDto.getUsername())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Username is Already Taken!");
		}
		
		if(userService.existsByEmail(userDto.getEmail())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Email is Already Taken!");
		}
		
		if(userService.existsByCpf(userDto.getCpf())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: CPF is Already Taken!");
		}
		
		if(userService.existsPhoneNumber(userDto.getPhoneNumber())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Phone Number is Already Taken!");
		}
		
		var userModel = new UserModel();
		
		BeanUtils.copyProperties(userDto, userModel);
		userModel.setUserStatus(UserStatus.ACTIVE);
		userModel.setUserType(UserType.STUDENT);
		userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
		userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
		userService.save(userModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
		
	}
	
	
	
}
