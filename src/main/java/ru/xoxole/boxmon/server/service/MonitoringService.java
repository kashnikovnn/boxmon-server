package ru.xoxole.boxmon.server.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.xoxole.boxmon.server.bot.BoxMonTelegramBot;
import ru.xoxole.boxmon.server.monitoring.HumidityMonitoring;
import ru.xoxole.boxmon.server.monitoring.Monitoring;
import ru.xoxole.boxmon.server.monitoring.MonotoringStatus;
import ru.xoxole.boxmon.server.monitoring.TemperatureMonitoring;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class MonitoringService {

    private final BoxMonTelegramBot boxMonTelegramBot;
    private final EspDataService espDataService;

    private static List<Monitoring> monitorings = new ArrayList<>();

    public MonitoringService(BoxMonTelegramBot boxMonTelegramBot, EspDataService espDataService) {
        this.boxMonTelegramBot = boxMonTelegramBot;
        this.espDataService = espDataService;
    }


    @Scheduled(fixedDelay = 300000)
    public void checkMonitorings() {
        monitorings.forEach(Monitoring::doCheck);
    }

    public static void registerMonitoring(Monitoring monitoring){
        monitorings.add(monitoring);
    }


}
