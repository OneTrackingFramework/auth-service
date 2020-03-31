package one.tracking.framework.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentials {

  public static final String TOPIC = "users";

  private String id;

  private String encrytedEmail;

  private String encrytedPassword;
}
