package com.iot.environment_monitor.service;

import com.iot.environment_monitor.model.Reading;
import com.iot.environment_monitor.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReadingService {

  private final ReadingRepository readingRepository;

  @Autowired
  public ReadingService (ReadingRepository readingRepository) {
    this.readingRepository = readingRepository;
  }

  public Reading save(Reading reading) {
    // tu możesz w przyszłości dodać walidację.


    return readingRepository.save(reading);
  }

}
