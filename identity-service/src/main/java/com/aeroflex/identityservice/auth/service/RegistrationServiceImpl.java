package com.aeroflex.identityservice.auth.service;

import com.aeroflex.common.enums.RoleType;
import com.aeroflex.identityservice.auth.enums.RegistrationType;
import com.aeroflex.identityservice.auth.service.strategy.RegistrationStrategy;
import com.aeroflex.identityservice.auth.service.strategy.RegistrationStrategyFactory;
import com.aeroflex.identityservice.auth.validator.UserValidator;
import com.aeroflex.identityservice.security.password.PasswordService;
import com.aeroflex.identityservice.user.dto.request.RegisterRequest;
import com.aeroflex.identityservice.user.entity.User;
import com.aeroflex.identityservice.user.factory.UserFactory;
import com.aeroflex.identityservice.user.repository.UserRepository;
import com.aeroflex.identityservice.userrole.service.UserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationServiceImpl
        implements RegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final PasswordService passwordService;
    private final RegistrationStrategyFactory strategyFactory;
    private final UserValidator userValidator;
    private final UserFactory  userFactory;

    public RegistrationServiceImpl(
            UserRepository userRepository,
            UserRoleService userRoleService,
            PasswordService passwordService,
            RegistrationStrategyFactory strategyFactory,
            UserValidator userValidator,
            UserFactory userFactory
    ) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.passwordService = passwordService;
        this.strategyFactory = strategyFactory;
        this.userValidator = userValidator;
        this.userFactory = userFactory;
    }

    @Override
    @Transactional
    public void register(
            RegisterRequest request,
            RegistrationType registrationType
    ) {

        LOGGER.info("Registration is started for the {}",registrationType.name());

        userValidator.validateRegistration(request);

        String encodedPassword =
                passwordService.encode(request.password());

        User user = userFactory.createCustomer(request, encodedPassword);

        userRoleService.assignRole(user,RoleType.USER);

        RegistrationStrategy strategy =
                strategyFactory.get(registrationType);

        strategy.handle(user, request);

        User regUser = userRepository.save(user);

        LOGGER.info("User - {} has been registered successfully",regUser.getId());
    }
}
