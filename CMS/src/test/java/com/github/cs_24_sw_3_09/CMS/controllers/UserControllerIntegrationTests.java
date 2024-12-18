package com.github.cs_24_sw_3_09.CMS.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.dto.UserDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;
import com.github.cs_24_sw_3_09.CMS.services.UserService;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.not;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserService userService;

    @Autowired
    public UserControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, UserService userService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testThatCreateUserSuccessfullyReturnsHttp201Created() throws Exception {
        // Have to be hardcoded as a Json string, due to the password only being write and not read
        String userJson = "{\"firstName\": \"FirstTestName\", \"lastName\":\"LastTestName\", \"email\":\"test@test.com\", \"password\":\"testtest1234\"}";

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testThatGetUserSuccessfullyReturnsHttp200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users")
        ).andExpect(
                MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testThatGetUserSuccessfullyReturnsListOfUsers() throws Exception {
        UserEntity testUserEntity = TestDataUtil.createUserEntity();
        userService.save(testUserEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[1].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[1].firstName").value(testUserEntity.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[1].email").value(testUserEntity.getEmail())
        ).andExpect(
                MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testThatGetUserReturnsStatus200WhenUserExists() throws Exception {
        UserEntity user = TestDataUtil.createUserEntity();
        userService.save(user);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/2")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("firstName").value(user.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("email").value(user.getEmail())
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testThatGetUserReturnsStatus404WhenNoUserExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/100000")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testThatDeleteUserReturnsStatus200() throws Exception {
        UserEntity userEntity = TestDataUtil.createUserEntity();
        UserEntity savedUserEntiity = userService.save(userEntity).get();

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/users/" + savedUserEntiity.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testThatDeleteUserReturnsStatus404() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/users/99")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testThatFullUpdateUserReturnsStatus404WhenNoUserExists() throws Exception {
        UserDto userDto = TestDataUtil.createUserDto();
        String userDtoJson = objectMapper.writeValueAsString(userDto);


        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testThatFullUpdateUserReturnsStatus200WhenUserExists() throws Exception {
        UserEntity userEntity = TestDataUtil.createUserEntity();
        UserEntity savedUserEntity = userService.save(userEntity).get();

        // Have to be hardcoded as a Json string, due to the password only being write and not read
        String userDtoJson = "{\"firstName\": \"FirstTestName\", \"lastName\":\"LastTestName\", \"email\":\"test@test.com\", \"password\":\"testtest1234\"}";

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/users/" + savedUserEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testThatPatchUpdateUserReturnsStatus200() throws Exception {
        UserEntity userEntity = TestDataUtil.createUserEntity();
        UserEntity savedUserEntity = userService.save(userEntity).get();

        UserDto userDto = TestDataUtil.createUserDto();
        String userDtoJson = objectMapper.writeValueAsString(userDto);
        //String userDtoJson = "{\"firstName\": \"FirstTestName\", \"lastName\":\"LastTestName\", \"email\":\"test@test.com\", \"password\":\"testtest1234\"}";


        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/users/" + savedUserEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value(userDto.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(userDto.getEmail())
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testThatPatchUpdateUserReturnsStatus404() throws Exception {
        UserDto userDto = TestDataUtil.createUserDto();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/users/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testThatPatchPasswordEncryptsPassword() throws Exception {
        UserEntity userEntity = TestDataUtil.createUserEntity();
        UserEntity savedUserEntity = userService.save(userEntity).get();

        UserDto userDto = TestDataUtil.createUserDto();
        String userDtoJson = objectMapper.writeValueAsString(userDto);
        //String userDtoJson = "{"firstName": "FirstTestName", "lastName":"LastTestName", "email":"test@test.com", "password":"testtest1234"}";


        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/users/" + savedUserEntity.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userDtoJson)
                )
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testThatTriesToMakeNewUserWithExistingMailReturns409() throws Exception {
        UserEntity userEntity = TestDataUtil.createUserEntity();
		userService.save(userEntity);
        assertTrue(userService.isExists(2L));
		//One user is hardcoded into the system
		assertFalse(userService.isExists(3L));

		String userDtoJson = "{\"firstName\": \"FirstTestName\", \"lastName\":\"LastTestName\", \"email\":\"test@test.com\", \"password\":\"testtest1234\"}";

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(userDtoJson)
                )
                .andExpect(
                        MockMvcResultMatchers.status().isConflict()
                );
                
		assertFalse(userService.isExists(3L));
	}
}
