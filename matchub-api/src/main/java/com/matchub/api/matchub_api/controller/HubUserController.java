package com.matchub.api.matchub_api.controller;

import com.matchub.api.matchub_api.dto.HubUserDTOBase;
import com.matchub.api.matchub_api.dto.HubUserDTODetails;
import com.matchub.api.matchub_api.dto.HubUserDTOImage;
import com.matchub.api.matchub_api.dto.HubUserDTOLinks;
import com.matchub.api.matchub_api.security.dto.ChangePasswordDTO;
import com.matchub.api.matchub_api.security.dto.ResetPasswordDTO;
import com.matchub.api.matchub_api.service.HubUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

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
        HubUserDTOLinks updatedHubUser = hubUserService.updateHubUser(hubUser, connectedHubUser);
        return ResponseEntity.ok().body(updatedHubUser);
    }

    // Change password
    @PatchMapping("/change-password")
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

    //Update user
    @PatchMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordDTO request,
                                                  Principal connectedHubUser){
        hubUserService.resetPassword(request, connectedHubUser);
        return ResponseEntity.ok().build();
    }

    //Check email
    @PatchMapping("/confirm")
    public ResponseEntity<Void> confirmEmail(Principal connectedHubUser, HttpServletResponse response){
        hubUserService.confirmToRegister(connectedHubUser, response);
        return ResponseEntity.noContent().build();
    }

}
