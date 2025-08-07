package com.iot.environment_monitor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "readings")

public class Reading {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String deviceId;
  private double temperature;
  private double humidity;
  private double pressure;

  private LocalDateTime timestamp;

  @PrePersist
  protected void onCreate() {
    this.timestamp = LocalDateTime.now();

  }
}
