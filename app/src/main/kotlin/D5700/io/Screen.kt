package D5700.io

class Screen private constructor() {
    private val frameBuffer = ByteArray(64)

    companion object {
        private val instance: Screen by lazy { Screen() }

        fun instance(): Screen = instance
    }

    fun draw(ascii: Byte, row: Int, column: Int) {
        validatePosition(row, column)
        frameBuffer[row * 8 + column] = ascii
    }

    fun render(): ByteArray = frameBuffer.copyOf()

    private fun validatePosition(row: Int, column: Int) {
        require(row in 0..7) { "Row must be between 0 and 7" }
        require(column in 0..7) { "Column must be between 0 and 7" }
    }
}
