ChocolateCharts().compute()

class ChocolateCharts {

    val input = "165061"

    val recipes = mutableListOf<Int>()
    var indexRecipeElf1 = 0
    var indexRecipeElf2 = 1
    var recipesDone = 2

    fun compute() {
        var start = System.currentTimeMillis()
        recipes.add(3)
        recipes.add(7)
        while (true) {
            val recipe1 = recipes[indexRecipeElf1]
            val recipe2 = recipes[indexRecipeElf2]
            val newRecipe = recipe1 + recipe2
            if (newRecipe < 10) {
                recipes.add(newRecipe)
                recipesDone++
            } else {
                recipes.add(newRecipe / 10)
                recipesDone++
                if (recipes.takeLast(input.length).joinToString("").equals(input)) {
                    break
                }
                recipes.add(newRecipe % 10)
                recipesDone++
            }
            if (recipes.takeLast(input.length).joinToString("").equals(input)) {

                break
            }
            indexRecipeElf1 = (indexRecipeElf1 + recipe1 + 1) % recipes.size
            indexRecipeElf2 = (indexRecipeElf2 + recipe2 + 1) % recipes.size

        }

        println(recipesDone - input.length)
    }
}