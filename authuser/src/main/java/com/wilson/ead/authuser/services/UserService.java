package com.wilson.ead.authuser.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.wilson.ead.authuser.dto.UserDto;
import com.wilson.ead.authuser.models.UserModel;

public interface UserService {

	List<UserModel> findAll();

	Optional<UserModel> findById(UUID userId);

	void delete(UserModel userModel);

	void save(UserModel userModel);

	boolean existsByUserName(String username);

	boolean existsByEmail(String email);

	boolean existsByCpf(String cpf);

	boolean existsPhoneNumber(String phoneNumber);
	
	Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable);

}
