package controls

import three.js.Camera
import three.js.Quaternion
import kotlinx.browser.document

class FlyControls(camera: Camera) {

    init {
        document.addEventListener("contextmenu", { event: org.w3c.dom.events.Event -> event.preventDefault() }, false)
    }

    var movementSpeed = 1.0
    var rollSpeed = 0.005
    var dragToLook = false
    var autoForwad = false
    var tmpQuaternion = Quaternion()
    var mouseStatus = 0


//    companion object {
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