package com.iot.environment_monitor.controller;

import com.iot.environment_monitor.model.Reading;
import com.iot.environment_monitor.service.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/readings")
@CrossOrigin
public class ReadingController {

  private final ReadingService readingService;

  @Autowired
  public ReadingController(ReadingService readingService) {
    this.readingService = readingService;
  }

  @PostMapping
  public Reading saveReading(@RequestBody Reading reading) {
    return readingService.save(reading);
  }
}