package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.enums.NotificationChannel;
import com.aeroflex.notificationservice.model.NotificationUserProfile;
import com.aeroflex.notificationservice.repository.NotificationUserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RecipientResolver {

    private final NotificationUserProfileRepository profileRepository;

    private final Map<
                NotificationChannel,
                ChannelRecipientResolver<? extends ChannelRecipient>
                > resolvers;

    public RecipientResolver(
            NotificationUserProfileRepository profileRepository,
            List<ChannelRecipientResolver<? extends ChannelRecipient>>
                    resolverList) {

        this.profileRepository = profileRepository;

        this.resolvers =
                resolverList.stream()
                        .collect(Collectors.toMap(
                                ChannelRecipientResolver::channel,
                                Function.identity()
                        ));
    }

    public ChannelRecipient resolve(
            String userId,
            NotificationChannel channel) {

        NotificationUserProfile profile =
                profileRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "User profile not found: "
                                                + userId
                                ));

        ChannelRecipientResolver<? extends ChannelRecipient>
                resolver = resolvers.get(channel);

        if (resolver == null) {
            throw new IllegalStateException(
                    "No recipient resolver configured for: "
                            + channel
            );
        }

        return resolver.resolve(profile);
    }
}
