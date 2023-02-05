package we.juicy.juicyrecipes.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "recipeUser")
    private List<Contents> amountPresent = new ArrayList<>();

    public void addContents(Contents contents){
        amountPresent.add(contents);
        contents.setRecipeUser(this);
    }

    @Override
    public String toString() {
        return "RecipeUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amountPresent=" + amountPresent +
                '}';
    }
}
