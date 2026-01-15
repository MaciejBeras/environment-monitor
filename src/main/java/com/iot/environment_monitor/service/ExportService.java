package com.iot.environment_monitor.service;

import com.iot.environment_monitor.model.ReadingList;
import com.iot.environment_monitor.model.Reading;
import com.iot.environment_monitor.repository.ReadingRepository;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
@Service
public class ExportService {

  private final ReadingRepository readingRepository;

  public ExportService(ReadingRepository readingRepository) {
    this.readingRepository = readingRepository;
  }

  public File exportToXml(String filePath) throws Exception {
    List<Reading> readings = readingRepository.findAll();
    ReadingList wrapper = new ReadingList(readings);

    JAXBContext context = JAXBContext.newInstance(ReadingList.class, Reading.class);
    Marshaller marshaller = context.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

    File file = new File(filePath);
    marshaller.marshal(wrapper, file);

    return file;
  }
}
