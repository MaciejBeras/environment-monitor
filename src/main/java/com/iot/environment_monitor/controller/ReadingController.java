package com.iot.environment_monitor.controller;

import com.iot.environment_monitor.model.Reading;
import com.iot.environment_monitor.service.ReadingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readings")
@CrossOrigin
public class ReadingController {

  private final ReadingService service;

  public ReadingController(ReadingService service) {
    this.service = service;
  }

  // Wemos POSTuje tutaj JSON
  @PostMapping
  public Reading save(@RequestBody Reading reading) {
    return service.save(reading);
  }

  // Najnowszy odczyt
  @GetMapping("/latest")
  public ResponseEntity<Reading> latest() {
    return service.latest()
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.noContent().build()); // 204 gdy brak danych
  }

  // Ostatnie 6 godzin (na wykres)
  @GetMapping("/last6h")
  public List<Reading> last6h() {
    return service.last6h();
  }

}
