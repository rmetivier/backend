package rm.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.backend.model.Categorie;


public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    //List<Categorie> findCategoriesByOrdre();
}
