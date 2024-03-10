package com.matchhub.matchhub.controller;

import com.matchhub.matchhub.dto.HubUserDTODetails;
import com.matchhub.matchhub.dto.HubUserDTOLinks;
import com.matchhub.matchhub.security.dto.ChangeBlockStateDTO;
import com.matchhub.matchhub.service.CommentService;
import com.matchhub.matchhub.service.HubUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "Moderator", description = "")
@RestController
@RequestMapping(value = "/moderators")
@RequiredArgsConstructor
public class ModeratorController {
    private final HubUserService hubUserService;
    private final CommentService commentService;

    @PutMapping(value = "/{hubUserid}")
    public ResponseEntity<HubUserDTOLinks> block(@PathVariable Long hubUserid,
                                                 @RequestBody ChangeBlockStateDTO request){
        HubUserDTOLinks blockedHubUser = hubUserService.block(hubUserid, request);
        return ResponseEntity.ok().body(blockedHubUser);
    }

    @DeleteMapping(value = "/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId){
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }
}
