package ru.xoxole.boxmon.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.xoxole.boxmon.server.model.EspData;

import java.util.Date;
import java.util.List;


public interface EspDataRepository extends JpaRepository<EspData,Long> {

    List<EspData> findAllByCreateDateAfter(Date afterDate);


    @Query("SELECT esp_data FROM EspData esp_data where esp_data.createDate=(select max(esp_data.createDate) from esp_data)")
    List<EspData> getLastEspData();
}
