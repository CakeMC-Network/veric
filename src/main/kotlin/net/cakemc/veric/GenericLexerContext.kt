package net.cakemc.veric

import java.util.function.Consumer
import java.util.regex.Pattern
import kotlin.math.max

open class GenericLexerContext<T>(protected val whitespace: T) {
    protected val rules: MutableList<LexerRule>

    init {
        this.rules = ArrayList()
    }

    open fun addRule(type: T, consumer: Consumer<LexerRule>): GenericLexerContext<T> {
        val rule = LexerRule(type)
        consumer.accept(rule)
        rules.add(rule)
        return this
    }

    fun parse(input: String): List<LexerToken?> {
        var input = input
        val tokenList: MutableList<LexerToken?> = ArrayList()
        var last_length = 0

        var lexerToken: LexerToken?
        while ((nextToken(input).also { lexerToken = it }) != null) {
            if (input.isEmpty() || (last_length == input.length)) break

            if (lexerToken!!.type !== whitespace) {
                tokenList.add(lexerToken)
            }

            last_length = input.length
            input = input.substring(lexerToken!!.length)
        }

        return tokenList
    }

    fun toImmutable(): GenericLexerContext<T> {
        return ImmutableGenericContext(this, whitespace)
    }

    fun nextToken(input: String): LexerToken? {
        var selectedRule: LexerRule? = null
        var longestRule = 1
        for (rule in rules) {
            val length = rule.getMatchLength(input)

            if (length >= longestRule) {
                longestRule = length
                selectedRule = rule
            }
        }

        return if (selectedRule == null) null
        else LexerToken(selectedRule.type, input.substring(0, longestRule))
    }

    inner class LexerToken(val type: T, val content: String) {
        val length: Int = content.length
    }

    inner class LexerRule(val type: T) {
        val matches: MutableList<Pattern> = ArrayList()

        fun addString(value: String?): LexerRule {
            matches.add(StringUtils.regexEscape(value).let { Pattern.compile(it) })
            return this
        }

        fun addStrings(vararg values: String?): LexerRule {
            for (value in values) {
                addString(value)
            }

            return this
        }

        fun addRegex(regex: String): LexerRule {
            matches.add(Pattern.compile(regex))
            return this
        }

        fun addRegexes(vararg regexes: String): LexerRule {
            for (regex in regexes) {
                addRegex(regex)
            }

            return this
        }

        fun addSingleline(open: String?, close: String?): LexerRule {
            return addSingleline(open, "", close)
        }

        fun addSingleline(open: String?, escape: String, close: String?): LexerRule {
            return addDelimiter(open, escape, close, 0)
        }

        fun addMultiline(open: String?, close: String?): LexerRule {
            return addMultiline(open, "", close)
        }

        fun addMultiline(open: String?, escape: String, close: String?): LexerRule {
            return addDelimiter(open, escape, close, Pattern.DOTALL)
        }

        private fun addDelimiter(open: String?, escape: String, close: String?, flags: Int): LexerRule {
            val s = StringUtils.regexEscape(open)
            val c = StringUtils.regexEscape(close)

            val regex: String
            if (escape.isEmpty()) {
                regex = "$s.*?$c"
            } else {
                val e = StringUtils.regexEscape(escape)
                regex = "$s(?:$e(?:$e|$c|(?!$c).)|(?!$e|$c).)*$c"
            }

            matches.add(Pattern.compile(regex, flags))
            return this
        }

        fun getMatchLength(string: String): Int {
            var length = 0
            for (pattern in matches) {
                val matcher = pattern.matcher(string)
                if (matcher.lookingAt()) {
                    length = max(length.toDouble(), matcher.end().toDouble()).toInt()
                }
            }

            return if (length < 1) -1 else length
        }
    }

    class ImmutableGenericContext<T>(context: GenericLexerContext<T>, val whitespaceType: T) : GenericLexerContext<T>(whitespaceType) {
        init {
            rules.addAll(context.rules)
        }

        override fun addRule(type: T, consumer: Consumer<LexerRule>): GenericLexerContext<T> {
            throw UnsupportedOperationException()
        }
    }
}