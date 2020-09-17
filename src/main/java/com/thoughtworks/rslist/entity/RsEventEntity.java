package com.thoughtworks.rslist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.rslist.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rs_event")
public class RsEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String eventName;
    private String keyword;

    @Valid
    private UserDto userDto;

    @JsonIgnore
    public UserDto getUserDto() {
        return userDto;
    }

    @JsonProperty
    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public RsEventEntity(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }
}
