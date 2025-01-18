package net.cakemc.veric

class TokenStream private constructor(private val list: List<Token>) {
    private val start =
        Token(Token.Type.WHITESPACE, SyntaxPosition.Companion.empty())
    private var end: Token? = null
    private var index = 0

    init {
        if (!list.isEmpty()) {
            val t1 = list[list.size - 1]
            val end = t1.syntaxPosition.end
            this.end = Token(Token.Type.EOF, SyntaxPosition.Companion.of(end, end))
        } else {
            this.end = Token(Token.Type.EOF, SyntaxPosition.Companion.empty())
        }
    }

    fun advance() {
        index++
    }

    fun remaining(): Int {
        return list.size - index
    }

    fun token(): Token? {
        return peak(0)
    }

    fun value(): String {
        return token()!!.value
    }

    fun type(): Token.Type {
        return token()!!.type
    }

    fun `is`(type: Token.Type): Boolean {
        return token()!!.type == type
    }

    fun `is`(value: String): Boolean {
        return token()!!.value == value
    }

    fun position(): FilePosition {
        return token()!!.syntaxPosition.start
    }

    fun lastPositionEnd(): FilePosition {
        return peak(-1)!!.syntaxPosition.end
    }

    fun nextPositionEnd(): FilePosition {
        return peak(0)!!.syntaxPosition.end
    }

    fun syntaxPosition(): SyntaxPosition {
        return token()!!.syntaxPosition
    }

    fun peak(offset: Int): Token? {
        val idx = index + offset
        if (idx < 0) return start
        if (idx >= list.size) return end
        return list[idx]
    }

    fun peakString(offset: Int, count: Int): String {
        val sb = StringBuilder()

        for (i in 0 until count) {
            sb.append(peak(offset + i)!!.value).append(" ")
        }

        return sb.toString().trim { it <= ' ' }
    }

    override fun toString(): String {
        return value()
    }

    companion object {
        fun wrap(list: List<Token>): TokenStream {
            return TokenStream(list)
        }
    }
}