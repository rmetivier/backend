package rm.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.backend.model.Categorie;
import rm.backend.model.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginAndPwd(String login, String password);
}
