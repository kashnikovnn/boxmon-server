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

    private Double minHym;

    private Double maxHym;


    public HumidityMonitoring(EspDataService espDataService, BoxMonTelegramBot boxMonTelegramBot, MonitoringParametersService monitoringParametersService) {
        super(espDataService, boxMonTelegramBot, monitoringParametersService);
        super.setName("HumidityMonitoring");
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
        if (status.equals(MonotoringStatus.PROBLEM)) {
            Double humidity = espDataService.getLastEspData().getHumidity();
            isOk = humidity < maxHym && humidity > minHym;
        }

        if (status.equals(MonotoringStatus.OK)) {
            List<EspData> espDataList = super.espDataService.getByLastHours(1);
            isOk = espDataList
                    .stream().parallel()
                    .map(espData -> espData.getHumidity())
                    .allMatch(hymidity -> hymidity < maxHym && hymidity > minHym);
        }

        String normParamsDesc = normParamsDesc();
        if (isOk) {
            if (status.equals(MonotoringStatus.PROBLEM)) {
                status = MonotoringStatus.OK;
                boxMonTelegramBot.sendMessageToPrivateChat(getName() + ": " + status +"\nВлажность в норме. \nТекущая Влажность: "
                        + espDataService.getLastEspData().getHumidity() + normParamsDesc
                );
            }
        } else {
            if (status.equals(MonotoringStatus.OK)) {
                status = MonotoringStatus.PROBLEM;
                boxMonTelegramBot.sendMessageToPrivateChat(getName() + ": " + status + "\nВлажность не в норме в течении часа! \nТекущая Влажность: "
                        + espDataService.getLastEspData().getHumidity() + normParamsDesc);
            }
        }
        log.info("Check humidity. Status:" + status);

    }

    @Override
    public String getTextStatus() {
        String normParamsDesc = normParamsDesc();
        Double hymidity = espDataService.getLastEspData().getHumidity();
        if (status.equals(MonotoringStatus.OK)) {
            return getName() + ": " + status + "\nВлажность в норме. \nТекущая Влажность: " + hymidity + normParamsDesc;
        }
        if (status.equals(MonotoringStatus.PROBLEM)) {
            return getName() + ": " + status + "\nВлажность не в норме в течении часа! \nТекущая Влажность: "
                    + hymidity + normParamsDesc;
        }
        return status.toString();
    }

    private String normParamsDesc() {
        return "\nМинимально допустимая: " + minHym + "\nМаксимально допустимая: " + maxHym;
    }

}
