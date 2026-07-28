package D5700.scheduling

import D5700.cpu.CPU
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class TimerService(private val cpu: CPU) {
    private var executor: ScheduledExecutorService? = null
    private var running = false

    fun start() {
        if (running) {
            return
        }
        running = true
        executor = Executors.newSingleThreadScheduledExecutor()
        executor?.scheduleAtFixedRate(
            { tick() },
            0L,
            16L,
            TimeUnit.MILLISECONDS
        )
    }

    fun stop() {
        if (!running) {
            return
        }
        running = false
        executor?.shutdownNow()
        executor = null
    }

    fun tick() {
        if (cpu.timer > 0) {
            cpu.timer = (cpu.timer - 1).toByte()
        }
    }
}
