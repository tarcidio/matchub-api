package com.matchhub.matchhub.service;

import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.dto.HubUserDTOBase;
import com.matchhub.matchhub.dto.HubUserDTODetails;
import com.matchhub.matchhub.dto.HubUserDTOLinks;
import com.matchhub.matchhub.repository.HubUserRepository;
import com.matchhub.matchhub.security.dto.ChangeBlockStateDTO;
import com.matchhub.matchhub.security.dto.ChangePasswordDTO;
import com.matchhub.matchhub.security.dto.ChangePositionDTO;
import com.matchhub.matchhub.service.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HubUserService{
    private final PasswordEncoder passwordEncoder;
    private final HubUserRepository hubUserRepository;
    private final ModelMapper modelMapper;

    public HubUserDTODetails findById(Long id) {
        // Get information and check existence
        Optional<HubUser> hubUser = hubUserRepository.findById(id);
        HubUser hubUserDomain = hubUser.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                "Id: "  + id + "." +
                "Type: " + HubUser.class.getName())
        );
        // Return result
        return modelMapper.map(hubUserDomain, HubUserDTODetails.class);
    }

    public HubUser findDomainById(Long id) {
        // Get information and check existence
        Optional<HubUser> hubUser = hubUserRepository.findById(id);
        return hubUser.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                        "Id: "  + id + "." +
                        "Type: " + HubUser.class.getName())
        );
    }


    public HubUserDTOLinks update(HubUserDTOBase hubUser,
                                  Principal connectedHubUser) {
        // Get old information by user logged
        HubUser updateHubUser = (HubUser) ((UsernamePasswordAuthenticationToken) connectedHubUser).getPrincipal();
        // Update user
        modelMapper.map(hubUser, updateHubUser);
        // Save update and give response
        return modelMapper.map(hubUserRepository.save(updateHubUser), HubUserDTOLinks.class);
    }

    public void changePassword(ChangePasswordDTO request,
                               Principal connectedHubUser) {
        // Get old information by user logged
        HubUser logged = (HubUser) ((UsernamePasswordAuthenticationToken) connectedHubUser).getPrincipal();

        // Check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), logged.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // Check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }
        // Update the password
        logged.setPassword(passwordEncoder.encode(request.getNewPassword()));
        // Save the new password
        hubUserRepository.save(logged);
    }

    public HubUserDTOLinks block(Long hubUserid, ChangeBlockStateDTO request) {
        // Get de new block user
        HubUser blocked = this.findDomainById(hubUserid);
        // Alter state
        blocked.setBlocked(request.getBlocked());
        // Save
        return modelMapper.map(hubUserRepository.save(blocked), HubUserDTOLinks.class);
    }

    public void delete(Long id) {
        hubUserRepository.deleteById(id);
    }

    public HubUserDTOLinks alterPosition(Long hubUserid, ChangePositionDTO request) {
        HubUser updatedHubUser = this.findDomainById(hubUserid);
        updatedHubUser.setRole(request.getRole());
        return modelMapper.map(hubUserRepository.save(updatedHubUser), HubUserDTOLinks.class);
    }
}
