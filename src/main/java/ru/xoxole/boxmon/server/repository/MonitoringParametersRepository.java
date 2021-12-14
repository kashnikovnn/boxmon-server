package ru.xoxole.boxmon.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.xoxole.boxmon.server.model.MonitoringParameters;


public interface MonitoringParametersRepository extends JpaRepository<MonitoringParameters,Long> {


}
