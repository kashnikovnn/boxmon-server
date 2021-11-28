package ru.xoxole.boxmon.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.xoxole.boxmon.server.model.EspData;
import ru.xoxole.boxmon.server.repository.EspDataRepository;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class EspDataService {

    private final EspDataRepository espDataRepository;

    public void saveEspData(EspData espData){
        espDataRepository.save(espData);
    }

    public List<EspData> getByLastHours(Integer hours){
        Date date = new Date(new Date().getTime() - hours*60*60*1000);
        return espDataRepository.findAllByCreateDateAfter(date);
    }
}
