package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    public  List<UserDto> userDtos=initUsers();

    private UserRepository userRepository;

    private RsEventRepository rsEventRepository;

    public UserController(UserRepository userRepository, RsEventRepository rsEventRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
    }

    private List<UserDto> initUsers() {
        List<UserDto> tmpUserDtos = new ArrayList<>();
        tmpUserDtos.add(new UserDto("lily","female",20,"lily@163.com","12387898789"));
        tmpUserDtos.add(new UserDto("lisa","female",20,"lily@163.com","12387898789"));
        tmpUserDtos.add(new UserDto("joy","female",20,"lily@163.com","12387898789"));

        return tmpUserDtos;
    }

    @PostMapping("/user/register")
    public ResponseEntity register(@Valid @RequestBody UserDto userDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("index", String.valueOf(userDtos.size()));

        userDtos.add(userDto);

        UserEntity userEntity=UserEntity.builder()
                .name(userDto.getName())
                .gender(userDto.getGender())
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .vote(userDto.getVote())
                .build();

        userRepository.save(userEntity);

        return ResponseEntity.created(null).headers(headers).build();
    }

    @GetMapping("/user/get/{index}")
    public ResponseEntity getUserById(@PathVariable int index) {
        UserEntity user=userRepository.findUserById(index);
        return ResponseEntity.ok(user);
    }


    @GetMapping("/user/users")
    public ResponseEntity getAllUsers() {
        return ResponseEntity.ok(userDtos);
    }

    @DeleteMapping("/user/delete/{id}")
    @Transactional
    public ResponseEntity deleteUserById(@PathVariable int id) {
        userRepository.deleteUserById(id);
//        rsEventRepository.deleteByUserId(id);
        return ResponseEntity.ok(userDtos);
    }

}
