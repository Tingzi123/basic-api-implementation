package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.RsEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RsEventRepository extends JpaRepository<RsEventEntity,Integer> {
    List<RsEventEntity> findAll();

}
