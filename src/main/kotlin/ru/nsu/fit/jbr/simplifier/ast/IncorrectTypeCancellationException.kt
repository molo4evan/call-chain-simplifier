package ru.nsu.fit.jbr.simplifier.ast

import java.util.concurrent.CancellationException

/**
 * Marks type error occurred while parsing program.
 */
class IncorrectTypeCancellationException(message: String?): CancellationException(message) {
    constructor(): this(null)
}