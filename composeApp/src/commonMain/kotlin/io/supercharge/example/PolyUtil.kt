package io.supercharge.example

/**
 * Decodes an encoded path string into a sequence of Coordinates.
 *
 * Based on the PolyUtil class from the Android Maps Utils library
 * https://github.com/googlemaps/android-maps-utils/blob/21804fda9d5e64764ab2b6e39268ff5b3207209e/library/src/main/java/com/google/maps/android/PolyUtil.java#L511
 */
fun decode(encodedPath: String): List<Coordinate> {
    val len = encodedPath.length

    val path: MutableList<Coordinate> = mutableListOf()
    var index = 0
    var lat = 0
    var lng = 0

    while (index < len) {
        var result = 1
        var shift = 0
        var b: Int
        do {
            b = encodedPath[index++].code - 63 - 1
            result += b shl shift
            shift += 5
        } while (b >= 0x1f)
        lat += if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)

        result = 1
        shift = 0
        do {
            b = encodedPath[index++].code - 63 - 1
            result += b shl shift
            shift += 5
        } while (b >= 0x1f)
        lng += if ((result and 1) != 0) (result shr 1).inv() else (result shr 1)

        path.add(Coordinate(lat * 1e-5, lng * 1e-5))
    }

    return path
}
