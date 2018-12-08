import java.io.File

val split = File("input").readText().split(" ")
val input = IntArray(split.size) { split[it].toInt() }

println(readHeader(input.iterator()))


fun readHeader(inputIterator: IntIterator):Int{
    val countChilds = inputIterator.nextInt();
    val countMetadata = inputIterator.nextInt()
    var result = 0
    for(i in 0 until countChilds) {
        result += readHeader(inputIterator)
    }
    for(i in 0 until countMetadata){
        result += inputIterator.nextInt()
    }
    return result
}