package world.hello.event_register.service;

import org.springframework.web.multipart.MultipartFile;
import world.hello.event_register.domain.dto.BadgeDto;
import world.hello.event_register.domain.enums.RegistrationType;

import java.util.List;
import java.util.UUID;

public interface BadgeService {
  BadgeDto createBadge(
      RegistrationType registrationType, String userEmail, UUID eventId, MultipartFile photo);

  List<BadgeDto> getBadgesByUserEmail(String userEmail);

  BadgeDto getBadgeByUserEmailAndId(String userEmail, UUID id);

  byte[] createBadgePdf(String userEmail, UUID badgeId);
}