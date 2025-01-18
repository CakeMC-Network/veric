package net.cakemc.veric

import java.util.*

class Token(type: Type, value: String, syntaxPosition: SyntaxPosition) {
    val syntaxPosition: SyntaxPosition = Objects.requireNonNull(syntaxPosition)
    val value: String = Objects.requireNonNull(value)
    val type: Type

    init {
        this.type = Objects.requireNonNull(type)
    }

    constructor(type: Type, syntaxPosition: SyntaxPosition) : this(type, "", syntaxPosition)

    fun `is`(type: Type): Boolean {
        return this.type == type
    }

    fun `is`(value: String): Boolean {
        return this.value == value
    }

    override fun toString(): String {
        return "{ type: $type, value: '$value' }"
    }

    enum class Type {
        // Whitespace
        WHITESPACE,
        EOF,

        // Atoms
        IDENTIFIER,
        CHARACTER,
        BOOLEAN,
        STRING,
        LONG,
        INT,
        FLOAT,
        DOUBLE,

        NULL,
        L_PAREN,
        R_PAREN,
        L_SQUARE,
        R_SQUARE,
        L_CURLY,
        R_CURLY,
        COLON,
        COMMA,
        AT,
        ASSIGN,
        DOT,
    }
}