package rm.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.backend.model.Categorie;
import rm.backend.model.Favori;

import java.util.List;


public interface FavoriRepository extends JpaRepository<Favori, Long> {
    //List<Categorie> findCategoriesByOrdre();

    //List<Tutorial> findByPublished(boolean published);
    //List<Tutorial> findByTitleContaining(String title);

}
