package net.cakemc.veric.entry

import net.cakemc.veric.Token
import java.util.*

// TODO RECODE THIS TO USE @VericElements
class VericList : VericElement<List<Any?>> {
    private val key: String
    private val elements: List<Any>

    constructor(elements: List<Any>, key: String) {
        this.key = key
        this.elements = elements
    }

    constructor(key: String, elements: List<Token>) {
        this.key = key

        val objects: MutableList<Any> = ArrayList()
        for (current in elements) {
            val element: VericElement<*> = when (current.type) {

                Token.Type.CHARACTER -> VericCharacter(key, current)
                Token.Type.BOOLEAN -> VericBoolean(key, current)
                Token.Type.STRING -> VericString(key, current)
                Token.Type.LONG -> VericLong(key, current)
                Token.Type.INT -> VericInt(key, current)
                Token.Type.FLOAT -> VericFloat(key, current)
                Token.Type.DOUBLE -> VericDouble(key, current)

                else -> {
                    throw IllegalStateException(
                        ("invalid token for" +
                                " array creation: $key/${current.value}")
                    )
                }
            }

            objects.add(element.value()!!)
        }
        this.elements = objects
    }

    override fun toFormatedVericString(): String {
        val elements = StringBuilder()
        for (element in this.elements) {
            if (element is String) elements.append("\"").append(element).append("\"").append(" ").append("\n")
            else elements.append(element.toString()).append(" ")
        }
        return "$key = [\n$elements]"
    }

    override fun key(): String {
        return key
    }

    override fun type(): VericType {
        return VericType.LIST
    }

    override fun value(): List<Any?> {
        return elements
    }
}
