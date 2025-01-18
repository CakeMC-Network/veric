package net.cakemc.veric.entry

import net.cakemc.veric.Token

class VericBoolean : VericElement<Boolean> {
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

    override fun value(): Boolean {
        return value.equals("true", ignoreCase = true)
    }

    override fun toFormatedVericString(): String {
        return "$key = $value"
    }

    override fun key(): String {
        return key
    }

    override fun type(): VericType {
        return VericType.BOOLEAN
    }
}

