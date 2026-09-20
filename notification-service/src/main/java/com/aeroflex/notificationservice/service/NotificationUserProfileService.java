package com.aeroflex.notificationservice.service;

import com.aeroflex.common.events.BusinessEvent;
import com.aeroflex.common.events.user.UserProfileData;
import com.aeroflex.notificationservice.model.NotificationUserProfile;
import com.aeroflex.notificationservice.repository.NotificationUserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class NotificationUserProfileService {

    private final NotificationUserProfileRepository repository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void process(BusinessEvent<Object> event) {

        UserProfileData data =
                objectMapper.convertValue(
                        event.payload(),
                        UserProfileData.class
                );

        upsert(data);
    }


    public void upsert(
            UserProfileData data) {


        NotificationUserProfile profile =
                repository.findByUserId(data.userId())
                        .orElseGet(
                                NotificationUserProfile::new
                        );

        if (profile.getProfileVersion() != null
                && data.version() <= profile.getProfileVersion()) {

            return;
        }

        profile.setUserId(data.userId());
        profile.setUsername(data.username());
        profile.setEmail(data.email());
        profile.setMobile(data.mobile());
        profile.setEmailVerified(data.emailVerified());
        profile.setSmsVerified(data.smsVerified());
        profile.setProfileVersion(data.version());

        repository.save(profile);
    }
}
