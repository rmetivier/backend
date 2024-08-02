package rm.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import rm.backend.bean.Login;
import rm.backend.bean.ResponseLogin;
import rm.backend.model.Categorie;
import rm.backend.model.Favori;
import rm.backend.model.User;
import rm.backend.repository.CategorieRepository;
import rm.backend.repository.FavoriRepository;
import rm.backend.repository.UserRepository;
import rm.backend.services.DBService;
import java.util.List;
import java.util.Optional;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;


@RestController
    @RequestMapping("/api")

    public class MainController {

        Logger logger = LogManager.getLogger(MainController.class);
        @Autowired
        UserRepository userRepository;
        @Autowired
        CategorieRepository categorieRepository;
        @Autowired
        FavoriRepository favoriRepository;
        @Autowired
        DBService dbService;

        private long getKeyDbUserIdFromHeaders(HttpHeaders headers){
            try {return Long.parseLong(headers.getFirst("userId"));}catch (Exception ex){return 0; }
        }
        private String getUrlOauth2GoogleRedirct(){
            if ("Windows 10".equals(System.getProperty("os.name"))) {
                return  "http://localhost:4200/";
            }
            return "https://4me.elearnchess.com/";
        }


        @GetMapping("/home")
        public String home(Model model) {
            return "hello world";
        }

        @PostMapping("/login")
        public ResponseEntity<ResponseLogin> login(@RequestBody Login login) {
            Optional<User> oUser = userRepository.findByLoginAndPwd(login.getUser(), login.getPassword());
            if (oUser.isPresent()) {
                return new ResponseEntity<>(ResponseLogin.builder()
                        .login(     login.getUser())
                        .userId(    ""+oUser.get().getId())
                        .categories(dbService.getCategories())
                        .build() , HttpStatus.OK);
            } else{
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        }

        @GetMapping("/initDB")
        public ResponseEntity<List<Favori>> initDb() {
            dbService.initDB();
            logger.error(">>> chargement des favoris") ;
            return new ResponseEntity<>(dbService.getFavoris(1), HttpStatus.OK);
        }



        /** Favori */
        @GetMapping("/favoris")
        public ResponseEntity<List<Favori>> getFavoris(@RequestHeader HttpHeaders headers) {
            logger.error(">>> chargement des favoris pour " + this.getKeyDbUserIdFromHeaders(headers)) ;
            return new ResponseEntity<>(dbService.getFavoris(this.getKeyDbUserIdFromHeaders(headers)), HttpStatus.OK);
        }
        @PostMapping("/favoris")
        public  ResponseEntity<List<Favori>>  createFavori(@RequestHeader HttpHeaders headers, @org.springframework.web.bind.annotation.RequestBody Favori favori) {
            Optional<Favori> ofavoriDB = favoriRepository.findById(Long.valueOf(favori.getId()));
            if (ofavoriDB.isPresent()){
                dbService.updateFavori(ofavoriDB.get(),favori);
            }else{
                favoriRepository.save(new Favori(this.getKeyDbUserIdFromHeaders(headers),favori.getIdCategorie(),favori.getTitle(), favori.getDescription(), favori.getOrdre(), favori.getUrl()));
            }
            return new ResponseEntity<>(dbService.getFavoris(this.getKeyDbUserIdFromHeaders(headers)), HttpStatus.OK);
        }
        @DeleteMapping("/favoris")
        public  ResponseEntity<List<Favori>>  deleteFavori(@RequestHeader HttpHeaders headers, @RequestBody Favori favori) {
            Optional<Favori> ofavoriDB = favoriRepository.findById(Long.valueOf(favori.getId()));
            if (ofavoriDB.isPresent()){
                favoriRepository.deleteById(Long.valueOf(favori.getId()));
            }
            return new ResponseEntity<>(dbService.getFavoris(this.getKeyDbUserIdFromHeaders(headers)), HttpStatus.OK);
        }

        /** Catégorie */
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



    @PostMapping("/oauth2")
    public  ResponseEntity<ResponseLogin>  oauth2(@RequestBody String code) {

        String        access_token = "";
        String        email        = "";
        ObjectMapper objectMapper = new ObjectMapper();

        
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // 1) recup  du code google
        //-------------------------------------------------------------------------------------------------------------------------
        logger.error("Code: " + code );
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // 2) On a le code google du user, on demande un token pour votre application
        //-------------------------------------------------------------------------------------------------------------------------
		/* POST
    	   curl --location 'https://oauth2.googleapis.com/token' \
			--header 'Content-Type: application/x-www-form-urlencoded' \
			--data-urlencode 'code=4/0AfJohXk8coL-ckEtQFNU3Jxep6WLZExY_Evu7MNClT59gMF33UDrylcABsZLThKAtRCncw' \
			--data-urlencode 'client_id=289637276116-4ihedsk5mapd8tbnqtgt37a18adcghse.apps.googleusercontent.com' \
			--data-urlencode 'client_secret=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' \
			--data-urlencode 'redirect_uri=http://127.0.0.1:8080/elearnchess/rest/oauth2' \
			--data-urlencode 'grant_type=authorization_code'
		 */
        logger.error("etape 2: " );
        try {
            OkHttpClient client = new OkHttpClient();
            com.squareup.okhttp.RequestBody formBody = new FormEncodingBuilder()
                    .add("code",         code)
                    .add("client_id",    "69992971949-sggkh37fq9o0c9ecthpd0cevmgpa06ut.apps.googleusercontent.com")
                    .add("client_secret","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")
                    //.add("redirect_uri", Appli.getInstance().getOauth2RedirectUri())
                    //.add("client_id",    "289637276116-4ihedsk5mapd8tbnqtgt37a18adcghse.apps.googleusercontent.com")
                    //.add("client_secret","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")
                    //.add("redirect_uri", "http://localhost:4200/")
                    .add("redirect_uri", getUrlOauth2GoogleRedirct())
                    .add("grant_type",   "authorization_code")
                    .build();
            logger.error("etape 2.1 " );
            Request googleTokenRequest = new Request.Builder()
                    .url("https://oauth2.googleapis.com/token")
                    .post(formBody)
                    .build();
            logger.error("etape 2.2 " );
            com.squareup.okhttp.Response response = client.newCall(googleTokenRequest).execute();
            logger.error("etape 2.3 " );
            if (response.isSuccessful()) {
                String reponseToken =  response.body().string();
                logger.error("reponse token " + reponseToken );
                logger.error("UserWS.oauth2 reponse token " + response.code() +"|"+ reponseToken );


                JsonNode jsonNode = objectMapper.readTree(reponseToken);
                access_token = jsonNode.get("access_token").asText();
                logger.error("UserWS.oauth2 access_token  " + access_token );


	            /* Exemple de reponse:
		    	 	{
					    "access_token": "ya29.a0AfB_byDP8rIOuCNqLikpHXyaqAGMGIkmSxuEuWhDJXX_aS87wZ7-FDNvQ9N-ZeZnkyRtTk0_XEdRhyij40FnQG5kWDMChE0PwKo952bmUjJpXiq92VT7thLxy8W0z0QTCtkoXTMSL0667kAE477kMO0i7mk56YLEdlBDaCgYKAfQSARASFQHGX2MizLePtvLE62GwY5M3X-3VSg0171",
					    "expires_in": 3599,
					    "scope": "openid https://www.googleapis.com/auth/userinfo.email",
					    "token_type": "Bearer",
					    "id_token": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFmNDBmMGE4ZWYzZDg4MDk3OGRjODJmMjVjM2VjMzE3YzZhNWI3ODEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIyODk2MzcyNzYxMTYtNGloZWRzazVtYXBkOHRibnF0Z3QzN2ExOGFkY2doc2UuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIyODk2MzcyNzYxMTYtNGloZWRzazVtYXBkOHRibnF0Z3QzN2ExOGFkY2doc2UuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDg1NDI1NDY5NjI4NTM5MDQ0NjIiLCJlbWFpbCI6InJpY2hhcmQubWV0aXZpZXJAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJXeGFpdk1LTGpVaVF1dXlkVFFhYkFBIiwiaWF0IjoxNzA1MTMxOTg4LCJleHAiOjE3MDUxMzU1ODh9.rebdWQ8N99EH2b-Dnb854c4VUVErlYv1uDjYefYOR00EIhU5DvLfAcfdUFQcJH_Mdi6SK5gVCtKkNuClFQqf86K2Z6jol-mV68q2Rq0-B_TLk4BRxhq1O3DZUgoNnIwthHQ6ycUn-0PCAVUUVmaD1N3EGIJ66nJPI0eodDKyogviw3SFlW9PcPT_41B7tUB4fURYDtwxxgARnM8bOqvddQLjOq2qDYjCzbzr-Hiv6MSbZdezMZOZlFHEOtCGRawh3cDxmfwOCBZTuRC4ezj4owYaKKacHM6lCMBeZWTklDPoiSpR1Jcle9uCiItMSHV_RMbJlawDkdXo7CTyMRDvgg"
					}
		    	 */
            }else{
                logger.error("UserWS.oauth2 get token en erreur   " + "Unexpected code " + response );
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            logger.error("etape 2.4 " );
            //if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        } catch ( Throwable e) {
            logger.error("UserWS.oauth2 get token en erreur  avec code " + code   + " " + e.toString() );
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // 3) On a le token pour appeler les services Google, on demande les infos du user, son mail
        //-------------------------------------------------------------------------------------------------------------------------
		/* GET
		   curl
		   --location 'https://www.googleapis.com/oauth2/v1/userinfo?alt=json' \
		   --header 'Authorization: Bearer ya29.a0AfB_byBhvYQKs7Q8yUGeRT0leKC2yzor_voOdnZcoa32Vg_GWEvQwMhEA0bRWgnq2-b1Xad_iaAwe5kpOPkAsdNOfuQ413-oVr72nbhpHePL0qWKMJH3BwOiMOXnWj0aIiRJaCj3E4YavTQwtwRf5_W5DpFqf15Jo6XnaCgYKATkSARASFQHGX2MiHgdDhPVQ5YRd3Li6dXWFZA0171'
        */
        try {
            OkHttpClient client = new OkHttpClient();
            Request googleTokenRequest = new Request.Builder()
                    .url("https://www.googleapis.com/oauth2/v1/userinfo?alt=json")
                    .header("Authorization", "Bearer " + access_token)
                    .get()
                    .build();
            com.squareup.okhttp.Response response = client.newCall(googleTokenRequest).execute();
            if (response.isSuccessful()) {
                String reponseUserInfo =  response.body().string();
                logger.error("reponse userinfo " + reponseUserInfo );
                logger.error("UserWS.oauth2 reponse userInfo " + response.code() +"|"+ reponseUserInfo );
                JsonNode jsonNode = objectMapper.readTree(reponseUserInfo);
                email             = jsonNode.get("email").asText();
                logger.error("UserWS.oauth2 userInfo email  " + email );
		    	// Exemple de reponse:
				//	{
				//	  "id": "108542546962853904462",
				//	  "email": "richard.metivier@gmail.com",
				//	  "verified_email": true,
				//	  "picture": "https://lh3.googleusercontent.com/a-/ALV-UjVGTsl2q7t5OLOqp8cNdm0-amPBZZDtkAw3gxKrdBjEaA=s96-c"
				//	}

            }else{
                logger.error("UserWS.oauth2 get token en erreur avec code " + code + " " + "Unexpected code " + response );
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            //if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        } catch (Exception e) {
            logger.error("UserWS.oauth2 get token en erreur   " + e.toString() );
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // 4) On a l'email on recupere le premier compte
        //-------------------------------------------------------------------------------------------------------------------------

        Optional<User> oUser = userRepository.findByEmail(email);
        if (oUser.isPresent()) {
            return new ResponseEntity<>(ResponseLogin.builder()
                    .login(     email.split("@")[0] )
                    .userId(    ""+oUser.get().getId())
                    .categories(dbService.getCategories())
                    .build() , HttpStatus.OK);
        } else{
            // Il faut creer le user
            User user = new User();
            user.setEmail(email);
            user.setLogin(email.split("@")[0]);
            user.setPwd("oauth");
            userRepository.save(user);
            // On recupere l'id
            oUser = userRepository.findByEmail(email);
            return new ResponseEntity<>(ResponseLogin.builder()
                        .login(     email.split("@")[0] )
                        .userId(    ""+oUser.get().getId())
                        .categories(dbService.getCategories())
                        .build() , HttpStatus.OK);
        }
    }

}
