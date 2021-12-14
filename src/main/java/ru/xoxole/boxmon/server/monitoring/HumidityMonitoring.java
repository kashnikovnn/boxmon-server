package ru.xoxole.boxmon.server.monitoring;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.xoxole.boxmon.server.bot.BoxMonTelegramBot;
import ru.xoxole.boxmon.server.model.EspData;
import ru.xoxole.boxmon.server.model.MonitoringParameters;
import ru.xoxole.boxmon.server.service.EspDataService;
import ru.xoxole.boxmon.server.service.MonitoringParametersService;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class HumidityMonitoring extends Monitoring {

    private final MonitoringParametersService monitoringParametersService;

    private Double minHym;

    private Double maxHym;

    public HumidityMonitoring(EspDataService espDataService, BoxMonTelegramBot boxMonTelegramBot, MonitoringParametersService monitoringParametersService) {
        super(espDataService, boxMonTelegramBot);
        this.monitoringParametersService = monitoringParametersService;
    }

    @PostConstruct
    protected void init() {
        MonitoringParameters monitoringParameters = monitoringParametersService.getMonitoringParameters();
        maxHym = monitoringParameters.getMaxHumidity();
        minHym = monitoringParameters.getMinHumidity();
        register();
    }

    @Override
    public void doCheck() {

        boolean isOk = false;
        if(status.equals(MonotoringStatus.PROBLEM)){
            Double humidity = espDataService.getLastEspData().getHumidity();
            isOk = humidity < maxHym && humidity > minHym;
        }

        if(status.equals(MonotoringStatus.OK)) {
            List<EspData> espDataList = super.espDataService.getByLastHours(1);
            isOk = espDataList
                    .stream().parallel()
                    .map(espData -> espData.getHumidity())
                    .allMatch(hymidity -> hymidity < maxHym && hymidity > minHym);
        }

        String normParamsDesc = "\nМинимально допустимая: " + minHym + "\n Максимально допустимая: " + maxHym;
        if (isOk) {
            if (status.equals(MonotoringStatus.PROBLEM)) {
                status = MonotoringStatus.OK;
                boxMonTelegramBot.sendMessageToPrivateChat(status + " Влажность в норме. \nТекущая Влажность: "
                        + espDataService.getLastEspData().getTemperature() + normParamsDesc
                );
            }
        } else {
            if (status.equals(MonotoringStatus.OK)) {
                status = MonotoringStatus.PROBLEM;
                boxMonTelegramBot.sendMessageToPrivateChat(status + " Влажность не в норме в течении часа! \nТекущая Влажность: "
                        + espDataService.getLastEspData().getTemperature() + normParamsDesc);
            }
        }
        log.info("Check humidity. Status:" + status);

    }

}
