/**
 *
 */
package one.tracking.framework.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Marko Voß
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
// @Table(indexes = {
// @Index(name = "IDX_USERID", columnList = "userId"),
// })
public class UserData {

  @Id
  @Column(length = 32, nullable = false, unique = true)
  private String userId;

  @Column(length = 256, nullable = false)
  private String encryptedEmail;

  @Column(length = 256, nullable = false)
  private String encryptedPassword;
}
