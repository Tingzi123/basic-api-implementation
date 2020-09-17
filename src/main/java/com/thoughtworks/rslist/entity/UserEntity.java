package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Column(name = "username")
    private String name;

    private String gender;

    private int age;

    private String email;

    private String phone;
    private int vote;

    @OneToMany(mappedBy = "userId",cascade = CascadeType.REMOVE)
    private List<RsEventEntity> rsEventEntitys;
}
