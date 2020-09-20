package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RsEventService {
    private UserRepository userRepository;

    private RsEventRepository rsEventRepository;

    private VoteRepository voteRepository;

    public RsEventService(UserRepository userRepository, RsEventRepository rsEventRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
        this.voteRepository = voteRepository;
    }

    public void vote(VoteDto vote, int rsEventId) {
        Optional<RsEventEntity> rsEventEntity = rsEventRepository.findById(rsEventId);
        Optional<UserEntity> userEntity = userRepository.findById(vote.getUserId());

        if (!rsEventEntity.isPresent()
                || !userEntity.isPresent()
                || vote.getVoteNum() > userEntity.get().getVote()) {
            throw new RuntimeException();
        }

        VoteEntity voteEntity=VoteEntity.builder()
                .voteNum(vote.getVoteNum())
                .voteTime(vote.getVoteTime())
                .user(userEntity.get())
                .rsEvent(rsEventEntity.get())
                .build();
        voteRepository.save(voteEntity);

        UserEntity user=userEntity.get();
        user.setVote(user.getVote()-vote.getVoteNum());
        userRepository.save(user);

        RsEventEntity rsEvent=rsEventEntity.get();
        rsEvent.setVoteNum(rsEvent.getVoteNum()+vote.getVoteNum());
        rsEventRepository.save(rsEvent);
    }
}
