package com.example.dieter.utils

import java.util.Random

/**
 *  * New version: 30/May/2017
 *    * Made it more kotlin-like with same functionality ^^
 *
 * based on: https://gist.github.com/RomansBermans/6f3836188427fbd3b1efcf7e6418f06d
 * Fancy ID generator that creates 20-character string identifiers with the following properties:
 *
 * 1. They're based on timestamp so that they sort *after* any existing ids.
 * 2. They contain 72-bits of random data after the timestamp so that IDs won't collide with other clients' IDs.
 * 3. They sort *lexicographically* (so the timestamp is converted to characters that will sort properly).
 * 4. They're monotonically increasing.  Even if you generate more than one in the same timestamp, the
 *    latter ones will sort after the former ones.  We do this by using the previous random bits
 *    but "incrementing" them by 1 (only in the case of a timestamp collision).
 */
object FirebaseIDGenerator {
    data class State(
        // Timestamp of last push, used to prevent local collisions if you push twice in one ms.
        val lastInstant: Long = -1L,
        // We generate 72-bits of randomness which get turned into 12 characters and appended to the
        // timestamp to prevent collisions with other clients.  We store the last characters we
        // generated because in the event of a collision, we'll use those same characters except
        // "incremented" by one.
        val lastRandChars: IntArray = IntArray(12)) {

        // We don't want to use the array in our equals/hashCode
        override fun equals(other: Any?) = lastInstant == (other as? State)?.lastInstant

        override fun hashCode() = lastInstant.toInt()
    }

    data class Result(val id: String, val nextState: State)

    // Modeled after base64 web-safe chars, but ordered by ASCII.
    private val PUSH_CHARS = "-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz"
    private val DEFAULT_INSTANT = { System.currentTimeMillis() }
    private val STATE_LOCK = Any()

    private var globalState = State()

    /**
     * Generate a new Firebase Id using a previous state. This method has no side-effects.
     *
     * @param[previousState] The previous state that will be modified.
     * @param[instant] The instant the id is being generated, otherwise [System.currentTimeMillis]
     *                 will be used.
     */
    fun generateNextId(previousState: State, instant: Long = DEFAULT_INSTANT()): Result {
        val duplicateTime = (instant == previousState.lastInstant)

        // Generate the first 8 characters
        val timeStampChars = CharArray(8).also { arr ->
            var instantLeft = instant
            (7 downTo 0).forEach {
                val module = instantLeft % 64L
                instantLeft /= 64L
                arr[it] = PUSH_CHARS[module.toInt()]
            }
            if (instantLeft != 0L) {
                throw AssertionError("We should have converted the entire timestamp.")
            }
        }

        // Generate the last 12 characters
        val randChars = when (!duplicateTime) {
            true -> Random().let { r -> IntArray(12) { r.nextInt(64) } }
            else -> previousState.lastRandChars.copyOf()
                .also { arr ->
                    val lastNot63 = arr.indexOfLast { it != 63 }
                    arr.fill(element = 0, fromIndex = lastNot63 + 1)
                    arr[lastNot63]++
                }
        }

        val randCharsAsString = randChars.fold(StringBuilder(12)) { str, i -> str.append(PUSH_CHARS[i]) }

        // Join both characters lists
        val id = String(timeStampChars) + randCharsAsString
        require(id.length == 20) { "Length should be 20." }

        return Result(id = id, nextState = State(lastInstant = instant, lastRandChars = randChars))
    }

    /**
     * Generate a new Firebase Id using a global state. This method has side-effects.
     *
     * @param[instant] The instant the id is being generated, otherwise [System.currentTimeMillis]
     *                 will be used.
     * @param[threadSafe] Determines if a lock will be used for each execution, making it thread safe ^^
     */
    fun generateId(instant: Long = DEFAULT_INSTANT(), threadSafe: Boolean = false): String {
        val op = {
            generateNextId(globalState, instant)
                .let { result ->
                    globalState = result.nextState
                    result.id
                }
        }

        return when (threadSafe) {
            true -> synchronized(STATE_LOCK) { op() }
            else -> op()
        }
    }
}