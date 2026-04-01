package com.wilson.ead.authuser.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.wilson.ead.authuser.enums.UserStatus;
import com.wilson.ead.authuser.enums.UserType;
import com.wilson.ead.authuser.models.UserModel;
import com.wilson.ead.authuser.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - UserServiceImpl")
public class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImpl userService;

	private UserModel userModel;
	private UUID userId;

	// Método auxiliar para criar um UserModel de teste
	private UserModel criarUsuario() {
		var user = new UserModel();

		user.setUserId(UUID.randomUUID());
		user.setUsername("wilsonbraga");
		user.setEmail("wilson@email.com");
		user.setPassword("senha123");
		user.setFullName("Wilson Rodrigues");
		user.setUserStatus(UserStatus.ACTIVE);
		user.setUserType(UserType.STUDENT);
		user.setPhoneNumber("11999999999");
		user.setCpf("12345678901");
		user.setCreationDate(LocalDateTime.now());
		user.setLastUpdateDate(LocalDateTime.now());

		return user;
	}

	@BeforeEach
	void setUp() {
		userModel = criarUsuario();
		userId = userModel.getUserId();
	}

	// findAll

	@Test
	@DisplayName("Deve retornar lista de usuários com sucesso")
	void findAll_deveRetornarListaDeUsuario() {

		when(userRepository.findAll()).thenReturn(List.of(userModel));

		var result = userService.findAll();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("wilsonbraga", result.get(0).getUsername());
		verify(userRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("Deve retornar lista vazia quando não houver usuários")
	void findAll_deveRetornarListaVazia() {
		when(userRepository.findAll()).thenReturn(List.of());

		var result = userService.findAll();
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	// findById
	@Test
	@DisplayName("Deve retornar usuário quando ID existir")
	void findById_deveRetornarUsuarioQuandoIdExistir() {
		when(userRepository.findById(userId)).thenReturn(Optional.of(userModel));

		var result = userService.findById(userId);

		assertTrue(result.isPresent());
		assertEquals(userId, result.get().getUserId());
		assertEquals("wilsonbraga", result.get().getUsername());

	}

	@Test
	@DisplayName("Deve retornar Optional vazio quando ID não existir")
	void findById_deveRetornarVazioQuandoIdNaoExistir() {
		when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

		var result = userService.findById(UUID.randomUUID());

		assertFalse(result.isPresent());
	}

	// save
	@Test
	@DisplayName("Deve salvar usuário com sucesso")
	void save_deveSalvarUsuarioComSucesso() {
		userService.save(userModel);
		verify(userRepository, times(1)).save(userModel);
	}

	// delete
	@Test
	@DisplayName("Deve deletar usuário com sucesso")
	void delete_deveDeletarUsuarioComSucesso() {
		userService.delete(userModel);

		verify(userRepository, times(1)).delete(userModel);
	}

	// existsByUserName
	@Test
	@DisplayName("Deve retornar true quando username já existir")
	void existsByUserName_deveRetornarTrueQuandoUsernameExistir() {
		when(userRepository.existsByUsername("wilsonbraga")).thenReturn(true);

		assertTrue(userService.existsByUserName("wilsonbraga"));
	}

	@Test
	@DisplayName("Deve retornar false quando username não existir")
	void existsByUserName_deveRetornarFalseQuandoUsernameNaoExistir() {
		when(userRepository.existsByUsername("usuarionovo")).thenReturn(false);

		assertFalse(userService.existsByUserName("usuarionovo"));
	}

	// existsByEmail
	@Test
	@DisplayName("Deve retornar true quando email já existir")
	void existsByEmail_deveRetornarTrueQuandoEmailExistir() {
		when(userRepository.existsByEmail("wilson@email.com")).thenReturn(true);

		assertTrue(userService.existsByEmail("wilson@email.com"));
	}

	@Test
	@DisplayName("Deve retornar false quando email não existir")
	void existsByEmail_deveRetornarFalseQuandoEmailNaoExistir() {
		when(userRepository.existsByEmail("novo@email.com")).thenReturn(false);

		assertFalse(userService.existsByEmail("novo@email.com"));
	}

	// existsByCpf
	@Test
	@DisplayName("Deve retornar true quando CPF já existir")
	void existsByCpf_deveRetornarTrueQuandoCpfExistir() {
		when(userRepository.existsByCpf("12345678901")).thenReturn(true);

		assertTrue(userService.existsByCpf("12345678901"));
	}

	// existsPhoneNumber
	@Test
	@DisplayName("Deve retornar true quando telefone já existir")
	void existsPhoneNumber_deveRetornarTrueQuandoTelefoneExistir() {
		when(userRepository.existsByPhoneNumber("11999999999")).thenReturn(true);

		assertTrue(userService.existsPhoneNumber("11999999999"));
	}

	//findAll com Specification
	@Test
    @DisplayName("Deve retornar página de usuários com specification e pageable")
    void findAllSpec_deveRetornarPaginaDeUsuarios() {
        var pageable = PageRequest.of(0, 10);
        Page<UserModel> page = new PageImpl<>(List.of(userModel));
 
        when(userRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
 
        var result = userService.findAll(mock(Specification.class), pageable);
 
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}
