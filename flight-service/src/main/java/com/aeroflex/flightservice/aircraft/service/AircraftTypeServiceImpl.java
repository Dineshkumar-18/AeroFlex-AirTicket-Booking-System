package com.aeroflex.flightservice.aircraft.service;

import com.aeroflex.common.exception.ConflictException;
import com.aeroflex.common.exception.ResourceNotFoundException;
import com.aeroflex.flightservice.aircraft.dto.AircraftTypeRequest;
import com.aeroflex.flightservice.aircraft.dto.AircraftTypeResponse;
import com.aeroflex.flightservice.aircraft.entity.AircraftType;
import com.aeroflex.flightservice.aircraft.repository.AircraftTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AircraftTypeServiceImpl
        implements AircraftTypeService {

    private final AircraftTypeRepository aircraftTypeRepository;

    @Override
    @Transactional
    public AircraftTypeResponse createAircraftType(
            AircraftTypeRequest request) {

        String model = request.model().trim();

        if (aircraftTypeRepository.existsByModelIgnoreCase(model)) {
            throw new ConflictException(
                    "Aircraft type with model '" + model + "' already exists"
            );
        }

        AircraftType aircraftType = AircraftType.builder()
                .model(model)
                .manufacturer(request.manufacturer().trim())
                .economyCapacity(request.economyCapacity())
                .premiumEconomyCapacity(request.premiumEconomyCapacity())
                .businessCapacity(request.businessCapacity())
                .firstClassCapacity(request.firstClassCapacity())
                .seatingConfig(request.seatingConfig().trim())
                .active(true)
                .build();

        AircraftType saved = aircraftTypeRepository.save(aircraftType);

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AircraftTypeResponse getAircraftTypeById(
            UUID id) {

        AircraftType aircraftType =
                aircraftTypeRepository.findByIdAndActiveTrue(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Aircraft type not found"
                                )
                        );

        return mapToResponse(aircraftType);
    }

    @Override
    @Transactional(readOnly = true)
    public AircraftTypeResponse getAircraftTypeByIdIncludingInactive(UUID id) {



        AircraftType aircraftType =
                aircraftTypeRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Aircraft type not found"
                                )
                        );

        return mapToResponse(aircraftType);
    }



    @Override
    @Transactional(readOnly = true)
    public Page<AircraftTypeResponse> getAircraftTypes(
            String search,
            Pageable pageable) {

        Page<AircraftType> aircraftTypes;

        if (search == null || search.isBlank()) {
            aircraftTypes =
                    aircraftTypeRepository.findAll(pageable);
        } else {
            aircraftTypes =
                    aircraftTypeRepository.search(
                            search.trim(),
                            pageable
                    );
        }

        return aircraftTypes.map(this::mapToResponse);

    }

    @Override
    @Transactional
    public AircraftTypeResponse updateAircraftType(
            UUID id,
            AircraftTypeRequest request) {

        AircraftType aircraftType =
                aircraftTypeRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Aircraft type not found"
                                )
                        );

        String model = request.model().trim();

        if (aircraftTypeRepository
                .existsByModelIgnoreCaseAndIdNot(model, id)) {

            throw new ConflictException(
                    "Aircraft type with model '" + model
                            + "' already exists"
            );
        }

        aircraftType.setModel(model);
        aircraftType.setManufacturer(
                request.manufacturer().trim()
        );
        aircraftType.setEconomyCapacity(
                request.economyCapacity()
        );
        aircraftType.setPremiumEconomyCapacity(
                request.premiumEconomyCapacity()
        );
        aircraftType.setBusinessCapacity(
                request.businessCapacity()
        );
        aircraftType.setFirstClassCapacity(
                request.firstClassCapacity()
        );
        aircraftType.setSeatingConfig(
                request.seatingConfig().trim()
        );

        return mapToResponse(aircraftType);
    }

    @Override
    @Transactional
    public void deleteAircraftType(UUID id) {

        AircraftType aircraftType =
                aircraftTypeRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Aircraft type not found"
                                )
                        );

        aircraftType.setActive(false);
    }

    private AircraftTypeResponse mapToResponse(
            AircraftType aircraftType) {

        return new AircraftTypeResponse(
                aircraftType.getId(),
                aircraftType.getModel(),
                aircraftType.getManufacturer(),
                aircraftType.getTotalCapacity(),
                aircraftType.getEconomyCapacity(),
                aircraftType.getPremiumEconomyCapacity(),
                aircraftType.getBusinessCapacity(),
                aircraftType.getFirstClassCapacity(),
                aircraftType.getSeatingConfig(),
                aircraftType.isActive()
        );
    }

}
