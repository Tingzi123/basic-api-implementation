package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteController {
    private UserRepository userRepository;

    private RsEventRepository rsEventRepository;

    private VoteRepository voteRepository;

    public VoteController(UserRepository userRepository, RsEventRepository rsEventRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
        this.voteRepository = voteRepository;
    }

    @Transactional
    @PostMapping("/rs/vote/{RsEventId}")
    public ResponseEntity voteForRsEvent(@PathVariable int RsEventId, @RequestBody VoteDto voteDto) {
        UserEntity user = userRepository.findUserById(voteDto.getUserId());
        /*if (user.getVote() < voteDto.getVoteNum()) {
            return ResponseEntity.badRequest().build();
        }*/

        VoteEntity voteEntity = VoteEntity.builder()
                .voteNum(voteDto.getVoteNum())
                .userId(voteDto.getUserId())
                .voteTime(voteDto.getVoteTime())
                .build();
        voteRepository.save(voteEntity);
        userRepository.updateVoteByUserId(voteDto.getUserId(), user.getVote() - voteDto.getVoteNum());
        return ResponseEntity.created(null).build();
    }
}
