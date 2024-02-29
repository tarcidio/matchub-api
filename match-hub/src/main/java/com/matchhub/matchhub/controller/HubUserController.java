package com.matchhub.matchhub.controller;

import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.service.HubUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/users")
public class HubUserController {

    private final HubUserService hubUserService;

    @Autowired
    public HubUserController(HubUserService hubUserService){
        this.hubUserService = hubUserService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<HubUser> findById(@PathVariable Long id){
        HubUser hubUser = hubUserService.findById(id);
        return ResponseEntity.ok().body(hubUser);
    }

    @PostMapping
    public ResponseEntity<HubUser> create(@RequestBody HubUser hubUser){
        HubUser savedHubUser = hubUserService.save(hubUser);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedHubUser.getId())
                .toUri();
        return ResponseEntity.created(uri).body(savedHubUser);
    }

    @PutMapping
    public ResponseEntity<HubUser> update(@RequestBody HubUser hubUser){
        HubUser updatedHubUser = hubUserService.update(hubUser);
        return ResponseEntity.ok().body(updatedHubUser);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        hubUserService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
