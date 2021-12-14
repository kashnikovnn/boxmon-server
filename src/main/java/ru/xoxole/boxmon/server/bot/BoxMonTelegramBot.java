package ru.xoxole.boxmon.server.bot;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.xoxole.boxmon.server.service.EspDataService;

@Component
@Slf4j
public class BoxMonTelegramBot extends TelegramLongPollingBot {

    private final String botUserName;

    private final String token;

    private final String privateChatId;

    public BoxMonTelegramBot(@Value("${telegram.bot.username}")
                                     String botUserName,
                             @Value("${telegram.bot.token}")
                                     String token,
                             @Value("${telegram.privatechat.id}")
                                     String privateChatId, EspDataService espDataService) {
        this.botUserName = botUserName;
        this.token = token;
        this.privateChatId = privateChatId;
        this.espDataService = espDataService;
    }

    private final EspDataService espDataService;

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Get message");
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            String chatId = update.getMessage().getChatId().toString();

            if (update.getMessage().getText().trim().equals("/status")) {
                getStatus();
            }

        }

    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @SneakyThrows
    public void sendMessageToPrivateChat(String message) {
        SendMessage sm = new SendMessage();
        sm.setChatId(privateChatId);
        sm.setText(message);
        execute(sm);
    }

    public void getStatus() {
        sendMessageToPrivateChat(espDataService.getLastEspData().toString());
    }
}
