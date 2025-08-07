package com.iot.environment_monitor.repository;

import com.iot.environment_monitor.model.Reading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingRepository extends JpaRepository <Reading, Long> {

}
