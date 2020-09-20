package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
    private UserRepository userRepository;

    private RsEventRepository rsEventRepository;

    private VoteRepository voteRepository;

    public RsController(UserRepository userRepository, RsEventRepository rsEventRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
        this.voteRepository = voteRepository;
    }

    private List<RsEvent> rsList = initRsList();

    private List<RsEvent> initRsList() {
        List<RsEvent> tmpRsList = new ArrayList<>();
        /*tmpRsList.add(new RsEvent("第一条事件", "无分类",new UserDto("lily","female",20,"lily@163.com","12387898789")));
        tmpRsList.add(new RsEvent("第二条事件", "无分类"));
        tmpRsList.add(new RsEvent("第三条事件", "无分类"));
*/
        return tmpRsList;
    }

    @JsonView(RsEvent.rsEventDetailView.class)
    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getAllRsEvent(@RequestParam(required = false) Integer start,
                                                       @RequestParam(required = false) Integer end) {
        if (start == null || end == null) {
            return ResponseEntity.ok(rsList);
        }
        return ResponseEntity.ok(rsList.subList(start - 1, end));
    }

    @JsonView(RsEvent.rsEventDetailView.class)
    @GetMapping("/rs/list/{index}")
    public ResponseEntity<RsEvent> getRsEvent(@PathVariable int index) {
        return ResponseEntity.ok(rsList.get(index - 1));
    }

    /*@PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent)  {
        HttpHeaders headers = new HttpHeaders();
        headers.add("index", String.valueOf(rsList.size()));

        if (!userRepository.existsById(rsEvent.getUserId())){
            return ResponseEntity.badRequest().headers(headers).build();
        }

        RsEventEntity rsEventEntity=RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyword())
                .userId(rsEvent.getUserId())
                .build();

        rsEventRepository.save(rsEventEntity);

//        rsList.add(rsEvent);
        return ResponseEntity.status(201).headers(headers).build();
    }*/

    @DeleteMapping("/rs/event/delete/{index}")
    public ResponseEntity deleteRsEvent(@PathVariable int index) {
        rsList.remove(index);
        return ResponseEntity.ok(null);
    }

    @Transactional
    @PutMapping("/rs/event/change")
    public ResponseEntity changeRsEvent(@RequestParam int userId, @RequestParam int index,
                                        @RequestBody RsEvent rsEvent)  {
//        rsList.set(index, rsEvent);
        if (userId!=rsEvent.getUserId()){
            return ResponseEntity.badRequest().build();
        }
        rsEventRepository.updateKeywordbyId(index,rsEvent.getKeyword());
        return ResponseEntity.created(null).build();
    }
}
