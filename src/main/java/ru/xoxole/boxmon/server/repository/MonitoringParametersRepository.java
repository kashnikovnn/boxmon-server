package ru.xoxole.boxmon.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.xoxole.boxmon.server.model.MonitoringParameters;

import java.util.Optional;


public interface MonitoringParametersRepository extends JpaRepository<MonitoringParameters,Long> {

    @Override
    Optional<MonitoringParameters> findById(Long aLong);

    Optional<MonitoringParameters> readFirstByOrderByCreateDateDesc();

}
