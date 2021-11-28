package ru.xoxole.boxmon.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.xoxole.boxmon.server.model.EspData;
import ru.xoxole.boxmon.server.service.EspDataService;

import java.util.List;

@RestController
@RequestMapping(("/espdata"))
@AllArgsConstructor
public class EspDataController {


    private final EspDataService espDataService;

    @GetMapping("/getByLastHours")
    public List<EspData> findEspData(Integer hours) {
        return espDataService.getByLastHours(hours);
    }

    @PostMapping("/save")
    public void saveEspData(@RequestBody EspData espData){
            espDataService.saveEspData(espData);
    }
}
