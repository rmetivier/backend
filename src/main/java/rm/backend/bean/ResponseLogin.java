package rm.backend.bean;

import lombok.*;
import rm.backend.model.Categorie;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseLogin {
    String login;
    String userId;
    List<Categorie> categories;
}
