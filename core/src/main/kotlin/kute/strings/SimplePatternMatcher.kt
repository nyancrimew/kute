// Copyright (C) 2019 Till Kottmann <me@deletescape.ch>
//
// SPDX-License-Identifier: MIT

package kute.strings

/**
 * A matcher for simple patterns *(inspired by unix glob syntax)*
 *
 * # Syntax:
 *
 * Per default matching happens left (start) anchored, essentially calling [String.startsWith], with all escape sequences expanded.
 * Available wildcards:
 *  - `?` will match any **one** character.
 *  - `*` on the other hand will matches any number of characters required to find the next known character. No backtracking happens.
 * > Note: Prefixing a pattern with `*` will anchor it to the end, unless `*` is added to the end as well
 *
 * @param pattern A pattern following the mentioned syntax
 */
class SimplePatternMatcher(pattern: CharSequence) {
    private val impl = compile(pattern)

    /**
     * Tests whether given [str] matches this pattern
     */
    fun matches(str: CharSequence) = impl.matches(str)

    private interface MatcherImpl {
        fun matches(str: CharSequence): Boolean
    }

    private class MatchMinLength(
        private val minLength: Int
    ) : MatcherImpl {
        override fun matches(str: CharSequence) = str.length >= minLength
    }

    private class StartMatcherImpl(
        private val pattern: CharSequence
    ) : MatcherImpl {
        override fun matches(str: CharSequence)= str.startsWith(pattern)
    }

    private class TokenMatcherImpl(
        private val tokens: List<Token>,
        private val totalLength: Int,
        private val anchorEnd: Boolean
    ) : MatcherImpl {
        override fun matches(str: CharSequence): Boolean {
            if (str.length < totalLength) return false
            val tokenStack = tokens.toMutableList()
            val iter = str.iterator()
            while (tokenStack.size > 0) {
                var idx = 0
                var tok = tokenStack.removeAt(0)

                if (tok is Token.Wildcard && tok.length == -1) {
                    // We match up to the end, return
                    if (tokenStack.size == 0) return true

                    var minChars = 0
                    tok = tokenStack.removeAt(0)
                    // Parse all sequential wildcard tokens at once, counting the minimum chars we need to skip
                    var isLast = false
                    while (tok is Token.Wildcard) {
                        if (tok.length > 0) {
                            minChars += tok.length
                        }
                        if (tokenStack.size == 0) {
                            // We match up to the end, return
                            if (minChars == 0) return true
                            isLast = true
                            break
                        }
                        tok = tokenStack.removeAt(0)
                    }
                    var chars = 0
                    while (iter.hasNext()) {
                        if (tok.matches(iter.nextChar(), idx) && chars >= minChars) {
                            // We match up to the end, return
                            if (isLast) return true
                            idx++
                            break
                        }
                        chars++
                    }
                }
                for (i in idx until tok.length) {
                    if (!iter.hasNext()) return false
                    if (!tok.matches(iter.nextChar(), i)) return false
                }
            }
            return tokenStack.size == 0 && (!iter.hasNext() || !anchorEnd)
        }
    }

    private sealed class Token(
        internal val length: Int
    ) {
        abstract fun matches(c: Char, i: Int): Boolean

        class Wildcard(
            length: Int
        ): Token(length) {
            override fun matches(c: Char, i: Int) = length == -1 || i < length
        }

        class Characters(
            length: Int,
            private val content: CharArray
        ): Token(length){
            override fun matches(c: Char, i: Int) = content.getOrNull(i) == c

            // TODO: replace with concatToString once that isn't experimental anymore
            fun getContent(): CharSequence = content.joinToString("")
        }
    }


    companion object {
        private const val WILDCARD_CONT = '*'
        private const val WILDCARD_SINGLE = '?'
        private const val ESCAPE_CHAR = '\\'

        private fun compile(pattern: CharSequence): MatcherImpl {
            val anchoredStart = !pattern.startsWith(WILDCARD_CONT)
            // If a pattern isn't anchored to the start it is automatically anchored to the end
            val anchoredEnd = !pattern.endsWith(WILDCARD_CONT) && !anchoredStart

            var lenTotal = 0
            val tokens = mutableListOf<Token>()
            var escapeFlag = false
            var wildcardFlag = false
            var tokenLen = 0
            val content = mutableListOf<Char>()
            fun newToken() {
                if (tokenLen > 0) {
                    tokens += if (wildcardFlag) {
                        Token.Wildcard(tokenLen)
                    } else Token.Characters(tokenLen, content.toCharArray())
                } else if (wildcardFlag && tokenLen == -1) {
                    tokens += Token.Wildcard(tokenLen)
                    tokenLen = 0
                }

                lenTotal += tokenLen
                tokenLen = 1
                content.clear()
            }
            for (c in pattern) {
                tokenLen++
                when (c) {
                    ESCAPE_CHAR -> {
                        if (escapeFlag) {
                            content.add(c)
                        } else {
                            tokenLen--
                            if (wildcardFlag) {
                                newToken()
                                wildcardFlag = false
                                tokenLen = 0
                            }
                        }
                        escapeFlag = !escapeFlag
                    }
                    WILDCARD_SINGLE -> {
                        if (escapeFlag) {
                            content.add(c)
                            escapeFlag = false
                        } else if (!wildcardFlag) {
                            tokenLen--
                            newToken()
                            wildcardFlag = true
                        }
                    }
                    WILDCARD_CONT -> {
                        if (escapeFlag) {
                            content.add(c)
                            escapeFlag = false
                        } else {
                            tokenLen--
                            newToken()
                            tokenLen = -1
                            wildcardFlag = true
                            newToken()
                            wildcardFlag = false
                            tokenLen = 0
                        }
                    }
                    else -> {
                        if (wildcardFlag) {
                            tokenLen--
                            newToken()
                            wildcardFlag = false
                        }
                        escapeFlag = false
                        content.add(c)
                    }
                }
            }
            newToken()
            return if (tokens.size == 1) {
                val token = tokens.first()
                if (token is Token.Wildcard)
                    MatchMinLength(token.length)
                else StartMatcherImpl((token as Token.Characters).getContent())
            } else TokenMatcherImpl(tokens, lenTotal, anchoredEnd)
        }
    }
}