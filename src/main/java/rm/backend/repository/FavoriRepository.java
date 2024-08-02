package rm.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.backend.model.Categorie;
import rm.backend.model.Favori;
import rm.backend.model.User;

import java.util.List;
import java.util.Optional;


public interface FavoriRepository extends JpaRepository<Favori, Long> {
    //List<Categorie> findCategoriesByOrdre();

    //List<Tutorial> findByPublished(boolean published);
    List<Favori> findByIdUser(long idUser);

    //Optional<User> findByLoginAndPwd(String login, String password);


}
