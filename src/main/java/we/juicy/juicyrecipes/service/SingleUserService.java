package we.juicy.juicyrecipes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import we.juicy.juicyrecipes.domain.Contents;
import we.juicy.juicyrecipes.domain.Ingredient;
import we.juicy.juicyrecipes.domain.Recipe;
import we.juicy.juicyrecipes.domain.RecipeUser;
import we.juicy.juicyrecipes.repository.ContentsRepository;
import we.juicy.juicyrecipes.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SingleUserService {

    private static final Integer SINGLE_USER_ID = 1;

    private final UserRepository userRepository;
    private final ContentsRepository contentsRepository;
    private final RecipeService recipeService;

    public RecipeUser getCurrentUser() {
        Optional<RecipeUser> maybeSingleUser = userRepository.findById(1);
        if (maybeSingleUser.isEmpty())
            throw new RuntimeException("User is not found");

        return maybeSingleUser.get();
    }

    public RecipeUser saveUser(RecipeUser user) {
        user.setId(SINGLE_USER_ID);
        return userRepository.save(user);
    }
    public Optional<RecipeUser> findById(Integer id){
        return userRepository.findById(id);
    }

    @Transactional
    public RecipeUser addIngredient(Integer id, Contents contents){
        Optional<RecipeUser> maybeUser = userRepository.findById(id);
        if (maybeUser.isEmpty())
            throw new RuntimeException("User with id is not found");

        RecipeUser user = maybeUser.get();
        contents.setRecipeUser(user);

        Contents savedContents = contentsRepository.save(contents);
        user.addContents(savedContents);
        return userRepository.save(user);
    }

    public List<Recipe> findRecipesForUserIngredientContents() {
        Set<Recipe> allRecipes = recipeService.findAll();
        List<Contents> userIngredientContents = contentsRepository.findByRecipeUserId(SINGLE_USER_ID);

        return allRecipes.stream()
                .filter(it -> isAllMatchingIngredientContents(it, userIngredientContents))
                .toList();
    }

    private boolean isAllMatchingIngredientContents(Recipe recipe, List<Contents> userIngredientContents) {
        return recipe.getNecessaryAmount()
                .stream()
                .allMatch(it -> isMatchIngredientAndAmount(it, userIngredientContents));
    }

    private boolean isMatchIngredientAndAmount(Contents ingredientContents, List<Contents> userIngredientContents) {
        return userIngredientContents
                .stream()
                .anyMatch(it -> it.getIngredient().equals(ingredientContents.getIngredient())
                        && it.getAmount() >= ingredientContents.getAmount());
    }

}
