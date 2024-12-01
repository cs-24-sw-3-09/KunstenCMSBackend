package com.github.cs_24_sw_3_09.CMS.controllers;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.model.dto.AuthLoginDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.AuthResetNewDto;
import com.github.cs_24_sw_3_09.CMS.services.JwtService;
import com.github.cs_24_sw_3_09.CMS.services.UserService;
import com.github.cs_24_sw_3_09.CMS.services.serviceImpl.JwtServiceImpl.TOKEN_TYPE;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class AuthControllerIntegrationTests {
	private MockMvc mockMvc;
	private UserService userService;
	private JwtService jwtService;
	private ObjectMapper objectMapper;

	@Autowired
	public AuthControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, JwtService jwtService, UserService userService) {
		this.mockMvc = mockMvc;
		this.objectMapper = objectMapper;
		this.userService = userService;
		this.jwtService = jwtService;
	}

	@Test
	public void testThatResetPasswordFailsIfUserDoesntExist() throws Exception {

		String email = "notexist@example.com";

		//Valid reset token
		String token = jwtService.generateToken(email, TOKEN_TYPE.RESET_TOKEN);

		AuthResetNewDto resetNewDto = AuthResetNewDto.builder().email(email).password("newpassword").token(token).build();
		String resetNewDtoJson = objectMapper.writeValueAsString(resetNewDto);

		mockMvc.perform(
			MockMvcRequestBuilders.post("/api/account/reset-password/new")
			.contentType(MediaType.APPLICATION_JSON)
			.content(resetNewDtoJson)
		).andExpect(
			MockMvcResultMatchers.status().isBadRequest()
		);
	}

	@Test
	public void testThatResetPasswordIfUserExist() throws Exception {

		String email = "admin@kunsten.dk";

		//Valid reset token
		String token = jwtService.generateToken(email, TOKEN_TYPE.RESET_TOKEN);

		AuthResetNewDto resetNewDto = AuthResetNewDto.builder().email(email).password("newpassword").token(token).build();
		String resetNewDtoJson = objectMapper.writeValueAsString(resetNewDto);

		String passwordBefore = userService.findByEmail(email).get().getPassword();

		mockMvc.perform(
			MockMvcRequestBuilders.post("/api/account/reset-password/new")
			.contentType(MediaType.APPLICATION_JSON)
			.content(resetNewDtoJson)
		).andExpect(
			MockMvcResultMatchers.status().isOk()
		);

		String passwordAfter = userService.findByEmail(email).get().getPassword();
		assertFalse(passwordBefore.equals(passwordAfter));
	}

	@Test
	public void testThatResetPasswordFailsIfTokenIsInvalid() throws Exception {

		String email = "admin@kunsten.dk";

		//Valid reset token for user admin@kunsten.dks
		String token = jwtService.generateToken(email + "s", TOKEN_TYPE.RESET_TOKEN);

		AuthResetNewDto resetNewDto = AuthResetNewDto.builder().email(email).password("newpassword").token(token).build();
		String resetNewDtoJson = objectMapper.writeValueAsString(resetNewDto);

		mockMvc.perform(
			MockMvcRequestBuilders.post("/api/account/reset-password/new")
			.contentType(MediaType.APPLICATION_JSON)
			.content(resetNewDtoJson)
		).andExpect(
			MockMvcResultMatchers.status().isBadRequest()
		);
	}

	@Test
	public void testThatResetPasswordFailsIfPasswordIsInvalid() throws Exception {

		String email = "admin@kunsten.dk";

		//Valid reset token
		String token = jwtService.generateToken(email, TOKEN_TYPE.RESET_TOKEN);

		//Password length is less than min requirement
		AuthResetNewDto resetNewDto = AuthResetNewDto.builder().email(email).password("a").token(token).build();
		String resetNewDtoJson = objectMapper.writeValueAsString(resetNewDto);

		mockMvc.perform(
			MockMvcRequestBuilders.post("/api/account/reset-password/new")
			.contentType(MediaType.APPLICATION_JSON)
			.content(resetNewDtoJson)
		).andExpect(
			MockMvcResultMatchers.status().isBadRequest()
		);
	}
	

	@Test
	public void testThatResetPasswordFailsIfTokenIsAuthType() throws Exception {

		String email = "admin@kunsten.dk";

		//invalid reset token
		String token = jwtService.generateToken(email, TOKEN_TYPE.AUTH_TOKEN);

		//Password length is less than min requirement
		AuthResetNewDto resetNewDto = AuthResetNewDto.builder().email(email).password("newpassword").token(token).build();
		String resetNewDtoJson = objectMapper.writeValueAsString(resetNewDto);

		mockMvc.perform(
			MockMvcRequestBuilders.post("/api/account/reset-password/new")
			.contentType(MediaType.APPLICATION_JSON)
			.content(resetNewDtoJson)
		).andExpect(
			MockMvcResultMatchers.status().isBadRequest()
		);
	}

	@Test
	public void testThatLoginFailsIfWrongEmail() throws Exception {
		AuthLoginDto loginDto = AuthLoginDto.builder().email("notexisting@example.com").password("admin123").build();
		String loginDtoJson = objectMapper.writeValueAsString(loginDto);

		mockMvc.perform(
			MockMvcRequestBuilders.post("/api/account/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(loginDtoJson)
		).andExpect(
			MockMvcResultMatchers.status().isForbidden()
		);
	}

	@Test
	public void testThatLoginFailsIfWrongPassword() throws Exception {
		AuthLoginDto loginDto = AuthLoginDto.builder().email("admin@kunsten.dk").password("wrongpassword").build();
		String loginDtoJson = objectMapper.writeValueAsString(loginDto);

		mockMvc.perform(
			MockMvcRequestBuilders.post("/api/account/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(loginDtoJson)
		).andExpect(
			MockMvcResultMatchers.status().isForbidden()
		);
	}

	@Test
	public void testThatLoginSucceed() throws Exception {
		AuthLoginDto loginDto = AuthLoginDto.builder().email("admin@kunsten.dk").password("admin123").build();
		String loginDtoJson = objectMapper.writeValueAsString(loginDto);

		mockMvc.perform(
			MockMvcRequestBuilders.post("/api/account/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(loginDtoJson)
		).andExpect(
			MockMvcResultMatchers.status().isOk()
		);
	}

	@Test
	public void testThatRequestFailsWithoutTokenType() throws Exception {
		mockMvc.perform(
			MockMvcRequestBuilders.get("/api/display_devices")
		).andExpect(
			MockMvcResultMatchers.status().isForbidden()
		);
	}

	@Test
	public void testThatRequestFailsWithResetTokenType() throws Exception {
		String token = jwtService.generateToken("admin@kunsten.dk", TOKEN_TYPE.RESET_TOKEN);
		mockMvc.perform(
			MockMvcRequestBuilders.get("/api/display_devices")
			.header("Authorization", "Bearer " + token)
		).andExpect(
			MockMvcResultMatchers.status().isForbidden()
		);
	}

	@Test
	public void testThatSucceedWithAuthTokenType() throws Exception {
		String token = jwtService.generateToken("admin@kunsten.dk", TOKEN_TYPE.AUTH_TOKEN);

		mockMvc.perform(
			MockMvcRequestBuilders.get("/api/display_devices")
			.header("Authorization", "Bearer " + token)
		).andExpect(
			MockMvcResultMatchers.status().isOk()
		);
	}
}
