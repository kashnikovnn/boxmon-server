package ru.xoxole.boxmon.server.monitoring;

import lombok.Getter;
import lombok.Setter;
import ru.xoxole.boxmon.server.bot.BoxMonTelegramBot;
import ru.xoxole.boxmon.server.service.EspDataService;
import ru.xoxole.boxmon.server.service.MonitoringParametersService;
import ru.xoxole.boxmon.server.service.MonitoringService;


public abstract class Monitoring {

    public Monitoring(EspDataService espDataService, BoxMonTelegramBot boxMonTelegramBot, MonitoringParametersService monitoringParametersService) {
        this.espDataService = espDataService;
        this.boxMonTelegramBot = boxMonTelegramBot;
        this.monitoringParametersService = monitoringParametersService;
    }

    final EspDataService espDataService;

    final BoxMonTelegramBot boxMonTelegramBot;

    final MonitoringParametersService monitoringParametersService;

    @Getter
    @Setter
    private String name = "Monitoring";

    @Getter
    @Setter
    MonotoringStatus status = MonotoringStatus.OK;

    public abstract void doCheck();

    protected void register() {
        MonitoringService.registerMonitoring(this);
    }

    public abstract String getTextStatus();

}
