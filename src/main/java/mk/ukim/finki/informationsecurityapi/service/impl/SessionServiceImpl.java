package mk.ukim.finki.informationsecurityapi.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.informationsecurityapi.api.dto.SessionDTO;
import mk.ukim.finki.informationsecurityapi.domain.Session;
import mk.ukim.finki.informationsecurityapi.domain.User;
import mk.ukim.finki.informationsecurityapi.repository.SessionRepository;
import mk.ukim.finki.informationsecurityapi.service.SessionService;
import mk.ukim.finki.informationsecurityapi.service.helper.AuthenticationHelper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    @Override
    public SessionDTO createSessionForUser(User user) {
        Session session = new Session();

        session.setUser(user);

        String sessionToken = UUID.randomUUID().toString();
        session.setToken(sessionToken);

        session.setExpirationDate(LocalDateTime.now().plusHours(24));

        sessionRepository.save(session);

        AuthenticationHelper.addSession(sessionToken, session);

        return new SessionDTO(sessionToken, Duration.ofHours(24));
    }

    @Override
    public List<Session> getAllActiveSessions() {
        return sessionRepository.findAllByExpirationDateAfter(LocalDateTime.now());
    }

}
