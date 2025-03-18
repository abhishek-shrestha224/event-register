package world.hello.event_register.service;

import java.util.Map;

public interface PdfService {
  byte[] generatePdf(Map<String, Object> variables, String imageFileName);
}