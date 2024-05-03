package rm.backend.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rm.backend.bean.Login;
import rm.backend.model.Categorie;
import rm.backend.model.Favori;
import rm.backend.repository.CategorieRepository;
import rm.backend.repository.FavoriRepository;
import rm.backend.services.DBService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
    @RequestMapping("/api")

    public class MainController {

        Logger logger = LogManager.getLogger(MainController.class);
        @Autowired
        CategorieRepository categorieRepository;
        @Autowired
        FavoriRepository favoriRepository;
        @Autowired
        DBService dbService;

        @GetMapping("/home")
        public String home(Model model) {
            return "hello world";
        }
        @PostMapping("/login")
        public ResponseEntity<List<Categorie>> login(@RequestBody Login login) {
            return new ResponseEntity<>(dbService.getCategories(), HttpStatus.OK);
        }
        @GetMapping("/initDB")
        public ResponseEntity<List<Favori>> initDb() {
            dbService.initDB();
            logger.error(">>> chargement des favoris") ;
            return new ResponseEntity<>(dbService.getFavoris(), HttpStatus.OK);
        }
        @GetMapping("/favoris")
        public ResponseEntity<List<Favori>> getFavoris() {
            logger.error(">>> chargement des favoris") ;
            return new ResponseEntity<>(dbService.getFavoris(), HttpStatus.OK);
        }
        @PostMapping("/favoris")
        public  ResponseEntity<List<Favori>>  createFavori(@RequestBody Favori favori) {
            Optional<Favori> ofavoriDB = favoriRepository.findById(Long.valueOf(favori.getId()));
            if (ofavoriDB.isPresent()){
                dbService.updateFavori(ofavoriDB.get(),favori);
            }else{
                favoriRepository.save(new Favori(favori.getIdCategorie(),favori.getTitle(), favori.getDescription(), favori.getOrdre(), favori.getUrl()));
            }
            return new ResponseEntity<>(dbService.getFavoris(), HttpStatus.OK);
        }

    @DeleteMapping("/favoris")
    public  ResponseEntity<List<Favori>>  deleteFavori(@RequestBody Favori favori) {
        Optional<Favori> ofavoriDB = favoriRepository.findById(Long.valueOf(favori.getId()));
        if (ofavoriDB.isPresent()){
            favoriRepository.deleteById(Long.valueOf(favori.getId()));
        }
        return new ResponseEntity<>(dbService.getFavoris(), HttpStatus.OK);
    }

        @GetMapping("/categories")
        public ResponseEntity<List<Categorie>> getAllCategories() {
            return new ResponseEntity<>(dbService.getCategories(), HttpStatus.OK);
        }
        @PostMapping("/categories")
        public  ResponseEntity<Categorie>  createCategorie(@RequestBody Categorie categorie) {
            try {
                Categorie _categorie = categorieRepository.save(new Categorie(categorie.getTitle(),categorie.getOrdre()));
                return new ResponseEntity<>(_categorie, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
}
