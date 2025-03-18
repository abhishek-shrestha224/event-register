package world.hello.event_register.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  String uploadFile(MultipartFile file);

  Resource downloadFile(String filename);

  MultipartFile validateFile(MultipartFile file);

  void deleteFile(String filename);
}