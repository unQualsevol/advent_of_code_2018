import java.io.File

val result = mutableSetOf<String>()
val clothMap = mutableMapOf<String, Int>()
val clothIdMap = mutableMapOf<String, Cloth>()
File("input").forEachLine {
    val regex = """^#(\d+)\s@\s(\d+)\,(\d+):\s(\d+)x(\d+)$""".toRegex()

    val matchResult = regex.find(it)

    val (id, x0s, y0s, dxs, dys) = matchResult!!.destructured

    val x0 = x0s.toInt()
    val y0 = y0s.toInt()
    val dx = dxs.toInt()
    val dy = dys.toInt()

    val xf = x0 + dx.toInt() -1
    val yf = y0.toInt() + dy.toInt() -1

    val inches = mutableListOf<String>()
    val cloth = Cloth(id, inches)
    clothIdMap[id] = cloth

    for(x in x0..xf) {
        for(y in y0..yf){
            val inch = "$x,$y"
            inches.add("$x,$y")
            var count = clothMap[inch]
            if(count == null){
                clothMap[inch] = 1
            } else {
                if(count == 1){
                    result.add(inch)
                }
                clothMap[inch] = ++count
            }
        }
    }
}

clothIdMap.values.filter { v -> !v.isOverlapping(clothMap)}.forEach { println(it.id) }

data class Cloth(val id:String, val inches:List<String>){
    fun isOverlapping(clothMap:Map<String, Int>):Boolean {
        inches.forEach {
            val count =clothMap[it];
            if(count!! > 1) {
                return true
            }
        }
        return false;
    }
}
