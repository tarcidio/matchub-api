package com.matchhub.matchhub.service;

import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.domain.Screen;
import com.matchhub.matchhub.dto.ScreenDTODetails;
import com.matchhub.matchhub.repository.ScreenRepository;
import com.matchhub.matchhub.service.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScreenService {

    private final ScreenRepository screenRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public ScreenService(ScreenRepository screenRepository, ModelMapper modelMapper){
        this.screenRepository = screenRepository;
        this.modelMapper = modelMapper;
    }

    public ScreenDTODetails findByChampionsId(Long spotlightId, Long opponentId) {
        // Get information and check existence
        Optional<Screen> screen = Optional.ofNullable(screenRepository.findBySpotlight_IdAndOpponent_Id(spotlightId, opponentId));
        Screen screenDomain = screen.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                "Spotlight Champion Id: "  + spotlightId + "." +
                "Opponent Champion Id: "  + opponentId + "." +
                "Type: " + Screen.class.getName())
        );
        // Converter
        ScreenDTODetails screenDTODetails = new ScreenDTODetails();
        modelMapper.map(screenDomain, screenDTODetails);
        // Return result
        return screenDTODetails;
    }

    public Screen findDomainById(Long id){
        Optional<Screen> screen = screenRepository.findById(id);
        return screen.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                "Id: "  + id + "." +
                "Type: " + Screen.class.getName())
        );
    }
}
