package rm.backend.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;


/*
create table fauser (
        id  bigint(20) unsigned NOT NULL auto_increment,
login varchar(255),
pwd   varchar(255),
pwdgen varchar(255),
email varchar(255),
role varchar(255),
dateCrt datetime,
dateLastUse datetime,
primary key (id)
    )
*/


@Getter
@Setter
@Entity
@Table(name = "fauser")
public class User {

    public User() {
    }

    public User(long id, String login, String pwd, String pwdgen, String email, String role ) {
        this.id = id;
        this.login = login;
        this.pwd = pwd;
        this.pwdgen = pwdgen;
        this.email = email;
        this.role = role;

    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "login")
    private String login;

    @Column(name = "pwd")
    private String pwd;

    @Column(name = "pwdgen")
    private String pwdgen;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    private String role;

    /*
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateCrt")
    private Date dateCrt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateLastUse")
    private Date dateLastUse;
*/

}
