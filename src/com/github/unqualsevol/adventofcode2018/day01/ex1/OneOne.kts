import java.io.File

var count = 0
File("input").forEachLine { count += it.toInt() }
println(count)