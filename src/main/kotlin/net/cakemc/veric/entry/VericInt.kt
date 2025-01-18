package net.cakemc.veric.entry

import net.cakemc.veric.Token

class VericInt : VericElement<Int> {
    private val key: String
    private val value: String

    constructor(key: String, value: Token) {
        this.key = key
        this.value = value.value
    }

    constructor(key: String, value: String) {
        this.key = key
        this.value = value
    }

    override fun value(): Int {
        return parseInteger(this.value)
    }

    override fun key(): String {
        return key
    }

    override fun type(): VericType {
        return VericType.INT
    }

    override fun toFormatedVericString(): String {
        return "$key = $value"
    }

    private fun parseInteger(value: String): Int {
        return if (value.startsWith("0x")) {
            value.substring(2).toInt(16)
        } else {
            value.toInt()
        }
    }
}
