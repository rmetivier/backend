package rm.backend.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rm.backend.controller.MainController;
import rm.backend.model.Categorie;
import rm.backend.model.Favori;
import rm.backend.model.User;
import rm.backend.repository.CategorieRepository;
import rm.backend.repository.FavoriRepository;
import rm.backend.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DBService {

    Logger logger = LogManager.getLogger(DBService.class);
    @Autowired
    CategorieRepository categorieRepository;
    @Autowired
    FavoriRepository favoriRepository;

    public  List<Categorie> getCategories(){
        List<Categorie> categories = new ArrayList<Categorie>();
        categorieRepository.findAll().forEach(categories::add);
        return categories;
    }

    public  List<Favori> getFavoris(long keyDbUserId){
        HashMap<String,Categorie> hmCategorie = new HashMap();
        List<Categorie>  categories =  getCategories();
        for ( Categorie categorie: categories  ) {
            hmCategorie.put(String.valueOf(categorie.getId()),categorie);
        }
        List<Favori> favoris = new ArrayList<Favori>();
        favoriRepository.findByIdUser(keyDbUserId).forEach(favoris::add);
        //favoriRepository.findAll().forEach(favoris::add);
        for (Favori favori: favoris) {
            favori.setLibelleCategorie(hmCategorie.get(String.valueOf(favori.getIdCategorie())).getTitle() );
            favori.setSeq((            hmCategorie.get(String.valueOf(favori.getIdCategorie())).getOrdre() * 100000) + favori.getOrdre() );
        }
        return favoris.stream().sorted(Comparator.comparingLong(Favori::getSeq)).collect(Collectors.toList());
    }

    public void updateFavori(Favori favoriSave, Favori favoriNewValue){
        favoriSave.setDescription(favoriNewValue.getDescription());
        favoriSave.setTitle(      favoriNewValue.getTitle() );
        favoriSave.setUrl(        favoriNewValue.getUrl());
        favoriSave.setIdCategorie(favoriNewValue.getIdCategorie());
        favoriSave.setOrdre(      favoriNewValue.getOrdre());
        favoriRepository.save(favoriSave);
    }

    public void initDB(){
        logger.error(">>> Add new categorie") ;
        categorieRepository.save(new Categorie("projet",1));
        categorieRepository.save(new Categorie("tech",2));
        categorieRepository.save(new Categorie("formation",3));
        logger.error(">>> Add new favorie") ;
        //favoriRepository.save(new Favori(categorieRepository.findById(Long.valueOf(1)).get(),"dvddac","Appplication pour dérogation",1,""));
        //favoriRepository.save(new Favori(categorieRepository.findById(Long.valueOf(2)).get(),"Asg","Avis Sinistre Grave",1,""));
        favoriRepository.save(new Favori(0,1,"dvddac","application",1,"https://xlrelease.azfr.allianz/#/templates/Folder9cef947e0bf349a5bab7fea88cdaabc8-Folderfa65b5781e3f4b2eb5ee60d3fff75559-Folder0f32790d37484c6f9b33a2289e71b90c-Releasefeef0133fb1a4c0fb37afda2d6d637e2"));
        favoriRepository.save(new Favori(0,2,"ASG","Avis Sinistre Grave",1,"https://xlrelease.azfr.allianz/#/templates/Folder9cef947e0bf349a5bab7fea88cdaabc8-Folderfa65b5781e3f4b2eb5ee60d3fff75559-Folder0f32790d37484c6f9b33a2289e71b90c-Releasefeef0133fb1a4c0fb37afda2d6d637e2"));
        favoriRepository.save(new Favori(0,3,"Xlr pour change Route53 sur un namespace. (qd change cluster)","https://xlrelease.azfr.allianz/#/templates/Folder9cef947e0bf349a5bab7fea88cdaabc8-Folderfa65b5781e3f4b2eb5ee60d3fff75559-Folder0f32790d37484c6f9b33a2289e71b90c-Releasefeef0133fb1a4c0fb37afda2d6d637e2",1,"https://xlrelease.azfr.allianz/#/templates/Folder9cef947e0bf349a5bab7fea88cdaabc8-Folderfa65b5781e3f4b2eb5ee60d3fff75559-Folder0f32790d37484c6f9b33a2289e71b90c-Releasefeef0133fb1a4c0fb37afda2d6d637e2"));
        favoriRepository.save(new Favori(0,1,"Communaute DevOps et Integration","toutes les infos",1, "https://allianzms.sharepoint.com/teams/FR0023-6442517"));
    }

}
