package com.matchhub.matchhub.controller;

import com.matchhub.matchhub.dto.HubUserDTOBase;
import com.matchhub.matchhub.dto.HubUserDTODetails;
import com.matchhub.matchhub.dto.HubUserDTOImage;
import com.matchhub.matchhub.dto.HubUserDTOLinks;
import com.matchhub.matchhub.security.dto.ChangePasswordDTO;
import com.matchhub.matchhub.service.HubUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

@Tag(name = "HubUser", description = "")
@RestController
@RequestMapping(value = "/hubusers")
@RequiredArgsConstructor
public class HubUserController {
    private final HubUserService hubUserService;

    //Get user
    @Operation(summary = "Get User By ID", description = "Return only one user")
    @GetMapping(value = "/{id}")
    public ResponseEntity<HubUserDTODetails> findById(@PathVariable Long id){
        HubUserDTODetails hubUser = hubUserService.findById(id);
        return ResponseEntity.ok().body(hubUser);
    }

    @Operation(summary = "Get Logged in HubUser", description = "Return only one user")
    @GetMapping
    public ResponseEntity<HubUserDTOLinks> findHubUser(Principal connectedHubUser){
        HubUserDTOLinks hubUser = hubUserService.findHubUser(connectedHubUser);
        return ResponseEntity.ok().body(hubUser);
    }

    //Update user
    @PutMapping
    public ResponseEntity<HubUserDTOLinks> update(@RequestBody HubUserDTOBase hubUser,
                                                  Principal connectedHubUser){
        HubUserDTOLinks updatedHubUser = hubUserService.update(hubUser, connectedHubUser);
        return ResponseEntity.ok().body(updatedHubUser);
    }

    // Change password
    @PatchMapping
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDTO request,
                                            Principal connectedHubUser) {
        hubUserService.changePassword(request, connectedHubUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    /*
    @RequestParam: used in GET requests to extract data from query parameters,
    or in POST requests to extract data from submitted forms
    * */
    public ResponseEntity<HubUserDTOImage> uploadImage(@RequestParam("file") MultipartFile file,
                                                       Principal connectedHubUser)  throws IOException {
        HubUserDTOImage imgLink = hubUserService.uploadImageS3(file, connectedHubUser);
        return ResponseEntity.ok().body(imgLink);
    }

}
