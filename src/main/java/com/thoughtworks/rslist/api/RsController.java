package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.exceptions.InvalidRequestParamException;
import com.thoughtworks.rslist.exceptions.MyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
    private List<RsEvent> rsList = initRsList();

    private List<RsEvent> initRsList() {
        List<RsEvent> tmpRsList = new ArrayList<>();
        tmpRsList.add(new RsEvent("第一条事件", "无分类",new UserDto("lily","female",20,"lily@163.com","12387898789")));
        tmpRsList.add(new RsEvent("第二条事件", "无分类"));
        tmpRsList.add(new RsEvent("第三条事件", "无分类"));

        return tmpRsList;
    }

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getAllRsEvent(@RequestParam(required = false) Integer start,
                                                       @RequestParam(required = false) Integer end) throws InvalidRequestParamException {
        List<RsEvent> reRsEvents = getRsEventsNotUser();

        if (start == null || end == null) {
            return ResponseEntity.ok(reRsEvents);
        }

        if (start<0 || start>rsList.size()|| end<0 || end>rsList.size()){
            throw new InvalidRequestParamException();
        }
        return ResponseEntity.ok(reRsEvents.subList(start - 1, end));
    }

    @GetMapping("/rs/list/{index}")
    public ResponseEntity<RsEvent> getRsEvent(@PathVariable int index) {
        if (index>rsList.size()){
            throw new IndexOutOfBoundsException();
        }
        List<RsEvent> reRsEvents = getRsEventsNotUser();
        return ResponseEntity.ok(reRsEvents.get(index - 1));
    }

    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent) throws JsonProcessingException {
        for (RsEvent rs : rsList) {
            if (rs.getUserDto().getName().equals(rsEvent.getUserDto().getName())){
                RsEvent tmpRsEvent = new RsEvent(rsEvent.getEventName(), rsEvent.getKeyword());
                rsList.add(tmpRsEvent);
                return ResponseEntity.status(201).build();
            }
        }
        rsList.add(rsEvent);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/rs/event/change/{index}")
    public ResponseEntity changeRsEvent(@PathVariable int index,
                                        @RequestBody String rsEventStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RsEvent rsEvent = objectMapper.readValue(rsEventStr, RsEvent.class);
        rsList.set(index, rsEvent);
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping("/rs/event/delete/{index}")
    public ResponseEntity deleteRsEvent(@PathVariable int index) {
        rsList.remove(index);
        return ResponseEntity.ok(null);
    }

    private List<RsEvent> getRsEventsNotUser() {
        List<RsEvent> reRsEvents = new ArrayList<>();
        for (RsEvent rsEvent : rsList) {
            RsEvent tmpRsEvent = new RsEvent(rsEvent.getEventName(), rsEvent.getKeyword());
            reRsEvents.add(tmpRsEvent);
        }
        return reRsEvents;
    }

}
