package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RsEvent {
    public interface rsEventSimpleView {
    }

    public interface rsEventDetailView extends rsEventSimpleView {
    }

    private String eventName;
    private String keyword;
    private int voteNum;

    private int userId;

    @JsonView(rsEventDetailView.class)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public RsEvent(String eventName, String keyword,int voteNum) {
        this.eventName = eventName;
        this.keyword = keyword;
        this.voteNum = voteNum;
    }
}
