package D5700

import D5700.cpu.CPU
import D5700.io.Keyboard
import D5700.io.Screen
import D5700.memory.RAM
import D5700.memory.ROM
import D5700.scheduling.CpuScheduler
import D5700.scheduling.TimerService
import D5700.strategy.RamStrategy
import D5700.strategy.RomStrategy

class D5700Computer {
    val cpu = CPU()
    val ram = RAM()
    val rom = ROM(writable = true)
    val screen = Screen.instance()
    val keyboard = Keyboard()
    private val cpuScheduler = CpuScheduler(cpu)
    private val timerService = TimerService(cpu)

    init {
        cpu.memoryStrategy = RamStrategy(ram)
        cpu.rom = rom
    }

    fun loadProgram(data: ByteArray) {
        require(data.size <= 4096) { "Program data must fit in ROM" }
        for (index in data.indices) {
            rom.write(index, data[index])
        }
    }

    fun start() {
        if (cpu.isHalted()) {
            cpu.terminate("Program already halted")
        }
        cpuScheduler.start()
        timerService.start()
    }

    fun stop() {
        cpuScheduler.stop()
        timerService.stop()
    }

    fun reset() {
        cpu.pc = 0
        cpu.address = 0
        cpu.memoryFlag = false
        cpu.timer = 0
        cpu.registers.fill(0)
        cpu.rom = rom
        cpu.memoryStrategy = RamStrategy(ram)
        screen.clear()
    }
}
