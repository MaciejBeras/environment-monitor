package com.iot.environment_monitor.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "readings")
public class ReadingList {

  private List<Reading> readings;

  public ReadingList() {
  }

  public ReadingList(List<Reading> readings) {
    this.readings = readings;
  }

  @XmlElement(name = "reading")
  public List<Reading> getReadings() {
    return readings;
  }

  public void setReadings(List<Reading> readings) {
    this.readings = readings;
  }
}