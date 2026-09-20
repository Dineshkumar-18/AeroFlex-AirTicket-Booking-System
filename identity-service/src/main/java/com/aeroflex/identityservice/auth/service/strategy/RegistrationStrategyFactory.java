package com.aeroflex.identityservice.auth.service.strategy;

import com.aeroflex.common.exception.ResourceNotFoundException;
import com.aeroflex.identityservice.auth.enums.RegistrationType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class RegistrationStrategyFactory {

    private final Map<RegistrationType, RegistrationStrategy> strategies;

    public RegistrationStrategyFactory(
            List<RegistrationStrategy> strategyList
    ) {

        this.strategies = new EnumMap<>(RegistrationType.class);

        strategyList.forEach(strategy ->
                strategies.put(strategy.supports(), strategy)
        );
    }

    public RegistrationStrategy get(
            RegistrationType registrationType
    ) {

        RegistrationStrategy strategy =
                strategies.get(registrationType);

        if (strategy == null) {
            throw new ResourceNotFoundException(
                    "Valid Registration not found: " + registrationType.name()
            );
        }

        return strategy;
    }
}
