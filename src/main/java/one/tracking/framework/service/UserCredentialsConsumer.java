package one.tracking.framework.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import one.tracking.framework.entity.UserData;
import one.tracking.framework.kafka.events.UserCredentials;
import one.tracking.framework.repo.UserDataRepository;
import one.tracking.framework.util.JWTHelper;

@Service
public class UserCredentialsConsumer {

  @Autowired
  private UserDataRepository repository;

  @Autowired
  private JWTHelper jwtHelper;

  @Autowired
  private ObjectMapper mapper;

  @Value("${app.jwe.secret}")
  private String jweEncodedSecret;

  private static final Logger LOG = LoggerFactory.getLogger(UserCredentialsConsumer.class);

  @KafkaListener(topics = UserCredentials.TOPIC, containerFactory = "kafka.listener.UserCredentials")
  public void consume(final String event) throws Exception {
    LOG.debug("Received KAFKA event: {}", event);

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
