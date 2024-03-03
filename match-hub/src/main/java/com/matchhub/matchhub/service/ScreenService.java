package com.matchhub.matchhub.service;

import com.matchhub.matchhub.domain.Screen;
import com.matchhub.matchhub.repository.ScreenRepository;
import com.matchhub.matchhub.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScreenService {

    private final ScreenRepository screenRepository;

    @Autowired
    public ScreenService(ScreenRepository screenRepository){
        this.screenRepository = screenRepository;
    }

    public Screen findByChampionsId(Long spotlightId, Long opponentId) {
        Optional<Screen> obj = Optional.ofNullable(screenRepository.findBySpotlight_IdAndOpponent_Id(spotlightId, opponentId));
        return obj.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                        "Spotlight Champion Id: "  + spotlightId + "." +
                        "Opponent Champion Id: "  + opponentId + "." +
                        "Type: " + Screen.class.getName())
        );
    }

    public Screen findById(Long id){
        Optional<Screen> obj = screenRepository.findById(id);
        return obj.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                        "Id: "  + id + "." +
                        "Type: " + Screen.class.getName())
        );
    }
}
