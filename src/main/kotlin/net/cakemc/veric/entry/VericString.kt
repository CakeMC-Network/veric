package net.cakemc.veric.entry

import net.cakemc.veric.Token

class VericString : VericElement<String> {
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
        return VericType.STRING
    }

    override fun value(): String {
        return value.substring(1, value.length - 1)
    }

    override fun toFormatedVericString(): String {
        return "$key = $value"
    }

    override fun toString(): String {
        return "VericString{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}'
    }
}
