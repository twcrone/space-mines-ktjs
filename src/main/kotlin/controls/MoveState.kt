package controls

data class MoveState(
    var up: Boolean = false,
    var down: Boolean = false,
    var left: Boolean = false,
    var right: Boolean = false,
    var forward: Boolean = false,
    var back: Boolean = false,
    var pitchUp: Boolean = false,
    var pitchDown: Boolean = false,
    var yawLeft: Boolean = false,
    var yawRight: Boolean = false,
    var rollLeft: Boolean = false,
    var rollRight: Boolean = false,
)
