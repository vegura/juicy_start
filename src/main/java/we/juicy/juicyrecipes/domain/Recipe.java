package we.juicy.juicyrecipes.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Fetch;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Entity
public class Recipe {
    @Id
    private Integer id;

    private String name;
    @Lob
    private String description;

    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Contents> necessaryAmount;
}
