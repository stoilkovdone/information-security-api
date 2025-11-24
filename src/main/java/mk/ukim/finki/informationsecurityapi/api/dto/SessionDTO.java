package mk.ukim.finki.informationsecurityapi.api.dto;

import java.time.Duration;

public record SessionDTO(String token,
                         Duration validForHours) {
}
