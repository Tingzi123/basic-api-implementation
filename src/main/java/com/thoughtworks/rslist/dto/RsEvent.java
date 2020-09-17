package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
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

    public RsEvent(String eventName, String keyword) {
        this.eventName = eventName;
        this.keyword = keyword;
    }
}
