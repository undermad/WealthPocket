package ectimel.commands.handlers;

import ectimel.commands.Authenticate;
import ectimel.cqrs.commands.Handler;
import ectimel.cqrs.commands.ResultCommandHandler;
import ectimel.dto.LoginResponse;
import ectimel.services.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Handler
public class AuthenticateHandler implements ResultCommandHandler<Authenticate, LoginResponse> {

    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    public AuthenticateHandler(JwtProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public LoginResponse send(Authenticate command) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                command.email(),
                command.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new LoginResponse(jwtProvider.generateToken(command.email()), "Bearer");
    }

}
