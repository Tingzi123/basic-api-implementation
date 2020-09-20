package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<VoteEntity, Integer> {
    @Override
    List<VoteEntity> findAll();

    List<VoteEntity> findAllByUserIdAndRsEventId(int userId, int RsEventId);
}
