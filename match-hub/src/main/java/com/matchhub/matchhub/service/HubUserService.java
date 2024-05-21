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
import org.hibernate.collection.spi.PersistentBag;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HubUserService{
    private final PasswordEncoder passwordEncoder;
    private final HubUserRepository hubUserRepository;
    private final ModelMapper modelMapper;

    private String maskUsername(String username) {
        // Verifica se a string tem menos de 4 caracteres
        if (username.length() < 4) {
            return username; // Retorna a string original se for muito curta para mascarar
        }

        // Pega o primeiro, segundo, penúltimo e último caractere
        char firstChar = username.charAt(0);
        char secondChar = username.charAt(1);
        char secondLastChar = username.charAt(username.length() - 2);
        char lastChar = username.charAt(username.length() - 1);

        // Cria uma string de asteriscos para substituir os caracteres entre o segundo e o penúltimo
        String maskedMiddle = "*".repeat(username.length() - 4);

        // Concatena os caracteres visíveis com a parte mascarada
        return firstChar + "" + secondChar + maskedMiddle + secondLastChar + lastChar;
    }

    public HubUserDTODetails findById(Long id) {
        // Get information and check existence
        Optional<HubUser> hubUser = hubUserRepository.findById(id);
        HubUser hubUserDomain = hubUser.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                "Id: "  + id + "." +
                "Type: " + HubUser.class.getName())
        );
        // Mask Username
        hubUserDomain.setUsername(maskUsername(hubUserDomain.getUsername()));
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

    public HubUserDTOLinks findHubUser(Principal connectedHubUser){
        // Get old information by user logged
        HubUser logged = (HubUser) ((UsernamePasswordAuthenticationToken) connectedHubUser).getPrincipal();
        // Mask Username
        logged.setUsername(maskUsername(logged.getUsername()));
        // Save update and give response
        return modelMapper.map(logged, HubUserDTOLinks.class);
    }


    public HubUserDTOLinks update(HubUserDTOBase hubUser,
                                  Principal connectedHubUser) {
        // Get old information by user logged
        HubUser updateHubUser = (HubUser) ((UsernamePasswordAuthenticationToken) connectedHubUser).getPrincipal();
        // Update user
        modelMapper.map(hubUser, updateHubUser);
        // Save update
        HubUserDTOLinks savedHubUser = modelMapper.map(hubUserRepository.save(updateHubUser), HubUserDTOLinks.class);
        // Mask Username
        savedHubUser.setUsername(maskUsername(savedHubUser.getUsername()));
        // Give response
        return savedHubUser;
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
        // Save update
        HubUserDTOLinks savedHubUser = modelMapper.map(hubUserRepository.save(updatedHubUser), HubUserDTOLinks.class);
        // Mask Username
        savedHubUser.setUsername(maskUsername(savedHubUser.getUsername()));
        // Give response
        return savedHubUser;
    }
}
