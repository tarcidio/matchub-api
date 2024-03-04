package com.matchhub.matchhub.controller;

import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.dto.HubUserDTOBase;
import com.matchhub.matchhub.dto.HubUserDTODetails;
import com.matchhub.matchhub.dto.HubUserDTOLinks;
import com.matchhub.matchhub.service.HubUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "HubUser", description = "")
@RestController
@RequestMapping(value = "/users")
public class HubUserController {

    private final HubUserService hubUserService;

    @Autowired
    public HubUserController(HubUserService hubUserService){
        this.hubUserService = hubUserService;
    }

    @Operation(summary = "Get User By ID", description = "Return only one user")
    @GetMapping(value = "/{id}")
    public ResponseEntity<HubUserDTODetails> findById(@PathVariable Long id){
        HubUserDTODetails hubUser = hubUserService.findById(id);
        return ResponseEntity.ok().body(hubUser);
    }

    @PostMapping
    public ResponseEntity<HubUserDTOLinks> create(@RequestBody HubUserDTOBase hubUser){
        HubUserDTOLinks savedHubUser = hubUserService.save(hubUser);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedHubUser.getId())
                .toUri();
        return ResponseEntity.created(uri).body(savedHubUser);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<HubUserDTOLinks> update(@PathVariable Long id,
                                                  @RequestBody HubUserDTOBase hubUser){
        HubUserDTOLinks updatedHubUser = hubUserService.update(id, hubUser);
        return ResponseEntity.ok().body(updatedHubUser);
    }

    /* Disabled: User never wil be deleted */
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id){
//        hubUserService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
}
