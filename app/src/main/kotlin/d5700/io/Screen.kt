package d5700.io

class Screen private constructor() : Display {
    private val frameBuffer = ByteArray(64)

    companion object {
        private val instance: Screen by lazy { Screen() }

        fun instance(): Screen = instance
    }

    override fun draw(ascii: Byte, row: Int, column: Int) {
        validatePosition(row, column)
        frameBuffer[row * 8 + column] = ascii
        render()
    }

    override fun render(): ByteArray {
        for (row in 0 until 8) {
            for (col in 0 until 8) {
                val value = frameBuffer[row * 8 + col]

                if (value == 0.toByte()) {
                    print('X')
                } else {
                    print(value.toInt().toChar())
                }
            }
            println()
        }
        println("#######")
        return frameBuffer.copyOf()
    }

    override fun clear() {
        frameBuffer.fill(0)
    }

    private fun validatePosition(row: Int, column: Int) {
        require(row in 0..7) { "Row must be between 0 and 7" }
        require(column in 0..7) { "Column must be between 0 and 7" }
    }
}
