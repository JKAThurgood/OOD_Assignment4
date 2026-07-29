package d5700.scheduling

import d5700.cpu.CPU
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class CpuScheduler(private val cpu: CPU) {
    private var executor: ScheduledExecutorService? = null
    private var running = false

    fun start() {
        if (running) {
            return
        }
        running = true
        executor = Executors.newSingleThreadScheduledExecutor()
        executor?.scheduleAtFixedRate(
            { cpu.cycle() },
            0L,
            2L,
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
}
