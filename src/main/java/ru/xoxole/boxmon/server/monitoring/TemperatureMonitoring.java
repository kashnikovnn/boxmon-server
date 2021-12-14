package ru.xoxole.boxmon.server.monitoring;


import lombok.extern.slf4j.Slf4j;
import ru.xoxole.boxmon.server.bot.BoxMonTelegramBot;
import ru.xoxole.boxmon.server.model.EspData;
import ru.xoxole.boxmon.server.service.EspDataService;

import java.util.List;

@Slf4j
public class TemperatureMonitoring extends Monitoring{

    private final Double minTemp;

    private final Double maxTemp;

    public TemperatureMonitoring(EspDataService espDataService, BoxMonTelegramBot boxMonTelegramBot, MonotoringStatus status, Double minTemp, Double maxTemp) {
        super(espDataService, boxMonTelegramBot, status);
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    @Override
    public void doCheck() {
        List<EspData> espDataList =  super.espDataService.getByLastHours(1);
        boolean isOk = espDataList
                .stream().parallel()
                .map(espData -> espData.getTemperature())
                .allMatch(temp -> temp<maxTemp && temp>minTemp);
        if (isOk){
            if (status.equals(MonotoringStatus.PROBLEM)){
                status = MonotoringStatus.OK;
                boxMonTelegramBot.sendMessageToPrivateChat(status + " Температура в норме. Текущая температура: "
                        + espDataService.getLastEspData().getTemperature());
            }
        }else {
            if (status.equals(MonotoringStatus.OK)){
                status = MonotoringStatus.PROBLEM;
                boxMonTelegramBot.sendMessageToPrivateChat(status + " Температура не в норме! Текущая температура: "
                        + espDataService.getLastEspData().getTemperature());
            }
        }
        log.info("Check temperature. Status:"+status);

    }

}
