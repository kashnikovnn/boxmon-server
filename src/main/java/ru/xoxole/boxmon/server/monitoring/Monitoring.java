package ru.xoxole.boxmon.server.monitoring;

import ru.xoxole.boxmon.server.bot.BoxMonTelegramBot;
import ru.xoxole.boxmon.server.service.EspDataService;
import ru.xoxole.boxmon.server.service.MonitoringService;


public abstract class Monitoring {

    public Monitoring(EspDataService espDataService, BoxMonTelegramBot boxMonTelegramBot) {
        this.espDataService = espDataService;
        this.boxMonTelegramBot = boxMonTelegramBot;
    }

    final EspDataService espDataService;

    final BoxMonTelegramBot boxMonTelegramBot;

    MonotoringStatus status = MonotoringStatus.OK;

    public abstract void doCheck();

    protected void register() {
        MonitoringService.registerMonitoring(this);
    }
}
