package rm.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "categorie")
public class Categorie {

    public Categorie() {

    }
    public Categorie(String title, int ordre) {
        this.title = title;
        this.ordre = ordre;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    private String title;


    @Column(name = "ordre")
    private int  ordre;


}
