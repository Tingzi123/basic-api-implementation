package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    @Override
    List<UserEntity> findAll();

    UserEntity findUserById(Integer id);

    void deleteUserById(Integer id);
}
