package ru.xoxole.boxmon.server.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "monitoring_parameters")
public class MonitoringParameters {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "min_temperature")
    private Double MinTemperature;

    @Column(name = "max_temperature")
    private Double MaxTemperature;

    @Column(name = "min_humidity")
    private Double MinHumidity;

    @Column(name = "max_humidity")
    private Double MaxHumidity;

    @Column(name = "no_data_minutes")
    private Integer noDataMinutes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    @UpdateTimestamp
    private Date createDate;
}
