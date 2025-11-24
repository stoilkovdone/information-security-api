package mk.ukim.finki.informationsecurityapi.config;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.informationsecurityapi.domain.Session;
import mk.ukim.finki.informationsecurityapi.service.SessionService;
import mk.ukim.finki.informationsecurityapi.service.helper.AuthenticationHelper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class Bootstrap implements CommandLineRunner {

    private final SessionService sessionService;

    @Override
    public void run(String... args) {
        loadActiveUserSessions();
    }

    private void loadActiveUserSessions() {
        Map<String, Session> activeSessions = sessionService.getAllActiveSessions().stream()
                .collect(Collectors.toMap(Session::getToken, Function.identity()));

        AuthenticationHelper.setActiveSessions(activeSessions);
    }
}
