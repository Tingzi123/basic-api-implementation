package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.RsEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RsEventRepository extends JpaRepository<RsEventEntity, Integer> {
    List<RsEventEntity> findAll();

   /* @Transactional
    void deleteByUserId(int userId);*/

   @Transactional
    @Modifying
    @Query(value = "update rs_event r set r.keyword=?2 where r.id=?1", nativeQuery = true)
    void updateKeywordbyId(Integer id, String keyword);

}
