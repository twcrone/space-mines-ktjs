import controls.FlyControls
import ext.aspectRatio
import ext.minus
import stats.js.Stats
import three.js.*
import kotlinx.browser.document
import kotlinx.browser.window
import pod.Pod
import kotlin.math.PI

class Cube {
    private val clock = Clock()
//    private val oldCamera = PerspectiveCamera(75, window.aspectRatio, 0.1, 1000).apply {
//        position.z = 5
//    }

    private val pod = Pod()
    private val camera = PerspectiveCamera(45, window.aspectRatio, 1.0, 1000).apply {
        position.set(50, 50, 125)
        lookAt(pod.mesh.position)
    }

    private val flyControls = FlyControls(
        object3d = camera,
        domElement = document.body ?: document,
        movementSpeed = 20.0,
        rollSpeed = PI/24,
        dragToLook = true
    )

    private val renderer = WebGLRenderer().apply {
        document.body?.appendChild(domElement)
        setSize(window.innerWidth, window.innerHeight)
        setPixelRatio(window.devicePixelRatio)
    }

    private val stats = Stats().apply {
        showPanel(0) // 0: fps, 1: ms, 2: mb, 3+: custom
        document.body?.appendChild(domElement)
        with (domElement.style) {
            position="fixed"
            top="0px"
            left="0px"
        }
    }

    private val scene = Scene().apply {
        //add(cube)
        add(pod.mesh)

        add(DirectionalLight(0xffffff, 1).apply { position.set(-1, 2, 4) })
        add(AmbientLight(0x404040, 1))
    }

    init {
        window.onresize = {
            camera.aspect = window.aspectRatio
            camera.updateProjectionMatrix()

            renderer.setSize(window.innerWidth, window.innerHeight)
        }
    }

    fun animate() {
        stats.begin()
        val delta = clock.getDelta().toDouble()
        flyControls.update(delta)

        renderer.clear()

        pod.mesh.rotation.x -= delta
        pod.mesh.rotation.y -= delta

        stats.end()

        window.requestAnimationFrame { animate() }
        renderer.render(scene, camera)
    }
}

fun main() = Cube().animate()