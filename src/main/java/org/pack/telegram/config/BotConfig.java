package org.pack.telegram.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class BotConfig {

    @Value("${bot.name}")
    String nameBot;

    @Value("${bot.token}")
    String tokenBot;

    public String getNameBot() {
        return nameBot;
    }

    public String getTokenBot() {
        return tokenBot;
    }
}
