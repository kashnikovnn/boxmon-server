package ru.xoxole.boxmon.server.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Time;
import java.util.Date;

@Data
@Entity
@Table(name = "esp_data")
public class EspData {

    @Id
    @GeneratedValue
    private  Long id;
    private Double temperature;
    private Double humidity;
    private Boolean heater;
    private Boolean light;
    private Boolean vape;
    private Boolean fan;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private Time time;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    @CreationTimestamp
    private Date createDate;

}
