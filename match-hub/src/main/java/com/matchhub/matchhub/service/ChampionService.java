package com.matchhub.matchhub.service;

import com.matchhub.matchhub.domain.Champion;
import com.matchhub.matchhub.dto.ChampionDTODetails;
import com.matchhub.matchhub.repository.ChampionRepository;
import com.matchhub.matchhub.service.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChampionService {
    private final ChampionRepository championRepository;
    private final ModelMapper modelMapper;

    public Champion findDomainById(Long id) {
        // Get information and check existence
        Optional<Champion> champion = championRepository.findById(id);
        return champion.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                        "Id: "  + id + "." +
                        "Type: " + Champion.class.getName())
        );
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
