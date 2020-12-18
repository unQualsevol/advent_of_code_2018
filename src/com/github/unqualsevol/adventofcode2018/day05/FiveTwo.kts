import java.io.File



val polymerAsString = File("input.txt").readText()


val reducedPolymerLongitude = mutableListOf<Int>()
for (ch in 'A' .. 'Z') {
    println(ch)

    val polymer = Polymer(polymerAsString)
    polymer.removeReactor(ch)
    reducedPolymerLongitude.add(polymer.reduce())
}
println(reducedPolymerLongitude.min())

data class Polymer(val polymerAsString:String) {
    private val polymerInChars = mutableListOf<Char>()

    init {
        polymerAsString.toCharArray().forEach { polymerInChars.add(it) }
        //remove the EOF char
        polymerInChars.removeAt(polymerInChars.size-1)
    }

    fun reduce():Int {
        var i = 0;

        while (i < polymerInChars.size - 1) {
            if (react(i, i + 1)) {
                if (i > 0) i--
            } else {
                i++
            }
        }
        return polymerInChars.size;
    }

    private fun react(i: Int, j: Int): Boolean {
        if (canReact(i, j)) {
            polymerInChars.removeAt(j);
            polymerInChars.removeAt(i);
            return true
        } else {
            return false
        }
    }


    private fun canReact(i: Int, j: Int) = polymerInChars[i].equals(polymerInChars[j], true) &&
            polymerInChars[i].isLowerCase().xor(polymerInChars[j].isLowerCase())



    fun removeReactor(ch: Char) {
        polymerInChars.removeAll { c -> c.equals(ch, true) }
    }
}