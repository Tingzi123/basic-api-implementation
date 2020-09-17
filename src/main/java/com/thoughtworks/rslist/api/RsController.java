package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import org.springframework.http.HttpHeaders;
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
                                                       @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return ResponseEntity.ok(rsList);
        }
        return ResponseEntity.ok(rsList.subList(start - 1, end));
    }

    @GetMapping("/rs/list/{index}")
    public ResponseEntity<RsEvent> getRsEvent(@PathVariable int index) {
        return ResponseEntity.ok(rsList.get(index - 1));
    }

    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent)  {

        HttpHeaders headers = new HttpHeaders();
        headers.add("index", String.valueOf(rsList.size()));

        for (RsEvent rs : rsList) {
            if (rs.getUserDto()!=null){
                if (rs.getUserDto().getName().equals(rsEvent.getUserDto().getName())){
                    RsEvent tmpRsEvent = new RsEvent(rsEvent.getEventName(), rsEvent.getKeyword());
                    rsList.add(tmpRsEvent);
                    return ResponseEntity.status(201).headers(headers).build();
                }
            }
        }
        rsList.add(rsEvent);
        return ResponseEntity.status(201).headers(headers).build();
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
}
