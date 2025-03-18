package world.hello.event_register.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "upload")
@Getter
@Setter
public class ResourceConfig {
  private Path directory;
  private List<String> allowedExtensions;
  private long maxFileSize;
}