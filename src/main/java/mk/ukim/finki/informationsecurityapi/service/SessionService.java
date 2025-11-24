package mk.ukim.finki.informationsecurityapi.service;

import mk.ukim.finki.informationsecurityapi.api.dto.SessionDTO;
import mk.ukim.finki.informationsecurityapi.domain.Session;
import mk.ukim.finki.informationsecurityapi.domain.User;

import java.util.List;

public interface SessionService {

    SessionDTO createSessionForUser(User user);

    List<Session> getAllActiveSessions();

}
