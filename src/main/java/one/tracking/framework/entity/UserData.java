/**
 *
 */
package one.tracking.framework.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
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
@Table(indexes = {
    @Index(name = "IDX_USERID", columnList = "userId"),
    @Index(name = "IDX_EMAIL", columnList = "email"),
})
public class UserData {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid")
  @Column(updatable = false, nullable = false, unique = true, length = 32)
  private String id;

  @Column(length = 32, nullable = false, unique = true)
  private String userId;

  @Column(length = 256, nullable = false)
  private String email;

  @Column(length = 256, nullable = false)
  private String encryptedPassword;
}
