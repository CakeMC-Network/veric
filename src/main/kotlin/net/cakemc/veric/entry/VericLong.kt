package net.cakemc.veric.entry

import net.cakemc.veric.Token

class VericLong : VericElement<Long> {
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

    override fun key(): String {
        return key
    }

    override fun type(): VericType {
        return VericType.LONG
    }

    override fun toFormatedVericString(): String {
        return "$key = $value" + "L"
    }

    override fun value(): Long {
        return parseLong(this.value)
    }

    private fun parseLong(value: String): Long {
        return if (value.startsWith("0x")) {
            value.substring(2, value.length - 1).toLong(16)
        } else {
            value.substring(0, value.length - 1).toLong()
        }
    }
}
