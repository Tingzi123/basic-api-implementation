package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import lombok.Data;

@Data
public class CommentService {
    private UserDto userDto;
    private RsEvent rsEvent;


}
