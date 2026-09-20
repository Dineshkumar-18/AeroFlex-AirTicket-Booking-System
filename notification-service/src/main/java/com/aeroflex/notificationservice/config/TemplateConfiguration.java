package com.aeroflex.notificationservice.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.nio.charset.StandardCharsets;

@Configuration
public class TemplateConfiguration {

    @Bean("emailTemplateEngine")
    public SpringTemplateEngine emailTemplateEngine(
            ApplicationContext applicationContext,
            NotificationTemplateProperties properties) {

        SpringTemplateEngine engine =
                new SpringTemplateEngine();

        SpringResourceTemplateResolver resolver =
                new SpringResourceTemplateResolver();

        resolver.setApplicationContext(applicationContext);

        resolver.setPrefix("classpath:/templates/email/");
        resolver.setSuffix(".html");

        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setCacheable(false);

        engine.setTemplateResolver(resolver);

        return engine;
    }

    @Bean("smsTemplateEngine")
    public SpringTemplateEngine smsTemplateEngine(
            ApplicationContext applicationContext,
            NotificationTemplateProperties properties) {

        SpringTemplateEngine engine = new SpringTemplateEngine();

        SpringResourceTemplateResolver resolver =
                new SpringResourceTemplateResolver();

        resolver.setApplicationContext(applicationContext);

        resolver.setPrefix("classpath:/templates/sms/");
        resolver.setSuffix(".txt");
        resolver.setTemplateMode(TemplateMode.TEXT);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setCacheable(false);

        engine.setTemplateResolver(resolver);

        return engine;
    }
}
