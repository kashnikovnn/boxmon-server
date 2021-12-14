package ru.xoxole.boxmon.server.monitoring;

import ru.xoxole.boxmon.server.bot.BoxMonTelegramBot;
import ru.xoxole.boxmon.server.service.EspDataService;

public class NoDataMonitoring extends Monitoring{
    public NoDataMonitoring(EspDataService espDataService, BoxMonTelegramBot boxMonTelegramBot, MonotoringStatus status) {
        super(espDataService, boxMonTelegramBot, status);
    }

    @Override
    public void doCheck() {

    }
}
