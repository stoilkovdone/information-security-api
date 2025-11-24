package mk.ukim.finki.informationsecurityapi.repository;

import mk.ukim.finki.informationsecurityapi.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findAllByExpirationDateAfter(LocalDateTime expirationDate);

}
