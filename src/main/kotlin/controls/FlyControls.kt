package controls

import three.js.Camera
import three.js.Quaternion
import kotlinx.browser.document
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import three.js.Event
import three.js.EventDispatcher
import three.js.Vector3

class FlyControls(val camera: Camera) {

    val domElement = document
    val scope = this


    var movementSpeed = 1.0
    var rollSpeed = 0.005
    var dragToLook = false
    var autoForward = false
    var tmpQuaternion = Quaternion()
    var mouseStatus = 0
    var moveState = MoveState()
    var moveVector = Vector3( 0, 0, 0 )
    var movementSpeedMultiplier = 1.0
    var rotationVector = Vector3( 0, 0, 0 )

    init {
        document.addEventListener(
            type ="contextmenu",
            callback = { event -> event.preventDefault() }
        )
        document.addEventListener(
            type = "keydown",
            callback = { event ->
                val keyboardEvent = event as KeyboardEvent
                if(!keyboardEvent.altKey) {
                    when(event.code) {
                         "ShiftLeft", "ShiftRight" -> movementSpeedMultiplier = .1
                         "KeyW" -> moveState.forward = 1
                         "KeyS" -> moveState.back = 1
                         "KeyA" ->  moveState.left = 1 
                         "KeyD" ->  moveState.right = 1 

                         "KeyR" ->  moveState.up = 1 
                         "KeyF" ->  moveState.down = 1 

                         "ArrowUp" ->  moveState.pitchUp = 1 
                         "ArrowDown" ->  moveState.pitchDown = 1 

                         "ArrowLeft" ->  moveState.yawLeft = 1 
                         "ArrowRight" ->  moveState.yawRight = 1 

                         "KeyQ" ->  moveState.rollLeft = 1 
                         "KeyE" ->  moveState.rollRight = 1 
                    }
                }
                this.updateMovementVector()
                this.updateRotationVector()
            }
        )
    }

    private fun contextmenu(event: Event) {

    }

    private fun updateMovementVector() {
        val forward = if (moveState.forward == 1 || (autoForward && moveState.back == 0)) 1 else 0

        moveVector = Vector3(
            x = (- this.moveState.left + this.moveState.right),
            y = ( - this.moveState.down + this.moveState.up ),
            z = ( - forward + this.moveState.back )
        )
    }

    private fun updateRotationVector() {
        rotationVector = Vector3(
            x = ( - this.moveState.pitchDown + this.moveState.pitchUp ),
            y = ( - this.moveState.yawRight + this.moveState.yawLeft ),
            z =  ( - this.moveState.rollRight + this.moveState.rollLeft )
        )
    }

    fun dispose() {

    }

    fun update() {

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