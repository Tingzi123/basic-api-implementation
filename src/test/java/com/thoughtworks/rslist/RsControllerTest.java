package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//每次测试重启一个tomcat
class RsControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    VoteRepository voteRepository;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
    }

    @Test
    void should_get_rs_list() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无分类")));
    }

    @Test
    void should_get_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyword", is("无分类")));

        mockMvc.perform(get("/rs/list/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyword", is("无分类")));

        mockMvc.perform(get("/rs/list/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyword", is("无分类")));
    }

    @Test
    void should_get_rs_event_by_range() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("无分类")));
    }


    @Test
    void should_add_a_rs_event_no_user() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("index", "4"));

        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", not(hasKey("user"))));
    }

    @Test
    void should_change_keyword_with_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        RsEvent rsEvent = new RsEvent("第三条事件", "无分类");
        rsEvent.setKeyword("生活");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(put("/rs/event/change/2").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("index", "4"));

        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyword", is("生活")));
    }

    @Test
    void should_change_event_name_with_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        RsEvent rsEvent = new RsEvent("第三条事件", "无分类");
        rsEvent.setEventName("今天");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(put("/rs/event/change/2").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("今天")))
                .andExpect(jsonPath("$[2].keyword", is("无分类")));
    }

    @Test
    void should_change_event_name_and_keyword_with_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        RsEvent rsEvent = new RsEvent("第三条事件", "无分类");
        rsEvent.setEventName("今天");
        rsEvent.setKeyword("生活");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(put("/rs/event/change/2").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("今天")))
                .andExpect(jsonPath("$[2].keyword", is("生活")));
    }

    @Test
    void should_delete_a_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));


        mockMvc.perform(delete("/rs/event/delete/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyword", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyword", is("无分类")));
    }


    //jpa
    @Test
    void should_add_a_rs_event_exist_user() throws Exception {
        UserDto userDto = new UserDto("chen", "woman", 18, "ting@163.com", "18588888888");
        UserEntity userEntity = UserEntity.builder()
                .name(userDto.getName())
                .gender(userDto.getGender())
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .vote(userDto.getVote())
                .build();

        userRepository.save(userEntity);

        int id=userEntity.getId();
        RsEvent rsEvent = new RsEvent("zhouri","yuy",id);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RsEventEntity> rsEventEntities = rsEventRepository.findAll();
        assertEquals(1,rsEventEntities.size());
        assertEquals("zhouri",rsEventEntities.get(0).getEventName());
    }

    @Test
    void should_not_add_a_rs_event_not_exist_user() throws Exception {
        RsEvent rsEvent = new RsEvent();
        rsEvent.setEventName("lily");
        rsEvent.setKeyword("ins");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(post("/rs/event").content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_change_keyword_with_rs_event_to_databse() throws Exception {
        UserDto userDto = new UserDto("chen", "woman", 18, "ting@163.com", "18588888888");
        UserEntity userEntity = UserEntity.builder()
                .name(userDto.getName())
                .gender(userDto.getGender())
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .vote(userDto.getVote())
                .build();

        userRepository.save(userEntity);

        RsEvent rsEvent = new RsEvent("lily", "ins",1);

        RsEventEntity rsEventEntity=RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .userId(rsEvent.getUserId())
                .build();
        rsEventRepository.save(rsEventEntity);

        rsEvent.setKeyword("tiktok");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(put("/rs/event/change?userId=1&index=1").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
//                .andExpect(header().string("index", "1"));

       assertEquals(1,rsEventRepository.findAll().size());
       assertEquals("tiktok",rsEventRepository.findAll().get(0).getKeyword());
    }

    @Test
    void should_not_change_when_user_id_not_equals() throws Exception {
        UserDto userDto = new UserDto("chen", "woman", 18, "ting@163.com", "18588888888");
        UserEntity userEntity = UserEntity.builder()
                .name(userDto.getName())
                .gender(userDto.getGender())
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .vote(userDto.getVote())
                .build();

        userRepository.save(userEntity);

        RsEvent rsEvent = new RsEvent("lily", "ins",1);

        RsEventEntity rsEventEntity=RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .userId(rsEvent.getUserId())
                .build();
        rsEventRepository.save(rsEventEntity);

        rsEvent.setKeyword("tiktok");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);

        mockMvc.perform(put("/rs/event/change?userId=6&index=1").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertEquals(1,rsEventRepository.findAll().size());
        assertEquals("ins",rsEventRepository.findAll().get(0).getKeyword());
    }

    @Test
    void should_vote_when_more_than_vote_nums() throws Exception {
        UserDto userDto = new UserDto("chen", "woman", 18, "ting@163.com", "18588888888");
        UserEntity userEntity = UserEntity.builder()
                .name(userDto.getName())
                .gender(userDto.getGender())
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .vote(userDto.getVote())
                .build();

        userRepository.save(userEntity);

        RsEvent rsEvent = new RsEvent("lily", "ins",1);

        RsEventEntity rsEventEntity=RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .userId(rsEvent.getUserId())
                .build();
        rsEventRepository.save(rsEventEntity);

        VoteDto voteDto =new VoteDto(2,1,LocalDateTime.now());
        VoteEntity voteEntity=VoteEntity.builder()
                .voteNum(voteDto.getVoteNum())
                .userId(voteDto.getUserId())
                .voteTime(voteDto.getVoteTime())
                .build();


        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(voteEntity);

        mockMvc.perform(post("/rs/vote/1")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(1,rsEventRepository.findAll().size());
        assertEquals("ins",rsEventRepository.findAll().get(0).getKeyword());
        assertEquals(1,voteRepository.findAll().size());
    }

    @Test
    void should_vote_when_less_than_vote_nums() throws Exception {
        UserDto userDto = new UserDto("chen", "woman", 18, "ting@163.com", "18588888888");
        UserEntity userEntity = UserEntity.builder()
                .name(userDto.getName())
                .gender(userDto.getGender())
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .vote(userDto.getVote())
                .build();

        userRepository.save(userEntity);

        RsEvent rsEvent = new RsEvent("lily", "ins",1);

        RsEventEntity rsEventEntity=RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .userId(rsEvent.getUserId())
                .build();
        rsEventRepository.save(rsEventEntity);

        VoteDto voteDto =new VoteDto(5,1,LocalDateTime.now());
        VoteEntity voteEntity=VoteEntity.builder()
                .voteNum(voteDto.getVoteNum())
                .userId(voteDto.getUserId())
                .voteTime(voteDto.getVoteTime())
                .build();


        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(voteEntity);

        mockMvc.perform(post("/rs/vote/1").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertEquals(1,rsEventRepository.findAll().size());
        assertEquals("ins",rsEventRepository.findAll().get(0).getKeyword());
        assertEquals(0,voteRepository.findAll().size());
    }
}
