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
    val dragToLook: Boolean = false
) : EventDispatcher() {

    var autoForward = false
    var tmpQuaternion = Quaternion()
    var mouseStatus = 0
    var moveState = MoveState()
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
        keyEvent(event, false)
    }

    private fun keyUp(event: Event) {
        keyEvent(event, true)
    }

    private fun keyEvent(event: Event, up: Boolean) {
        val i = if (up) 1 else 0
        val keyboardEvent = event as KeyboardEvent
        if (!up && event.altKey) {
            return
        }
        when (event.code) {
            "ShiftLeft", "ShiftRight" -> movementSpeedMultiplier = .1
            "KeyW" -> moveState.forward = i
            "KeyS" -> moveState.back = i
            "KeyA" -> moveState.left = i
            "KeyD" -> moveState.right = i

            "KeyR" -> moveState.up = i
            "KeyF" -> moveState.down = i

            "ArrowUp" -> moveState.pitchUp = i
            "ArrowDown" -> moveState.pitchDown = i

            "ArrowLeft" -> moveState.yawLeft = i
            "ArrowRight" -> moveState.yawRight = i

            "KeyQ" -> moveState.rollLeft = i
            "KeyE" -> moveState.rollRight = i
        }
        console.log("Keyboard event " + keyboardEvent.code)
        this.updateMovementVector()
        this.updateRotationVector()
    }

    private fun updateMovementVector() {
        val forward = if (moveState.forward == 1 || (autoForward && moveState.back == 0)) 1 else 0

        moveVector = Vector3(
            x = (-this.moveState.left + this.moveState.right),
            y = (-this.moveState.down + this.moveState.up),
            z = (-forward + this.moveState.back)
        )
    }

    private fun updateRotationVector() {
        rotationVector = Vector3(
            x = (-this.moveState.pitchDown + this.moveState.pitchUp),
            y = (-this.moveState.yawRight + this.moveState.yawLeft),
            z = (-this.moveState.rollRight + this.moveState.rollLeft)
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
        val moveMultiplier = delta * movementSpeed;
        val rotationMultiplier = delta * rollSpeed;

        object3d.translateX(moveVector.x.toDouble() * moveMultiplier);
        object3d.translateY(moveVector.y.toDouble() * moveMultiplier);
        object3d.translateZ(moveVector.z.toDouble() * moveMultiplier);

        tmpQuaternion.set(
            rotationVector.x.toDouble() * rotationMultiplier,
            rotationVector.y.toDouble() * rotationMultiplier,
            rotationVector.z.toDouble() * rotationMultiplier,
            1
        ).normalize()
        object3d.quaternion.multiply(tmpQuaternion);

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