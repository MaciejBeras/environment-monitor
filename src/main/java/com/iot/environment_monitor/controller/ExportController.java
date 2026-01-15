package com.iot.environment_monitor.controller;

import com.iot.environment_monitor.service.ExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;

@RestController
public class ExportController {

  private final ExportService exportService;

  public ExportController(ExportService exportService) {
    this.exportService = exportService;
  }

  @GetMapping("/export/xml")
  public ResponseEntity<byte[]> exportXml() throws Exception {
    File file = exportService.exportToXml("readings.xml");
    byte[] content = Files.readAllBytes(file.toPath());

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=readings.xml")
        .contentType(MediaType.APPLICATION_XML)
        .body(content);
  }
}