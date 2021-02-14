package controls

data class MoveState(
    var up: Int = 0,
    var down: Int = 0,
    var left: Int = 0,
    var right: Int = 0,
    var forward: Int = 0,
    var back: Int = 0,
    var pitchUp: Int = 0,
    var pitchDown: Int = 0,
    var yawLeft: Int = 0,
    var yawRight: Int = 0,
    var rollLeft: Int = 0,
    var rollRight: Int = 0,
)
