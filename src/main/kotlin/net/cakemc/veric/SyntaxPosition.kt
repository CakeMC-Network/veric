package net.cakemc.veric

@JvmRecord
data class SyntaxPosition(val start: FilePosition, val end: FilePosition) {
    companion object {
        fun empty(): SyntaxPosition {
            return SyntaxPosition(FilePosition(0, 0), FilePosition(0, 0))
        }

        fun of(start: FilePosition, end: FilePosition): SyntaxPosition {
            return SyntaxPosition(start, end)
        }
    }
}
