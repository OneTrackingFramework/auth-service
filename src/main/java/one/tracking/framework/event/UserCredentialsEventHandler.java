/**
 *
 */
package one.tracking.framework.event;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.tracking.framework.entity.UserData;
import one.tracking.framework.kafka.consumer.handler.IUserCredentialsEventHandler;
import one.tracking.framework.kafka.events.UserCredentials;
import one.tracking.framework.repo.UserDataRepository;
import one.tracking.framework.util.JWTHelper;

/**
 * @author Marko Voß
 *
 */
@Component
public class UserCredentialsEventHandler implements IUserCredentialsEventHandler {

  @Autowired
  private UserDataRepository repository;

  @Autowired
  private JWTHelper jwtHelper;

  @Autowired
  private ObjectMapper mapper;

  @Value("${app.jwe.secret}")
  private String jweEncodedSecret;

  @Override
  public void consume(final String event) throws Exception {

    final String payload = this.jwtHelper.decodeJWE(this.jweEncodedSecret, event);
    final UserCredentials userCredentials = this.mapper.readValue(payload, UserCredentials.class);

    final Optional<UserData> userDataOp =
        this.repository.findByUserIdOrEmail(userCredentials.getUserId(), userCredentials.getEmail());

    if (userDataOp.isEmpty()) {

      this.repository.save(UserData.builder()
          .email(userCredentials.getEmail())
          .encryptedPassword(userCredentials.getEncrytedPassword())
          .userId(userCredentials.getUserId())
          .build());

    } else {

      final UserData userData = userDataOp.get();
      userData.setEmail(userCredentials.getEmail());
      userData.setEncryptedPassword(userCredentials.getEncrytedPassword());
      userData.setUserId(userCredentials.getUserId());
      this.repository.save(userData);
    }
  }
}
