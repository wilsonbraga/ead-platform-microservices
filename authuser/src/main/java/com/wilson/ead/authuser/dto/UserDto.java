package com.wilson.ead.authuser.dto;

import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.wilson.ead.authuser.validation.UsernameConstraint;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
	
	public interface UserView {
		public interface RegistrationPost {}
		public interface UserPut {}
		public interface PasswordPut{}
		public interface ImagPut {}
	}


	private UUID userId;
	
	@JsonView(UserView.RegistrationPost.class)
	@NotBlank(groups = UserView.RegistrationPost.class)
	@Size(min = 3, max = 50, message = "Nome deve ter entre 3 e 50 caracteres", groups = UserView.RegistrationPost.class)
	@UsernameConstraint(groups = UserView.RegistrationPost.class)
	private String username;
	
	@JsonView(UserView.RegistrationPost.class)
    @NotBlank(groups = UserView.RegistrationPost.class)
    @Email(message = "Email deve ser válido", groups = UserView.RegistrationPost.class)
	private String email;
	
	@JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
	@NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
	//@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$", 
    //message = "Senha deve conter letra maiúscula, minúscula, número e caractere especial", 
    //groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
	@Size(min = 6, max = 50, message = "A senha deve ter entre 6 e 50 caracteres",  groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
	private String password;
	
	@JsonView({UserView.PasswordPut.class})
    @NotBlank(groups = UserView.PasswordPut.class)
	@Size(max = 255, message = "Senha antiga deve ter no máximo 60 caracteres")
	private String oldPassword;
	
	@JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
	@NotBlank(groups = UserView.PasswordPut.class)
	@Size(min = 3, max = 150, message = "Nome completo deve ter entre 2 a 80 caracteres", groups = {UserView.RegistrationPost.class, UserView.UserPut.class})
	private String fullName;
	
	@JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
	@NotBlank(groups = {UserView.RegistrationPost.class, UserView.UserPut.class})
    @Size(max = 11, message = "Phone number deve ter no máximo 11 caracteres", groups = {UserView.RegistrationPost.class, UserView.UserPut.class})
	private String phoneNumber;
	
	@Size(max = 14, message = "CPF deve ter no máximo 14 caracteres", groups = {UserView.RegistrationPost.class, UserView.UserPut.class})
    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
	private String cpf;
	
	@JsonView({UserView.ImagPut.class})
	@NotBlank(groups = {UserView.ImagPut.class})
	@Size(max = 255, message = "Image URL deve ter no máximo 255 caracteres")
	private String imageUrl;

}
