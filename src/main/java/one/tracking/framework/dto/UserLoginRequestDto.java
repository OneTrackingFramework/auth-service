/**
 *
 */
package one.tracking.framework.dto;

import lombok.Data;

/**
 * @author Marko Voß
 *
 */
@Data
public class UserLoginRequestDto {

  private String email;
  private String password;
}
