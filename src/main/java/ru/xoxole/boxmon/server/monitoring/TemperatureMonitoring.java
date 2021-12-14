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
public class TemperatureMonitoring extends Monitoring {


    private Double minTemp;

    private Double maxTemp;

    public TemperatureMonitoring(EspDataService espDataService, BoxMonTelegramBot boxMonTelegramBot, MonitoringParametersService monitoringParametersService) {
        super(espDataService, boxMonTelegramBot, monitoringParametersService);
        super.setName("TemperatureMonitoring");
    }

    @PostConstruct
    protected void init() {
        MonitoringParameters monitoringParameters = monitoringParametersService.getMonitoringParameters();
        minTemp = monitoringParameters.getMinTemperature();
        maxTemp = monitoringParameters.getMaxTemperature();
        register();
    }

    @Override
    public void doCheck() {

        boolean isOk = false;

        if (status.equals(MonotoringStatus.PROBLEM)) {
            Double temp = espDataService.getLastEspData().getTemperature();
            isOk = temp < maxTemp && temp > minTemp;
        }

        if (status.equals(MonotoringStatus.OK)) {
            List<EspData> espDataList = super.espDataService.getByLastHours(1);
            isOk = espDataList
                    .stream().parallel()
                    .map(espData -> espData.getTemperature())
                    .allMatch(temp -> temp < maxTemp && temp > minTemp);
        }

        String normParamsDesc = normParamsDesc();
        if (isOk) {
            if (status.equals(MonotoringStatus.PROBLEM)) {
                status = MonotoringStatus.OK;
                boxMonTelegramBot.sendMessageToPrivateChat(getName() + ": " +status +"\nТемпература в норме. \nТекущая температура: "
                        + espDataService.getLastEspData().getTemperature() + normParamsDesc);
            }
        } else {
            if (status.equals(MonotoringStatus.OK)) {
                status = MonotoringStatus.PROBLEM;
                boxMonTelegramBot.sendMessageToPrivateChat(getName() + ": " +status + "\nТемпература не в норме течении часа! \nТекущая температура: "
                        + espDataService.getLastEspData().getTemperature() + normParamsDesc);
            }
        }
        log.info("Check temperature. Status:" + status);

    }

    @Override
    public String getTextStatus() {
        String normParamsDesc = normParamsDesc();
        Double temp = espDataService.getLastEspData().getTemperature();
        if (status.equals(MonotoringStatus.OK)) {
            return getName() + ": " +status + "\nТемпература в норме. \nТекущая температура: " + temp + normParamsDesc;
        }
        if (status.equals(MonotoringStatus.PROBLEM)) {
            return getName() + ": " + status + "\nТемпература не в норме течении часа! \nТекущая температура: " + temp + normParamsDesc;
        }
        return status.toString();
    }

    private String normParamsDesc() {
        return "\nМинимально допустимая: " + minTemp + "\nМаксимально допустимая: " + maxTemp;
    }

}
