package mk.ukim.finki.informationsecurityapi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_verification")
@Getter
@Setter
public class OTPVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String otp;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;

    @Column(nullable = false)
    private boolean used = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
