package net.cakemc.veric

import net.cakemc.veric.entry.*
import net.cakemc.veric.error.ErrorUtil

class VericParser(
    private val content : String,
    private val stream: TokenStream
) {
    fun parse(): Veric {
        val objects: MutableList<VericElement<*>> = ArrayList()
        while (stream.remaining() != 0) {
            val current = stream.token()
            stream.advance()

            if (current!!.`is`(Token.Type.IDENTIFIER)) {
                objects.add(parseEntry(current.value))
            } else {
                throw IllegalStateException("unexpected token: ${current.value} expected identifier")
            }
        }

        return Veric("root__", objects)
    }

    fun parseEntry(key: String): VericElement<*> {
        if (stream.`is`(Token.Type.ASSIGN)) {
            stream.advance()
        }

        val current = stream.token()
        stream.advance()

        return when (current!!.type) {
            Token.Type.CHARACTER -> VericCharacter(key, current)
            Token.Type.BOOLEAN -> VericBoolean(key, current)
            Token.Type.STRING -> VericString(key, current)
            Token.Type.LONG -> VericLong(key, current)
            Token.Type.INT -> VericInt(key, current)
            Token.Type.FLOAT -> VericFloat(key, current)
            Token.Type.DOUBLE -> VericDouble(key, current)
            Token.Type.NULL -> VericNull(key)
            Token.Type.L_SQUARE -> parseList(key)
            Token.Type.L_CURLY -> parseObject(key)
            else -> throw IllegalStateException(ErrorUtil.createFullError(
                current.syntaxPosition, content, "unexpected token: ${current.value} expected identifier")
            )
        }
    }

    fun parseList(key: String): VericElement<*> {
        val elements: MutableList<VericElement<*>> = ArrayList()
        while (!stream.`is`(Token.Type.R_SQUARE)) {
            elements.add(this.parseUnKeyedObject())
        }

        if (stream.`is`(Token.Type.R_SQUARE)) stream.advance()

        return VericList(elements, key)
    }

    fun  parseUnKeyedObject(): VericElement<*> {
        val current = stream.token()
        val insideValues: MutableList<VericElement<*>> = ArrayList()

        if (stream.`is`(Token.Type.IDENTIFIER)) {
            while (!stream.`is`(Token.Type.R_CURLY)) {
                val expected = stream.token()
                stream.advance()

                if (expected!!.`is`(Token.Type.IDENTIFIER)) {
                    insideValues.add(parseEntry(expected.value))
                } else {
                    throw IllegalStateException(ErrorUtil.createFullError(
                        expected.syntaxPosition, content, "unexpected token: ${expected.value} expected identifier")
                    )
                }
            }

            if (stream.`is`(Token.Type.R_CURLY)) stream.advance()
        } else {
            throw IllegalStateException(ErrorUtil.createFullError(
                current!!.syntaxPosition, content, "unexpected token: ${current.value} expected identifier")
            )
        }

        return VericObject("un-keyed", insideValues)
    }

    fun parseObject(key: String): VericElement<*> {
        val current = stream.token()
        val insideValues: MutableList<VericElement<*>> = ArrayList()

        if (stream.`is`(Token.Type.IDENTIFIER)) {
            while (!stream.`is`(Token.Type.R_CURLY)) {
                val expected = stream.token()
                stream.advance()

                if (expected!!.`is`(Token.Type.IDENTIFIER)) {
                    insideValues.add(parseEntry(expected.value))
                } else {
                    throw IllegalStateException(ErrorUtil.createFullError(
                        expected.syntaxPosition, content, "unexpected token: ${expected.value} expected identifier")
                    )
                }
            }

            if (stream.`is`(Token.Type.R_CURLY)) stream.advance()
        } else {
            throw IllegalStateException(ErrorUtil.createFullError(
                current!!.syntaxPosition, content, "unexpected token: ${current.value} expected identifier")
            )
        }

        return VericObject(key, insideValues)
    }
}
