package com.matchub.api.matchub_api.service;

import com.matchub.api.matchub_api.domain.Screen;
import com.matchub.api.matchub_api.dto.ScreenDTODetails;
import com.matchub.api.matchub_api.repository.ScreenRepository;
import com.matchub.api.matchub_api.service.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScreenService {
    private final ScreenRepository screenRepository;
    private final ModelMapper modelMapper;

    public ScreenDTODetails findByChampionsId(Long spotlightId, Long opponentId) {
        // Get information and check existence
        Optional<Screen> screen = screenRepository.findBySpotlight_IdAndOpponent_Id(spotlightId, opponentId);
        Screen screenDomain = screen.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                "Spotlight Champion Id: "  + spotlightId + "." +
                "Opponent Champion Id: "  + opponentId + "." +
                "Type: " + Screen.class.getName())
        );
        // It's need because List conversion
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

    public ScreenDTODetails findById(Long id) {
        // Get information and check existence
        Optional<Screen> screen = screenRepository.findById(id);
        Screen screenDomain = screen.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                        "Id: "  + id + "." +
                        "Type: " + Screen.class.getName())
        );
        // It's need because List conversion
        ScreenDTODetails screenDTODetails = new ScreenDTODetails();
        modelMapper.map(screenDomain, screenDTODetails);
        // Return result
        return screenDTODetails;
    }
}
