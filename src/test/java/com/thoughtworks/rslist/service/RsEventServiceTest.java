package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class RsEventServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    RsEventRepository rsEventRepository;

    @Mock
    VoteRepository voteRepository;

    RsEventService rsEventService;

    @BeforeEach
    void setUp() {
        initMocks(this);
        rsEventService = new RsEventService(userRepository, rsEventRepository, voteRepository);
    }

    @Test
    void should_vote_success() {
        LocalDateTime voteTime = LocalDateTime.now();
        int userVotNum = 20;
        int RsEventVotNum = 30;
        int voteNum = 5;

        int userId = 12;
        int rsEventId = 15;

        VoteDto voteDto = VoteDto.builder()
                .voteNum(voteNum)
                .voteTime(voteTime)
                .userId(userId)
                .rsEventId(rsEventId)
                .build();

        UserEntity userEntity=UserEntity.builder()
                .vote(userVotNum)
                .build();

        RsEventEntity rsEventEntity=RsEventEntity.builder()
                .voteNum(RsEventVotNum)
                .build();

        VoteEntity voteEntity = VoteEntity.builder()
                .voteNum(voteDto.getVoteNum())
                .voteTime(voteDto.getVoteTime())
                .user(userEntity)
                .rsEvent(rsEventEntity)
                .build();


        when(rsEventRepository.findById(rsEventId)).thenReturn(Optional.of(rsEventEntity));
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        rsEventService.vote(voteDto, rsEventId);

        verify(userRepository).save(userEntity);
        verify(voteRepository).save(voteEntity);
        verify(rsEventRepository).save(rsEventEntity);
    }
}
