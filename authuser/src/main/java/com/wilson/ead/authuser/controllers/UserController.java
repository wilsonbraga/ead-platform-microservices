package com.wilson.ead.authuser.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.wilson.ead.authuser.dto.UserDto;
import com.wilson.ead.authuser.models.UserModel;
import com.wilson.ead.authuser.services.UserService;
import com.wilson.ead.authuser.specifications.SpecificationTemplate;;



@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping
	public ResponseEntity<Page<UserModel>> getAllUsers(SpecificationTemplate.UserSpec spec,
			@PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) 
			Pageable pageable) {
		
		Page<UserModel> userModelPage = userService.findAll(spec, pageable);
		
		if(!userModelPage.isEmpty()) {
			for(UserModel user : userModelPage.toList()) {
				user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel()); 
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<Object> getOneUser(@PathVariable UUID userId) {

		Optional<UserModel> userModelOptional = userService.findById(userId);

		if (!userModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("Usuário não encontado."));
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
		}
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<Object> deleteUser(@PathVariable UUID userId) {

		Optional<UserModel> userModelOptional = userService.findById(userId);

		if (!userModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("Usuário não encontado."));
		} else {
			userService.delete(userModelOptional.get());
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(("Usuário deletado com sucesso"));
		}
	}

	@PutMapping("/update/{userId}")
	public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") UUID userId,
			@RequestBody @Validated(UserDto.UserView.UserPut.class) @JsonView(UserDto.UserView.UserPut.class) UserDto userDto) {

		Optional<UserModel> userModelOptional = userService.findById(userId);

		if (!userModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("Usuário não encontado."));
		} else {

			var userModel = userModelOptional.get();

			if (userDto.getFullName() != null) {
				userModel.setFullName(userDto.getFullName());
			}

			if (userDto.getPhoneNumber() != null) {
				userModel.setPhoneNumber(userDto.getPhoneNumber());
			}

			if (userDto.getCpf() != null) {
				// TODO:OBS: SE O USUÁRIO EDITAR O CPF DEPOIS DE SALVO. NAO É UMA BOA PRATICA.
				// ELE PODE EDITAR PARA UM CPF JÁ CADASTRADO
				userModel.setCpf(userDto.getCpf());
			}

			userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
			userService.save(userModel);

			return ResponseEntity.status(HttpStatus.OK).body(userModel);
		}
	}

	@PutMapping("/{userId}/password")
	public ResponseEntity<Object> updatePassword(@PathVariable UUID userId,
			@RequestBody @Validated(UserDto.UserView.PasswordPut.class) @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto) {

		Optional<UserModel> userModelOptional = userService.findById(userId);

		if (!userModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("Usuário não encontado."));

		}
		if (!userModelOptional.get().getPassword().equals(userDto.getOldPassword())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(("Não foi possível alterar a senha. Verifique se a senha atual está correta."));

		} else {

			var userModel = userModelOptional.get();
			userModel.setPassword(userDto.getPassword());
			userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
			userService.save(userModel);
			return ResponseEntity.status(HttpStatus.OK).body("Senha atualizada com sucesso.");
		}
	}

	@PutMapping("/{userId}/image")
	public ResponseEntity<Object> updateImage(@PathVariable UUID userId,
			@Valid @RequestBody @JsonView(UserDto.UserView.ImagPut.class) UserDto userDto) {

		Optional<UserModel> userModelOptional = userService.findById(userId);

		if (!userModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("Usuário não encontado."));

		} else {
			var userModel = userModelOptional.get();
			userModel.setImageUrl(userDto.getImageUrl());
			userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
			userService.save(userModel);
			return ResponseEntity.status(HttpStatus.OK).body(userModel);
		}
	}

}
