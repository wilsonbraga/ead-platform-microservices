package com.wilson.ead.authuser.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.wilson.ead.authuser.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID>, JpaSpecificationExecutor<UserModel>{
	
	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	boolean existsByCpf(String cpf);

	boolean existsByPhoneNumber(String phoneNumber);
}
