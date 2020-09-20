package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.Constant.ComConst;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoteService {
    private UserRepository userRepository;

    private RsEventRepository rsEventRepository;

    private VoteRepository voteRepository;

    public VoteService(UserRepository userRepository, RsEventRepository rsEventRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
        this.voteRepository = voteRepository;
    }

    public List<VoteDto> getVotesByUserIdAndRsEventId(int userId, int rsEventId, int pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex - ComConst.INDEXDIFF, ComConst.PAGESIZE);

        List<VoteEntity> votes = voteRepository.findAllByUserIdAndRsEventId(userId, rsEventId, pageable);
        VoteDto voteDto = new VoteDto();

        return votes.stream().map(vote -> voteDto.builder()
                .userId(vote.getUser().getId())
                .rsEventId(vote.getRsEvent().getId())
                .voteNum(vote.getVoteNum())
                .voteTime(vote.getVoteTime())
                .build()
        ).collect(Collectors.toList());
    }
}
