package com.github.cs_24_sw_3_09.CMS.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cs_24_sw_3_09.CMS.TestDataUtil;
import com.github.cs_24_sw_3_09.CMS.model.dto.DisplayDeviceDto;
import com.github.cs_24_sw_3_09.CMS.model.dto.UserDto;
import com.github.cs_24_sw_3_09.CMS.model.entities.DisplayDeviceEntity;
import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;
import com.github.cs_24_sw_3_09.CMS.services.DisplayDeviceService;
import com.github.cs_24_sw_3_09.CMS.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {
    /*
    Get one
    Get all DONE
    pussy one
        work
        not work
    post one DONE
        Work DONE
    patch one
        work
        not work
    delete one
        Not work
        work
     */

    //Mock is a powerful way of testing controllers.
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
    public void testThatCreateUserSuccessfullyReturnsHttp201Created() throws Exception {
        UserEntity userEntity = TestDataUtil.createUserEntity();
        String userJson = objectMapper.writeValueAsString(userEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }
/*
    @Test
    public void testThatCreateDisplayDeviceSuccessfullyReturnsSavedDisplayDevice() throws Exception {
        DisplayDeviceEntity displayDeviceEntity = TestDataUtil.createDisplayDeviceEntity();
        String displayDeviceJson = objectMapper.writeValueAsString(displayDeviceEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/display_devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(displayDeviceJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Skærm Esbjerg")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.location").value("Esbjerg")
        );
    }
    */

    @Test
    public void testThatGetUserSuccessfullyReturnsHttp200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users")
        ).andExpect(
                MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetUserSuccessfullyReturnsListOfUsers() throws Exception {
        UserEntity testUserEntity = TestDataUtil.createUserEntity();
        userService.save(testUserEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[0].firstName").value(testUserEntity.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("content.[0].email").value(testUserEntity.getEmail())
        ).andExpect(
                MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetUserReturnsStatus200WhenUserExists() throws Exception {
        UserEntity userEntity = TestDataUtil.createUserEntity();
        userService.save(userEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/1")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("firstName").value(userEntity.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("email").value(userEntity.getEmail())
        );
    }

    @Test
    public void testThatGetUserReturnsStatus404WhenNoUserExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/100000")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatDeleteDisplayDevice() throws Exception {
        UserEntity userEntity = TestDataUtil.createUserEntity();
        UserEntity savedUserEntity = userService.save(userEntity);


        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/" + savedUserEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/users/" + savedUserEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent());
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/" + savedUserEntity.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void testThatDeleteUserReturnsStatus404() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/users/99")
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
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
    public void testThatFullUpdateUserReturnsStatus200WhenUserExists() throws Exception {
        UserEntity userEntity = TestDataUtil.createUserEntity();
        UserEntity savedUserEntity = userService.save(userEntity);

        // Have to be hardcoded as a Json string, do to the password only being write and not read
        String userDtoJson = "{\"firstName\": \"FirstTestName\", \"lastName\":\"LastTestName\", \"email\":\"test@test.com\", \"password\":\"testtest1234\"}";

        mockMvc.perform(
                MockMvcRequestBuilders.put("/api/users/" + savedUserEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPatchUpdateUserReturnsStatus200() throws Exception {
        UserEntity userEntity = TestDataUtil.createUserEntity();
        UserEntity savedUserEntity = userService.save(userEntity);

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
                MockMvcResultMatchers.jsonPath("$.firstName").value(userEntity.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(userEntity.getEmail())
        );
    }

    @Test
    public void testThatPatchUpdateUserReturnsStatus404() throws Exception {
        UserDto userDto = TestDataUtil.createUserDto();
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

}
