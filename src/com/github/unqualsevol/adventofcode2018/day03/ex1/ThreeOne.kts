import java.io.File

val result = mutableSetOf<String>()
val clothMap = mutableMapOf<String, Int>()
val clothIdMap = mutableMapOf<String, String>()
File("input").forEachLine {
    val regex = """^#(\d+)\s@\s(\d+)\,(\d+):\s(\d+)x(\d+)$""".toRegex()

    val matchResult = regex.find(it)

    val (ids, x0s, y0s, dxs, dys) = matchResult!!.destructured

    val x0 = x0s.toInt()
    val y0 = y0s.toInt()
    val dx = dxs.toInt()
    val dy = dys.toInt()

    val xf = x0 + dx.toInt() -1
    val yf = y0.toInt() + dy.toInt() -1

    for(x in x0..xf) {
        for(y in y0..yf){
            val inch = "$x,$y"
            var count = clothMap[inch]
            if(count == null){
                clothMap[inch] = 1
                clothIdMap[inch] = ids
            } else {
                if(count == 1){
                    result.add(inch)
                }
                clothMap[inch] = ++count
            }
        }
    }
}

println(result.size)
