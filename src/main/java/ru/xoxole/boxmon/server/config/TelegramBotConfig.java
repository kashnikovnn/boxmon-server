package ru.xoxole.boxmon.server.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
public class TelegramBotConfig {


    // Имя бота заданное при регистрации
    @Value("${telegram.bot.username}")
    String botUserName;

    // Токен полученный при регистрации
    @Value("${telegram.bot.token}")
    String token;

}
