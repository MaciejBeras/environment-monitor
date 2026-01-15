package com.iot.environment_monitor.service;

import com.iot.environment_monitor.model.Reading;
import com.iot.environment_monitor.repository.ReadingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReadingService {

  private final ReadingRepository repo;

  public ReadingService(ReadingRepository repo) {
    this.repo = repo;
  }

  public Reading save(Reading reading) {
    return repo.save(reading);
  }

  // Najnowszy odczyt (Optional — może jeszcze nie być danych)
  public Optional<Reading> latest() {
    return repo.findTopByOrderByTimestampDesc();
  }

  // Ostatnie 6 godzin
  public List<Reading> last6h() {
    return lastHours(6);
  }

  // Wspólna metoda do dowolnej liczby godzin — przydatne na przyszłość
  public List<Reading> lastHours(int hours) {
    LocalDateTime since = LocalDateTime.now().minusHours(hours);
    return repo.findByTimestampAfterOrderByTimestampAsc(since);
  }
}
