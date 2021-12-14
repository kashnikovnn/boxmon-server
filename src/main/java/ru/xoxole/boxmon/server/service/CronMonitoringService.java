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
public class CronMonitoringService {

    private final BoxMonTelegramBot boxMonTelegramBot;
    private final EspDataService espDataService;

    private List<Monitoring> monitorings = new ArrayList<>();

    public CronMonitoringService(BoxMonTelegramBot boxMonTelegramBot, EspDataService espDataService) {
        this.boxMonTelegramBot = boxMonTelegramBot;
        this.espDataService = espDataService;
    }

    @PostConstruct
    private void initMonitorings() {
        TemperatureMonitoring temperatureMonitoring = new TemperatureMonitoring(espDataService,
                boxMonTelegramBot,
                MonotoringStatus.OK,
                25d,
                29d);
        HumidityMonitoring humidityMonitoring = new HumidityMonitoring(espDataService,
                boxMonTelegramBot,
                MonotoringStatus.OK,
                35d,
                60d);
        monitorings.add(humidityMonitoring);
        monitorings.add(temperatureMonitoring);
    }

    @Scheduled(fixedDelay = 300000)
    public void checkMonitorings() {
        monitorings.forEach(Monitoring::doCheck);
    }


}
