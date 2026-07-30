package d5700.instruction

import d5700.cpu.CPU

abstract class Instruction {
    private var skipTriggered = false

    fun execute(cpu: CPU) {
        validate(cpu)
        perform(cpu)

        if (cpu.isHalted()) {
            return
        }

        updateProgramCounter(cpu)
    }

    open fun decode(opcode: Short) {
    }

    protected open fun validate(cpu: CPU) {
        if (this !is JumpInstruction) {
            require(cpu.getMemoryStrategy() != null) {
                "CPU memory strategy must be set"
            }
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
                cpu.advanceProgramCounter(4)
            } else {
                cpu.advanceProgramCounter(incrementAmount())
            }
        }
    }
}