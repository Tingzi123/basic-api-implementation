package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RsEventRepository rsEventRepository;

    @Autowired
    private VoteRepository voteRepository;

    private UserEntity userEntity;
    private RsEventEntity rsEventEntity;

    @BeforeEach
    void setUp() {
        setData();
    }

    @AfterEach
    void tearDown() {
        voteRepository.deleteAll();
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
    }

    private void setData() {
        userEntity = UserEntity.builder()
                .name("user0")
                .gender("male")
                .age(18)
                .email("a1@b1.com")
                .phone("00000000000")
                .build();

        userRepository.save(userEntity);

        rsEventEntity = RsEventEntity.builder()
                .eventName("rs0")
                .keyword("0")
                .user(userEntity)
                .build();

        rsEventRepository.save(rsEventEntity);

        VoteEntity voteEntity = VoteEntity.builder()
                .voteNum(1)
                .voteTime(LocalDateTime.of(2020, Month.SEPTEMBER, 1, 0, 0, 0))
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .build();
        voteRepository.save(voteEntity);

        voteEntity = VoteEntity.builder()
                .voteNum(2)
                .voteTime(LocalDateTime.of(2020, Month.SEPTEMBER, 2, 0, 0, 0))
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .build();
        voteRepository.save(voteEntity);

        voteEntity = VoteEntity.builder()
                .voteNum(3)
                .voteTime(LocalDateTime.of(2020, Month.SEPTEMBER, 3, 0, 0, 0))
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .build();
        voteRepository.save(voteEntity);

        voteEntity = VoteEntity.builder()
                .voteNum(4)
                .voteTime(LocalDateTime.of(2020, Month.SEPTEMBER, 4, 0, 0, 0))
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .build();
        voteRepository.save(voteEntity);

        voteEntity = VoteEntity.builder()
                .voteNum(5)
                .voteTime(LocalDateTime.of(2020, Month.SEPTEMBER, 5, 0, 0, 0))
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .build();
        voteRepository.save(voteEntity);

        voteEntity = VoteEntity.builder()
                .voteNum(6)
                .voteTime(LocalDateTime.of(2020, Month.SEPTEMBER, 6, 0, 0, 0))
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .build();
        voteRepository.save(voteEntity);

        voteEntity = VoteEntity.builder()
                .voteNum(7)
                .voteTime(LocalDateTime.of(2020, Month.SEPTEMBER, 7, 0, 0, 0))
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .build();
        voteRepository.save(voteEntity);

        voteEntity = VoteEntity.builder()
                .voteNum(8)
                .voteTime(LocalDateTime.of(2020, Month.SEPTEMBER, 8, 0, 0, 0))
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .build();
        voteRepository.save(voteEntity);
    }

    @Test
    void should_get_votes_by_user_id_and_rs_event_id() throws Exception {
        mockMvc.perform(get("/votes")
                .param("userId", String.valueOf(userEntity.getId()))
                .param("rsEventId", String.valueOf(rsEventEntity.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].userId", is(userEntity.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventEntity.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(1)));
    }

    @Test
    void should_get_votes_in_page_by_user_id_and_rs_event_id() throws Exception {
        mockMvc.perform(get("/votes")
                .param("userId", String.valueOf(userEntity.getId()))
                .param("rsEventId", String.valueOf(rsEventEntity.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].userId", is(userEntity.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventEntity.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(1)));

        mockMvc.perform(get("/votes")
                .param("userId", String.valueOf(userEntity.getId()))
                .param("rsEventId", String.valueOf(rsEventEntity.getId()))
                .param("pageIndex", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].userId", is(userEntity.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventEntity.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(6)));
    }

    @Test
    void should_get_votes_by_range_time() throws Exception {
        LocalDateTime startTime = LocalDateTime.of(2020, Month.SEPTEMBER, 1, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2020, Month.SEPTEMBER, 5, 0, 0, 0);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTimeStr = df.format(startTime);
        String endTimeStr = df.format(endTime);

        mockMvc.perform(get("/votes/range/time")
                .param("startTime", startTimeStr)
                .param("endTime", endTimeStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].userId", is(userEntity.getId())))
                .andExpect(jsonPath("$[0].rsEventId", is(rsEventEntity.getId())))
                .andExpect(jsonPath("$[0].voteNum", is(1)))
                .andExpect(jsonPath("$[1].voteNum", is(2)));
    }
}
