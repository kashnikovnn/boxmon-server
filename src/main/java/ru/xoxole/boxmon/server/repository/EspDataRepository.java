package ru.xoxole.boxmon.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.xoxole.boxmon.server.model.EspData;

import java.util.Date;
import java.util.List;


public interface EspDataRepository extends JpaRepository<EspData,Long> {

    List<EspData> findAllByCreateDateAfter(Date afterDate);
}
