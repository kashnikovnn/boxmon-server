package ru.xoxole.boxmon.server.monitoring;

import org.springframework.stereotype.Component;
import ru.xoxole.boxmon.server.bot.BoxMonTelegramBot;
import ru.xoxole.boxmon.server.service.EspDataService;
import ru.xoxole.boxmon.server.service.MonitoringParametersService;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class NoDataMonitoring extends Monitoring{

    private Integer noDataMinutes = 60;

    public NoDataMonitoring(EspDataService espDataService, BoxMonTelegramBot boxMonTelegramBot, MonitoringParametersService monitoringParametersService) {
        super(espDataService, boxMonTelegramBot, monitoringParametersService);
        setName("NoDataMonitoring");
    }

    @PostConstruct
    public void init(){
        noDataMinutes = monitoringParametersService.getMonitoringParameters().getNoDataMinutes();
        register();
    }

    @Override
    public void doCheck() {
        MonotoringStatus prevMonotoringStatus = getStatus();
        String msgText = getTextStatus();
        if (!getStatus().equals(prevMonotoringStatus)){
            boxMonTelegramBot.sendMessageToPrivateChat(msgText);
        }
    }

    @Override
    public String getTextStatus() {
        Date lastDate = espDataService.getLastEspData().getCreateDate();
        String statusText;
        if(new Date().getTime() - lastDate.getTime() > noDataMinutes*60*1000){
            setStatus(MonotoringStatus.PROBLEM);
            statusText =  "Нет данных от датчика более " + noDataMinutes + " минут. \nПоследние данные были получены. " + lastDate;
        }else {
            setStatus(MonotoringStatus.OK);
            statusText = " Последние данные были получены " + lastDate;
        }

        return getName() +": "+ getStatus()+ "\n"+ statusText;
    }


}
