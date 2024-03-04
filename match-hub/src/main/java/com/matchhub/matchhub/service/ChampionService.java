package com.matchhub.matchhub.service;

import com.matchhub.matchhub.domain.Champion;
import com.matchhub.matchhub.domain.Screen;
import com.matchhub.matchhub.dto.ChampionDTODetails;
import com.matchhub.matchhub.dto.ScreenDTODetails;
import com.matchhub.matchhub.repository.ChampionRepository;
import com.matchhub.matchhub.repository.ScreenRepository;
import com.matchhub.matchhub.service.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChampionService {

    private final ChampionRepository championRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public ChampionService(ChampionRepository championRepository, ModelMapper modelMapper){
        this.championRepository = championRepository;
        this.modelMapper = modelMapper;
    }
    public ChampionDTODetails findById(Long id) {
        // Get information and check existence
        Optional<Champion> champion = championRepository.findById(id);
        Champion championDomain = champion.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                        "Id: "  + id + "." +
                        "Type: " + Champion.class.getName())
        );
        // Converter
        ChampionDTODetails championDTODetails = new ChampionDTODetails();
        modelMapper.map(championDomain, championDTODetails);
        // Return result
        return championDTODetails;
    }
}
