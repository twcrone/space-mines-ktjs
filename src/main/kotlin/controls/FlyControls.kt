package controls

import kotlinx.browser.document
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.UIEvent
import three.js.Camera
import three.js.EventDispatcher
import three.js.Quaternion
import three.js.Vector3

class FlyControls(
    private val camera: Camera,
    val movementSpeed: Double = 1.0,
    val rollSpeed: Double = 0.005,
    val dragToLook: Boolean = false
) : EventDispatcher() {

    private val scope = this
    var autoForward = false
    var tmpQuaternion = Quaternion()
    var mouseStatus = 0
    var moveState = MoveState()
    var moveVector = Vector3(0, 0, 0)
    var movementSpeedMultiplier = 1.0
    var rotationVector = Vector3(0, 0, 0)

    init {
        document.addEventListener(
            type = "contextmenu",
            callback = { event -> event.preventDefault() }
        )
        document.addEventListener(
            type = "keydown",
            callback = this::keyDown
        )
        document.addEventListener(
            type = "keyup",
            callback = this::keyUp
        )
        this.updateMovementVector()
        this.updateRotationVector()
    }

    private fun keyDown(event: Event) {
        keyEvent(event, false)
    }

    private fun keyUp(event: Event) {
        keyEvent(event, true)
    }

    private fun keyEvent(event: Event, up: Boolean) {
        val i = if(up) 1 else 0
        val keyboardEvent = event as KeyboardEvent
        if (!keyboardEvent.altKey) {
            when (event.code) {
                "ShiftLeft", "ShiftRight" -> movementSpeedMultiplier = .1
                "KeyW" -> moveState.forward = i
                "KeyS" -> moveState.back =  i
                "KeyA" -> moveState.left =  i
                "KeyD" -> moveState.right =  i

                "KeyR" -> moveState.up =  i
                "KeyF" -> moveState.down =  i

                "ArrowUp" -> moveState.pitchUp =  i
                "ArrowDown" -> moveState.pitchDown =  i

                "ArrowLeft" -> moveState.yawLeft =  i
                "ArrowRight" -> moveState.yawRight =  i

                "KeyQ" -> moveState.rollLeft =  i
                "KeyE" -> moveState.rollRight =  i
            }
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

    }

    private val lastQuaternion = Quaternion()
    private val lastPosition = Vector3()

    fun update(delta: Double) {

        val moveMult = delta * scope.movementSpeed;
        val rotMult = delta * scope.rollSpeed;

        camera.translateX(scope.moveVector.x.toDouble() * moveMult);
        camera.translateY(scope.moveVector.y.toDouble() * moveMult);
        camera.translateZ(scope.moveVector.z.toDouble() * moveMult);

        tmpQuaternion.set(
            scope.rotationVector.x.toDouble() * rotMult,
            scope.rotationVector.y.toDouble() * rotMult,
            scope.rotationVector.z.toDouble() * rotMult,
            1
        ).normalize()
        camera.quaternion.multiply(scope.tmpQuaternion);

        val distance = lastPosition.distanceToSquared(camera.position)

        if (distance.toDouble() > EPS ||
            8 * (1.0 - lastQuaternion.dot(camera.quaternion).toDouble()) > EPS
        ) {
            dispatchEvent(UIEvent("change"))
            lastQuaternion.copy(scope.camera.quaternion)
            lastPosition.copy(scope.camera.position)

        }

    }

    companion object {
        private const val EPS = 0.000001
    }
//        private fun keyDown(event: KeyboardEvent ) {
//            if(event.altKey) {
//                return;
//            }
//
//            when ( event ) {
//
//
////                case 16: /* shift */ this.movementSpeedMultiplier = .1; break;
////
////                case 87: /*W*/ this.moveState.forward = 1; break;
////                case 83: /*S*/ this.moveState.back = 1; break;
////
////                case 65: /*A*/ this.moveState.left = 1; break;
////                case 68: /*D*/ this.moveState.right = 1; break;
////
////                case 82: /*R*/ this.moveState.up = 1; break;
////                case 70: /*F*/ this.moveState.down = 1; break;
////
////                case 38: /*up*/ this.moveState.pitchUp = 1; break;
////                case 40: /*down*/ this.moveState.pitchDown = 1; break;
////
////                case 37: /*left*/ this.moveState.yawLeft = 1; break;
////                case 39: /*right*/ this.moveState.yawRight = 1; break;
////
////                case 81: /*Q*/ this.moveState.rollLeft = 1; break;
////                case 69: /*E*/ this.moveState.rollRight = 1; break;
//                else -> return
//            }
//
//            this.updateMovementVector();
//            this.updateRotationVector();
//
//        };
//    }
}