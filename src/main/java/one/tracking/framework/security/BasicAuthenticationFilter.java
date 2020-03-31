/**
 *
 */
package one.tracking.framework.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.tracking.framework.dto.UserLoginRequestDto;
import one.tracking.framework.util.JWTHelper;

public class BasicAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JWTHelper jwtHelper;

  public BasicAuthenticationFilter(final AuthenticationManager authenticationManager, final JWTHelper jwtHelper) {
    this.authenticationManager = authenticationManager;
    this.jwtHelper = jwtHelper;

  }

  @Override
  public Authentication attemptAuthentication(final HttpServletRequest req, final HttpServletResponse res)
      throws AuthenticationException {

    try {
      final UserLoginRequestDto creds = new ObjectMapper().readValue(req.getInputStream(),
          UserLoginRequestDto.class);

      return this.authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(final HttpServletRequest req, final HttpServletResponse res,
      final FilterChain chain,
      final Authentication auth) throws IOException, ServletException {

    final String userId = (auth.getPrincipal() instanceof String) ? (String) auth.getPrincipal()
        : ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();

    final String token = this.jwtHelper.createJWT(userId, SecurityConstants.DEFAULT_EXPIRATION);

    res.addHeader(HttpHeaders.AUTHORIZATION, SecurityConstants.TOKEN_PREFIX + token);
    res.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
    try (PrintWriter writer = new PrintWriter(res.getOutputStream())) {
      writer.write(token);
    }
  }

}
