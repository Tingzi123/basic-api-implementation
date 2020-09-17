package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @Override
    List<UserEntity> findAll();

    UserEntity findUserById(Integer id);

    void deleteUserById(Integer id);

    @Transactional
    @Modifying
    @Query(value = "update User u set u.vote=?2 where u.id=?1",nativeQuery = true)
    void updateVoteByUserId(Integer id, int vote);
}
