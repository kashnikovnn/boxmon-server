package ru.xoxole.boxmon.server.service;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.stereotype.Component;
import ru.xoxole.boxmon.server.model.MonitoringParameters;
import ru.xoxole.boxmon.server.repository.MonitoringParametersRepository;

import java.util.Optional;

@Component
@AllArgsConstructor
public class MonitoringParametersService {

    private final MonitoringParametersRepository monitoringParametersRepository;

    public MonitoringParameters getMonitoringParameters() {
        return  monitoringParametersRepository.readFirstByOrderByCreateDateDesc()
                .orElseGet(() -> createAndSaveDefaultMonitoringParameters());
    }

    private synchronized MonitoringParameters createAndSaveDefaultMonitoringParameters(){
        MonitoringParameters monitoringParameters = new MonitoringParameters();
        monitoringParameters.setMaxHumidity(60d);
        monitoringParameters.setMinHumidity(25d);
        monitoringParameters.setMaxTemperature(28d);
        monitoringParameters.setMinTemperature(24d);
        monitoringParameters.setNoDataMinutes(60);
        return monitoringParametersRepository.saveAndFlush(monitoringParameters);
    }
}