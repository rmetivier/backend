package rm.backend.model;

//import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "fafavori")
public class Favori {

    public Favori() {}

  /*
    public Favori(Categorie categorie, String title, String description, int ordre, String url) {
        this.id = id;
        this.categorie = categorie;
        this.title = title;
        this.description = description;
        this.ordre = ordre;
        this.url = url;
    }
*/

    public Favori(long idUser,long idCategorie, String title, String description, int ordre, String url) {
        this.idUser      = idUser;
        this.idCategorie = idCategorie;
        this.title       = title;
        this.description = description;
        this.ordre       = ordre;
        this.url         = url;
    }

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "idUser")
    private long idUser;
    @Column(name = "idCategorie")
    private long idCategorie;
    @Column(name = "ordre")
    private int  ordre;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "url")
    private String url;

    private long seq;
    private String libelleCategorie;

    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCategorie", referencedColumnName = "id")
    private Categorie categorie;
    */
}
