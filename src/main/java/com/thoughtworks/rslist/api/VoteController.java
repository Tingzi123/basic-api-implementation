package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.RsEventService;
import com.thoughtworks.rslist.service.UserService;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class VoteController {
    private UserService userService;

    private RsEventService rsEventService;

    private VoteService voteService;

    public VoteController(UserService userService, RsEventService rsEventService, VoteService voteService) {
        this.userService = userService;
        this.rsEventService = rsEventService;
        this.voteService = voteService;
    }
/*
    @Transactional
    @PostMapping("/rs/vote/{RsEventId}")
    public ResponseEntity voteForRsEvent(@PathVariable int RsEventId, @RequestBody VoteDto voteDto) {
        UserEntity user = userRepository.findUserById(voteDto.getUserId());
        *//*if (user.getVote() < voteDto.getVoteNum()) {
            return ResponseEntity.badRequest().build();
        }*//*

        VoteEntity voteEntity = VoteEntity.builder()
                .voteNum(voteDto.getVoteNum())
//                .userId(voteDto.getUserId())
                .voteTime(voteDto.getVoteTime())
                .build();
        voteRepository.save(voteEntity);
        userRepository.updateVoteByUserId(voteDto.getUserId(), user.getVote() - voteDto.getVoteNum());
        return ResponseEntity.created(null).build();
    }*/

    //分割线
    @GetMapping("/votes")
    public ResponseEntity<List<VoteDto>> getVotesByUserIdAndRsEventId(@RequestParam int userId,
                                                                      @RequestParam int rsEventId,
                                                                      @RequestParam(defaultValue = "1") int pageIndex) {
        List<VoteDto> votes = voteService.getVotesByUserIdAndRsEventId(userId, rsEventId, pageIndex);
        return ResponseEntity.ok(votes);
    }

    @GetMapping("/votes/range/time")
    public ResponseEntity<List<VoteDto>> getVotesByUserIdAndRsEventId(@RequestParam String startTime,
                                                                      @RequestParam String endTime,
                                                                      @RequestParam(defaultValue = "1") int pageIndex) {
        List<VoteDto> votes = voteService.getVotesByRangeTime(startTime,endTime,pageIndex);
        return ResponseEntity.ok(votes);
    }
}
