package com.matchhub.matchhub.service;

import com.matchhub.matchhub.domain.HubUser;
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

    public HubUser findById(Long id) {
        Optional<HubUser> hubUser = hubUserRepository.findById(id);
        return hubUser.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                        "Id: "  + id + "." +
                        "Type: " + HubUser.class.getName())
        );
    }

    public HubUser save(HubUser hubUser) {
        /*Need authentication*/
        hubUser.setId(null);
        return hubUserRepository.save(hubUser);
    }

    public HubUser update(HubUser hubUser) {
        HubUser updatedUser = this.findById(hubUser.getId());
        /*Need authentication*/
        /*VERIFY IF USER HAVE PERMISSION*/
        modelMapper.map(hubUser, updatedUser);
        return hubUserRepository.save(updatedUser);
    }

    public void delete(Long id) {
        this.findById(id);
        /*Need authentication*/
        /*VERIFY IF USER HAVE PERMISSION*/
        hubUserRepository.deleteById(id);
    }
}
