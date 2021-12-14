package ru.xoxole.boxmon.server.monitoring;


import lombok.extern.slf4j.Slf4j;
import ru.xoxole.boxmon.server.bot.BoxMonTelegramBot;
import ru.xoxole.boxmon.server.model.EspData;
import ru.xoxole.boxmon.server.service.EspDataService;

import java.util.List;

@Slf4j
public class HumidityMonitoring extends Monitoring{

    private final Double minHym;

    private final Double maxHym;

    public HumidityMonitoring(EspDataService espDataService, BoxMonTelegramBot boxMonTelegramBot, MonotoringStatus status, Double minTemp, Double maxTemp) {
        super(espDataService, boxMonTelegramBot, status);
        this.minHym = minTemp;
        this.maxHym = maxTemp;
    }

    @Override
    public void doCheck() {
        List<EspData> espDataList =  super.espDataService.getByLastHours(1);
        boolean isOk = espDataList
                .stream().parallel()
                .map(espData -> espData.getHumidity())
                .allMatch(temp -> temp< maxHym && temp> minHym);
        if (isOk){
            if (status.equals(MonotoringStatus.PROBLEM)){
                status = MonotoringStatus.OK;
                boxMonTelegramBot.sendMessageToPrivateChat(status + " Влажность в норме. Текущая Влажность: "
                        + espDataService.getLastEspData().getTemperature());
            }
        }else {
            if (status.equals(MonotoringStatus.OK)){
                status = MonotoringStatus.PROBLEM;
                boxMonTelegramBot.sendMessageToPrivateChat(status + " Влажность не в норме! Текущая Влажность: "
                        + espDataService.getLastEspData().getTemperature());
            }
        }
        log.info("Check humidity. Status:"+status);

    }

}
