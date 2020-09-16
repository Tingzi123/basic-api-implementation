package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void should_register_user() throws Exception {
        UserDto userDto=new UserDto("chenting","woman",18,"ting@163.com","12345678910");
        ObjectMapper objectMapper=new ObjectMapper();
        String userDtoJson=objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/user/register")
                .content(userDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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
}
