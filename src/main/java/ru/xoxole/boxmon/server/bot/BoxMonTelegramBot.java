package ru.xoxole.boxmon.server.bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.xoxole.boxmon.server.service.EspDataService;
import ru.xoxole.boxmon.server.service.MonitoringService;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class BoxMonTelegramBot extends TelegramLongPollingBot {

    private final String botUserName;

    private final String token;

    private final String privateChatId;

    private final EspDataService espDataService;

    private final MonitoringService monitoringService;

    public BoxMonTelegramBot(@Value("${telegram.bot.username}")
                                     String botUserName,
                             @Value("${telegram.bot.token}")
                                     String token,
                             @Value("${telegram.privatechat.id}")
                                     String privateChatId, EspDataService espDataService, MonitoringService monitoringService) {
        this.botUserName = botUserName;
        this.token = token;
        this.privateChatId = privateChatId;
        this.espDataService = espDataService;
        this.monitoringService = monitoringService;
    }



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

            if (update.getMessage().getText().trim().equals("/start")) {
                sendMenu();
            }

            if (update.getMessage().getText().trim().equals("/status")) {
                getStatus();
            }

            if (update.getMessage().getText().trim().equals("/monitorings")) {
                getAllMonitoringsReport();
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

    public void getAllMonitoringsReport() {
        monitoringService.getAllMonitoringsReports()
                .forEach(this::sendMessageToPrivateChat);
    }

    @SneakyThrows
    private void sendMenu(){
        SendMessage sendMessage = new SendMessage();
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add("/start");
        keyboardFirstRow.add("/status");
        keyboardFirstRow.add("/monitorings");

        // Вторая строчка клавиатуры
//        KeyboardRow keyboardSecondRow = new KeyboardRow();
//        // Добавляем кнопки во вторую строчку клавиатуры
//        keyboardSecondRow.add("Команда 3");
//        keyboardSecondRow.add("Команда 4");

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
       // keyboard.add(keyboardSecondRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);

        sendMessage.setChatId(privateChatId);
        sendMessage.setText("Menu");
        execute(sendMessage);
    }
}
