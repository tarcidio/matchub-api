package com.matchub.api.matchub_api.controller;

import com.matchub.api.matchub_api.dto.HubUserDTOLinks;
import com.matchub.api.matchub_api.security.dto.ChangeBlockStateDTO;
import com.matchub.api.matchub_api.service.CommentService;
import com.matchub.api.matchub_api.service.HubUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
