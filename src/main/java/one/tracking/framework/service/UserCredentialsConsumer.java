package one.tracking.framework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import one.tracking.framework.repo.UserDataRepository;

@Service
public class UserCredentialsConsumer {

  @Autowired
  private UserDataRepository repository;

  private static final Logger LOG = LoggerFactory.getLogger(UserCredentialsConsumer.class);

  // @KafkaListener(topics = UserCredentials.TOPIC, containerFactory =
  // "kafka.listener.UserCredentials")
  // public void consume(final UserCredentials event) {
  // LOG.debug("Received KAFKA event: {}", event);
  //
  // final Optional<UserData> userDataOp = this.repository.findByUserId(event.getId());
  //
  // if (userDataOp.isEmpty()) {
  //
  // this.repository.save(UserData.builder()
  // .encryptedEmail(event.getEncrytedEmail())
  // .encryptedPassword(event.getEncrytedPassword())
  // .userId(event.getId())
  // .build());
  //
  // } else {
  //
  // final UserData userData = userDataOp.get();
  // userData.setEncryptedEmail(event.getEncrytedEmail());
  // userData.setEncryptedPassword(event.getEncrytedPassword());
  // this.repository.save(userData);
  // }
  // }
}
