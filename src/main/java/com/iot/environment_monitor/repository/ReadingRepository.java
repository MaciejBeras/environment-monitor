package com.iot.environment_monitor.repository;

import com.iot.environment_monitor.model.Reading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReadingRepository extends JpaRepository<Reading, Long> {

  // Najnowszy odczyt (nie filtrujemy po deviceId — jedna stacja)
  Optional<Reading> findTopByOrderByTimestampDesc();

  // Odczyty z ostatniego okresu (rosnąco po czasie — wygodne do wykresów)
  List<Reading> findByTimestampAfterOrderByTimestampAsc(LocalDateTime since);
}
