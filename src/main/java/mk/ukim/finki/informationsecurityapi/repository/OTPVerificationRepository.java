package mk.ukim.finki.informationsecurityapi.repository;

import mk.ukim.finki.informationsecurityapi.domain.OTPVerification;
import mk.ukim.finki.informationsecurityapi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPVerificationRepository extends JpaRepository<OTPVerification, Long> {

    Optional<OTPVerification> findByUserAndUsed(User user, boolean used);

}
