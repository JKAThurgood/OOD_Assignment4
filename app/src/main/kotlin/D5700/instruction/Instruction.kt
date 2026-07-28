package D5700.instruction

import D5700.cpu.CPU

abstract class Instruction {
    private var opcode: Short = 0
    private var skipTriggered = false

    fun execute(cpu: CPU) {
        decode(opcode)
        validate(cpu)
        perform(cpu)
        if (cpu.isHalted()) {
            return
        }
        updateProgramCounter(cpu)
    }

    protected fun decode(opcode: Short) {
        this.opcode = opcode
    }

    protected open fun validate(cpu: CPU) {
        if (this !is JumpInstruction) {
            require(cpu.memoryStrategy != null) { "CPU memory strategy must be set" }
        }
    }

    protected abstract fun perform(cpu: CPU)

    protected open fun shouldIncrementPC(): Boolean = true

    protected open fun incrementAmount(): Int = 2

    protected open fun shouldUpdatePc(): Boolean = true

    protected fun markSkip() {
        skipTriggered = true
    }

    protected open fun updateProgramCounter(cpu: CPU) {
        if (!shouldUpdatePc()) {
            return
        }
        if (shouldIncrementPC()) {
            if (skipTriggered) {
                cpu.pc += 4
            } else {
                cpu.pc += incrementAmount()
            }
        }
    }
}
