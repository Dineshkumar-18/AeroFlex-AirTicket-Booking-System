package com.aeroflex.notificationservice.service;

import com.aeroflex.common.notification.attachment.Attachment;
import com.aeroflex.notificationservice.dto.NotificationDeliveryEvent;
import com.aeroflex.notificationservice.enums.EmailDelivery;
import com.aeroflex.notificationservice.enums.EmailRecipient;
import com.aeroflex.notificationservice.enums.NotificationChannel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

@Component
@RequiredArgsConstructor
public class EmailNotificationSender
        implements  NotificationChannelSender<EmailDelivery> {

    private final JavaMailSender mailSender;

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public void send(EmailDelivery delivery) {

        EmailRecipient recipient = delivery.recipient();

        System.out.println(
                "Sending email to: " + recipient.to()
        );

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            StandardCharsets.UTF_8.name()
                    );

            // TO
            helper.setTo(recipient.to().toArray(new String[0]));

            // CC
            if (recipient.cc() != null &&
                    !recipient.cc().isEmpty()) {

                helper.setCc(
                        recipient.cc().toArray(
                                new String[0]
                        )
                );
            }

            // BCC
            if (recipient.bcc() != null &&
                    !recipient.bcc().isEmpty()) {

                helper.setBcc(
                        recipient.bcc().toArray(
                                new String[0]
                        )
                );
            }

            // Subject
            helper.setSubject(
                    delivery.subject()
            );

            // HTML content
            helper.setText(
                    delivery.content(),
                    true
            );

            // Attachments
            List<Attachment> attachments =
                    delivery.attachments();

            if (attachments != null) {

                for (Attachment attachment : attachments) {

                    helper.addAttachment(
                            attachment.fileName(),
                            new ByteArrayResource(attachment.content())
                    );
                }
            }

            mailSender.send(message);

            System.out.println(
                    "Email sent successfully to: "
                            + recipient.to()
            );

        } catch (MessagingException ex) {

            throw new RuntimeException(
                    "Failed to send email to "
                            + recipient.to(),
                    ex
            );
        }
    }

}