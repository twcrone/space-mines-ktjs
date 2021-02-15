package controls

import kotlinx.browser.window
import org.w3c.dom.Node
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.UIEvent
import three.js.EventDispatcher
import three.js.Object3D
import three.js.Quaternion
import three.js.Vector3

class FlyControls(
    private val object3d: Object3D,
    private val domElement: Node,
    val movementSpeed: Double = 1.0,
    val rollSpeed: Double = 0.005,
    val dragToLook: Boolean = false,
    val autoForward: Boolean = false
) : EventDispatcher() {

    var tmpQuaternion = Quaternion()
    var mouseStatus = 0
    private val moveState = MoveState()
    var moveVector = Vector3(0, 0, 0)
    var movementSpeedMultiplier = 1.0
    var rotationVector = Vector3(0, 0, 0)

    init {
        domElement.addEventListener(
            type = "contextmenu",
            callback = this::contextMenu
        )
        window.addEventListener(
            type = "keydown",
            callback = this::keyDown
        )
        window.addEventListener(
            type = "keyup",
            callback = this::keyUp
        )
        this.updateMovementVector()
        this.updateRotationVector()
    }

    private fun contextMenu(event: Event) {
        event.preventDefault()
    }

    private fun keyDown(event: Event) {
        console.log("keyDown")
        keyEvent(event, false)
    }

    private fun keyUp(event: Event) {
        console.log("keyUp")
        keyEvent(event, true)
    }

    private fun keyEvent(event: Event, up: Boolean) {
        val keyboardEvent = event as KeyboardEvent
        if (!up && event.altKey) {
            return
        }
        when (event.code) {
            "ShiftLeft", "ShiftRight" -> movementSpeedMultiplier = .1
            "KeyW" -> moveState.forward = up
            "KeyS" -> moveState.back = up
            "KeyA" -> moveState.left = up
            "KeyD" -> moveState.right = up

            "KeyR" -> moveState.up = up
            "KeyF" -> moveState.down = up

            "ArrowUp" -> moveState.pitchUp = up
            "ArrowDown" -> moveState.pitchDown = up

            "ArrowLeft" -> moveState.yawLeft = up
            "ArrowRight" -> moveState.yawRight = up

            "KeyQ" -> moveState.rollLeft = up
            "KeyE" -> moveState.rollRight = up
        }
        console.log("Keyboard event " + keyboardEvent.code)
        this.updateMovementVector()
        this.updateRotationVector()
    }

    private fun updateMovementVector() {
        val forward = moveState.forward || (autoForward && !moveState.back)
        moveVector = Vector3(
            x = moveVectorValue(moveState.left, moveState.right),
            y = moveVectorValue(moveState.down, moveState.up),
            z = moveVectorValue(forward, moveState.back)
        )
    }

    private fun moveVectorValue(negative: Boolean, positive:Boolean): Int =
        when {
            negative && !positive -> -1
            positive && !negative -> 1
            else -> 0
        }

    private fun updateRotationVector() {
        rotationVector = Vector3(
            x = moveVectorValue(moveState.pitchDown, moveState.pitchUp),
            y = moveVectorValue(moveState.yawRight, moveState.yawLeft),
            z = moveVectorValue(moveState.rollRight, moveState.rollLeft)
        )
    }

    fun dispose() {
        domElement.removeEventListener("contextmenu", this::contextMenu)
        window.removeEventListener("keyup", this::keyUp)
        window.removeEventListener("keydown", this::keyDown)
    }

    private val lastQuaternion = Quaternion()
    private val lastPosition = Vector3()

    fun update(delta: Double) {
        val moveMultiplier = delta * movementSpeed
        val rotationMultiplier = delta * rollSpeed

        object3d.translateX(moveVector.x.toDouble() * moveMultiplier)
        object3d.translateY(moveVector.y.toDouble() * moveMultiplier)
        object3d.translateZ(moveVector.z.toDouble() * moveMultiplier)

        tmpQuaternion.set(
            rotationVector.x.toDouble() * rotationMultiplier,
            rotationVector.y.toDouble() * rotationMultiplier,
            rotationVector.z.toDouble() * rotationMultiplier,
            1
        ).normalize()
        object3d.quaternion.multiply(tmpQuaternion)

        val distance = lastPosition.distanceToSquared(object3d.position)

        if (distance.toDouble() > EPS ||
            8 * (1.0 - lastQuaternion.dot(object3d.quaternion).toDouble()) > EPS
        ) {
            dispatchEvent(UIEvent("change"))
            lastQuaternion.copy(object3d.quaternion)
            lastPosition.copy(object3d.position)
        }
    }

    companion object {
        private const val EPS = 0.000001
    }
}