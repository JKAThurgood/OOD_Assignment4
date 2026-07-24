package D5700.instruction

import D5700.cpu.CPU
import D5700.io.Screen

class DrawInstruction : Instruction() {
    override fun perform(cpu: CPU) {
        val screen = Screen.instance()
        screen.draw(cpu.registers[0], cpu.registers[1].toInt(), cpu.registers[2].toInt())
    }
}
