/**
 *
 */
package one.tracking.framework.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Component;
import one.tracking.framework.entity.UserData;
import one.tracking.framework.repo.UserDataRepository;

/**
 * @author Marko Voß
 *
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private UserDataRepository userRepository;

  @Value("${app.encoder.secret}")
  private String encoderSecret;

  @Value("${app.encoder.iterations}")
  private int encoderIterations;

  @Value("${app.encoder.hashWidth}")
  private int encoderHashWidth;

  private final PasswordEncoder passwordEncoder;

  public CustomAuthenticationProvider(final PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

    final String email = authentication.getName();
    final String password = authentication.getCredentials().toString();

    final Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder(
        this.encoderSecret, this.encoderIterations, this.encoderHashWidth);

    final Optional<UserData> userOp = this.userRepository.findByEncryptedEmail(encoder.encode(email));
    if (userOp.isEmpty() || !this.passwordEncoder.matches(password, userOp.get().getEncryptedPassword()))
      throw new BadCredentialsException("Invalid credentials");

    final UserData user = userOp.get();
    final List<GrantedAuthority> authorities = new ArrayList<>();
    return new UsernamePasswordAuthenticationToken(user.getUserId(), password, authorities);
  }

  @Override
  public boolean supports(final Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
