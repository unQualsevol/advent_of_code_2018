val testingRecipes = 165061

val recipes = mutableListOf<Int>()

var indexRecipeElf1 = 0
var indexRecipeElf2 = 1

recipes.add(3)
recipes.add(7)

var recipesDone = 2
val recipesToBeDone = testingRecipes + 10

while (recipesDone < recipesToBeDone) {
    val recipe1 = recipes[indexRecipeElf1]
    val recipe2 = recipes[indexRecipeElf2]
    val newRecipe = recipe1 + recipe2
    if (newRecipe < 10) {
        recipes.add(newRecipe)
        recipesDone++
    } else {
        recipes.add(newRecipe / 10)
        recipes.add(newRecipe % 10)
        recipesDone += 2
    }
    indexRecipeElf1 = (indexRecipeElf1 + recipe1 + 1) % recipes.size
    indexRecipeElf2 = (indexRecipeElf2 + recipe2 + 1) % recipes.size
}

recipes.subList(testingRecipes, recipesToBeDone).forEach { print(it) }
println()
