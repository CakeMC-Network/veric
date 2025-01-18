package net.cakemc.veric

import net.cakemc.veric.GenericLexerContext.LexerRule
import java.util.function.Consumer

object Lexer {
    val LEXER: GenericLexerContext<Token.Type> =
        GenericLexerContext(Token.Type.WHITESPACE) // Whitespaces
            .addRule(Token.Type.WHITESPACE, Consumer { i ->
                i
                    .addMultiline("/*", "*/")
                    .addRegex("//[^\\r\\n]*")
                    .addRegex("[ \t\r\n]+")
            }) // Atoms

            .addRule(Token.Type.IDENTIFIER) { i -> i.addRegex("[a-zA-Z_][a-zA-Z0-9_]*") }
            .addRule(Token.Type.BOOLEAN) { i -> i.addStrings("true", "false") }
            .addRule(Token.Type.CHARACTER) { i -> i.addMultiline("'", "\\", "'") }
            .addRule(Token.Type.STRING) { i -> i.addMultiline("\"", "\\", "\"") }
            .addRule(Token.Type.DOUBLE) { i -> i.addRegex("[0-9]+(\\.[0-9]+)?[dD]?") }
            .addRule(Token.Type.FLOAT) { i -> i.addRegex("[0-9]+(\\.[0-9]+)?[fF]") }

            .addRule(Token.Type.LONG, Consumer { i ->
                i.addRegexes(
                    "0x[0-9a-fA-F]+[Ll]",
                    "0b[0-1]+[Ll]",
                    "[0-9]+[Ll]"
                )
            })
            .addRule(Token.Type.INT, Consumer { i ->
                i.addRegexes(
                    "0x[0-9a-fA-F]+",
                    "0b[0-1]+",
                    "[0-9]+"
                )
            })

            .addRule(Token.Type.NULL) { i -> i.addString("null") } // Memory operations

            .addRule(Token.Type.ASSIGN) { i -> i.addString("=") } // Brackets

            .addRule(Token.Type.L_PAREN) { i -> i.addString("(") }
            .addRule(Token.Type.R_PAREN) { i -> i.addString(")") }
            .addRule(Token.Type.L_SQUARE) { i -> i.addString("[") }
            .addRule(Token.Type.R_SQUARE) { i -> i.addString("]") }
            .addRule(Token.Type.L_CURLY) { i -> i.addString("{") }
            .addRule(Token.Type.R_CURLY) { i -> i.addString("}") }

            .addRule(Token.Type.COLON) { i -> i.addString(":") }
            .addRule(Token.Type.COMMA) { i -> i.addString(",") }
            .addRule(Token.Type.DOT) { i -> i.addString(".") }
            .addRule(Token.Type.AT) { i -> i.addString("@") }

            .toImmutable()
}