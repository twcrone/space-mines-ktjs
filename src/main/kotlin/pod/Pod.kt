package pod

import three.js.Mesh
import three.js.MeshLambertMaterial
import three.js.SphereGeometry

data class Pod(
    val id: Int = 0,
    val radius: Int = 1,
    val spacing: Int = 10,
    val x: Int = 0,
    val y: Int = 0,
    val z: Int = 0,
    val radiation: Int = -1,
    val flagged: Boolean = false
) {
    private val geometry = SphereGeometry(radius);
    val color = colorOf(radiation)
    private val material = MeshLambertMaterial().apply { color = color }
    val mesh = Mesh(geometry, material)

    companion object {
        private fun colorOf(radiation: Int) = when(radiation) {
            0, -1 -> GRAY
            1 -> BLUE
            2 -> GREEN
            3 -> YELLOW
            4 -> ORANGE
            in 5..26 -> RED
            else -> throw RuntimeException("'radiation' level of $radiation is not valid")
        }
    }
}
