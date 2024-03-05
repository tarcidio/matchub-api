package com.matchhub.matchhub.service;

import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.dto.HubUserDTOBase;
import com.matchhub.matchhub.dto.HubUserDTODetails;
import com.matchhub.matchhub.dto.HubUserDTOLinks;
import com.matchhub.matchhub.dto.ScreenDTODetails;
import com.matchhub.matchhub.repository.HubUserRepository;
import com.matchhub.matchhub.service.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HubUserService{
    private final HubUserRepository hubUserRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public HubUserService(HubUserRepository hubUserRepository, ModelMapper modelMapper) {
        this.hubUserRepository = hubUserRepository;
        this.modelMapper = modelMapper;
    }

    public HubUser findDomainById(Long id){
        Optional<HubUser> hubUser = hubUserRepository.findById(id);
        return hubUser.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                        "Id: "  + id + "." +
                        "Type: " + HubUser.class.getName())
        );
    }

    public HubUserDTODetails findById(Long id) {
        // Get information and check existence
        Optional<HubUser> hubUser = hubUserRepository.findById(id);
        HubUser hubUserDomain = hubUser.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                "Id: "  + id + "." +
                "Type: " + HubUser.class.getName())
        );
        // Converter
        HubUserDTODetails hubUserDTODetails = new HubUserDTODetails();
        modelMapper.map(hubUserDomain, hubUserDTODetails);
        // Return result
        return hubUserDTODetails;
    }

    public HubUserDTOLinks save(HubUserDTOBase hubUser) {
        /*Need authentication*/
        // Create repository instance
        HubUser saveHubUser = new HubUser();
        // Convert
        modelMapper.map(hubUser, saveHubUser);
        // Set null
        saveHubUser.setId(null);
        return modelMapper.map(hubUserRepository.save(saveHubUser), HubUserDTOLinks.class);
    }

    public HubUserDTOLinks update(Long hubUserId, HubUserDTOBase hubUser) {
        //Get old user
        HubUser updatedUser = this.findDomainById(hubUserId);
        /*Need authentication*/
        /*VERIFY IF USER HAVE PERMISSION*/
        modelMapper.map(hubUser, updatedUser);
        return modelMapper.map(hubUserRepository.save(updatedUser), HubUserDTOLinks.class);
    }

    /* Disabled: User never wil be deleted */
//    public void delete(Long id) {
//        this.findById(id);
//        /*Need authentication*/
//        /*VERIFY IF USER HAVE PERMISSION*/
//        hubUserRepository.deleteById(id);
//    }
}
