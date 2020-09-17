package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Test
    void should_register_user_to_database() throws Exception {
        UserDto userDto=new UserDto("chen","woman",18,"ting@163.com","18588888888");
        ObjectMapper objectMapper=new ObjectMapper();
        String userDtoJson=objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<UserEntity> users=userRepository.findAll();
        assertEquals(2,users.size());
        assertEquals("chen",users.get(0).getName());
    }

    @Test
    void should_get_one_user_by_id() throws Exception {
        UserDto userDto=new UserDto("chen","woman",18,"ting@163.com","18588888888");
        UserEntity userEntity=UserEntity.builder()
                .name(userDto.getName())
                .gender(userDto.getGender())
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .vote(userDto.getVote())
                .build();

        userRepository.save(userEntity);

        mockMvc.perform(get("/user/get/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("chen")));
    }

    @Test
    void should_delete_user_by_id() throws Exception {
        UserDto userDto=new UserDto("chen","woman",18,"ting@163.com","18588888888");
        UserEntity userEntity=UserEntity.builder()
                .name(userDto.getName())
                .gender(userDto.getGender())
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .vote(userDto.getVote())
                .build();

        userRepository.save(userEntity);

        userDto=new UserDto("ting","woman",18,"ting@163.com","18588888888");
        userEntity=UserEntity.builder()
                .name(userDto.getName())
                .gender(userDto.getGender())
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .vote(userDto.getVote())
                .build();
        userRepository.save(userEntity);


        mockMvc.perform(delete("/user/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1,userRepository.findAll().size());
    }

    @Test
    void should_register_user() throws Exception {
        UserDto userDto=new UserDto("chen","woman",18,"ting@163.com","18588888888");
        ObjectMapper objectMapper=new ObjectMapper();
        String userDtoJson=objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void name_should_not_empty_or_null() throws Exception {
        UserDto userDto=new UserDto(null,"woman",18,"ting@163.com","12345678910");
        ObjectMapper objectMapper=new ObjectMapper();
        String userDtoJson=objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void name_length_should_no_more_than_8() throws Exception {
        UserDto userDto=new UserDto("chenchenchen","woman",18,"ting@163.com","12345678910");
        ObjectMapper objectMapper=new ObjectMapper();
        String userDtoJson=objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_no_register_when_gender_is_empty() throws Exception {
        UserDto userDto=new UserDto("chenting","",18,"ting@163.com","12345678910");
        ObjectMapper objectMapper=new ObjectMapper();
        String userDtoJson=objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_no_register_when_age_is_null() throws Exception {
        UserDto userDto=new UserDto("chenting","1",null,"ting@163.com","12345678910");
        ObjectMapper objectMapper=new ObjectMapper();
        String userDtoJson=objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_no_register_when_age_is_less_than_18() throws Exception {
        UserDto userDto=new UserDto("chenting","1",17,"ting@163.com","12345678910");
        ObjectMapper objectMapper=new ObjectMapper();
        String userDtoJson=objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_no_register_when_age_is_more_than_100() throws Exception {
        UserDto userDto=new UserDto("chenting","1",101,"ting@163.com","12345678910");
        ObjectMapper objectMapper=new ObjectMapper();
        String userDtoJson=objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_no_register_when_email_is_empty() throws Exception {
        UserDto userDto=new UserDto("chenting","1",18,"","12345678910");
        ObjectMapper objectMapper=new ObjectMapper();
        String userDtoJson=objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void email_should_valid() throws Exception {
        UserDto userDto=new UserDto("chenting","1",18,"26666666","12345678910");
        ObjectMapper objectMapper=new ObjectMapper();
        String userDtoJson=objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void phone_should_not_empty() throws Exception {
        UserDto userDto=new UserDto("chenting","1",18,"ting@163.com","");
        ObjectMapper objectMapper=new ObjectMapper();
        String userDtoJson=objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void phone_should_start_with_1() throws Exception {
        UserDto userDto=new UserDto("chenting","1",18,"ting@163.com","66666666666");
        ObjectMapper objectMapper=new ObjectMapper();
        String userDtoJson=objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void phone_length_should_is_11() throws Exception {
        UserDto userDto=new UserDto("chenting","1",18,"ting@163.com","16666666");
        ObjectMapper objectMapper=new ObjectMapper();
        String userDtoJson=objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_all_users() throws Exception {
        mockMvc.perform(get("/user/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name",is("lily")))
                .andExpect(jsonPath("$[1].name",is("lisa")))
                .andExpect(jsonPath("$[2].name",is("joy")));
    }
}
