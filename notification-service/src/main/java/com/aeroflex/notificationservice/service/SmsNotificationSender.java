package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.dto.NotificationDeliveryEvent;
import com.aeroflex.notificationservice.enums.NotificationChannel;
import com.aeroflex.notificationservice.enums.SmsDelivery;
import com.aeroflex.notificationservice.enums.SmsRecipient;
import org.springframework.stereotype.Component;

@Component
public class SmsNotificationSender
        implements NotificationChannelSender<SmsDelivery> {

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.SMS;
    }

    @Override
    public void send(SmsDelivery delivery) {


            throw new RuntimeException(
                    "SMS provider temporarily unavailable"
            );

//        System.out.println(
//                "Sending SMS for notification: "
//                        + delivery.context().notificationId()
//        );
//
//        System.out.println(
//                "Sending SMS to: "
//                        + delivery.recipient().mobileNumber()
//        );
//
//        System.out.println(
//                "Sending SMS: "
//                        + delivery.content()
//        );
    }
}
