package net.cakemc.veric

import net.cakemc.veric.GenericLexerContext.LexerToken
import java.nio.charset.StandardCharsets

object LexerTokenizer {
    @JvmStatic
    fun parse(bytes: ByteArray): List<Token> {
        val tokenList = parseKeepWhitespace(bytes)
        tokenList.removeIf { token: Token -> token.type == Token.Type.WHITESPACE }
        return tokenList
    }

    fun parseKeepWhitespace(bytes: ByteArray): MutableList<Token> {
        val text = String(bytes, StandardCharsets.UTF_8)
        val tokenList: MutableList<Token> = ArrayList()
        var offset = 0
        var line = 0
        var column = 0
        val length = text.length
        var input = text

        while (offset < length) {
            val startPos = FilePosition(column, line)

            val lexerToken = Lexer.LEXER.nextToken(input)
                ?: throw RuntimeException(
                    "Could not parse token: " +
                            SyntaxPosition.Companion.of(startPos, startPos)
                )

            if (lexerToken.length + offset > length) {
                break
            }

            for (i in offset until offset + lexerToken.length) {
                val c = text[i]

                if (c == '\n') {
                    line++
                    column = 0
                } else {
                    column += if ((c == '\t')) 4 else 1
                }
            }

            val endPos = FilePosition(column, line)
            tokenList.add(
                Token(
                    lexerToken.type,
                    lexerToken.content,
                    SyntaxPosition.Companion.of(startPos, endPos)
                )
            )

            input = input.substring(lexerToken.length)
            offset += lexerToken.length
        }

        return tokenList
    }
}