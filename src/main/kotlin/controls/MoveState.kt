package controls

data class MoveState(
    val up: Int = 0,
    val down: Int = 0,
    val left: Int = 0,
    val right: Int = 0,
    val forward: Int = 0,
    val back: Int = 0,
    val pitchUp: Int = 0,
    val pitchDown: Int = 0,
    val yawLeft: Int = 0,
    val yawRight: Int = 0,
    val rollLeft: Int = 0,
    val rollRight: Int = 0,
)
