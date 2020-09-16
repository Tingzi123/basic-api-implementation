package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private List<UserDto> userDtos=new ArrayList<>();

    @PostMapping("/user/register")
    public void register(@Valid @RequestBody UserDto userDtoStr) {
        /*ObjectMapper objectMapper=new ObjectMapper();
        UserDto userDto=objectMapper.readValue(userDtoStr,UserDto.class);*/
        userDtos.add(userDtoStr);
    }

}
