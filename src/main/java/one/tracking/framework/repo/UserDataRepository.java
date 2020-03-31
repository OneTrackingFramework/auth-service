/**
 *
 */
package one.tracking.framework.repo;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import one.tracking.framework.entity.UserData;

/**
 * @author Marko Voß
 *
 */
public interface UserDataRepository extends CrudRepository<UserData, String> {

  Optional<UserData> findByEmail(String email);

  Optional<UserData> findByUserId(String id);

  boolean existsByEmail(String email);

  boolean existsByUserId(String id);
}
