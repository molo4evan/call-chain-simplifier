package ru.nsu.fit.jbr.simplifier

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.misc.ParseCancellationException
import ru.nsu.fit.jbr.simplifier.antlr.CallChainLexer
import ru.nsu.fit.jbr.simplifier.antlr.CallChainParser
import ru.nsu.fit.jbr.simplifier.antlrinterop.AstCreator
import ru.nsu.fit.jbr.simplifier.antlrinterop.ThrowingErrorListener
import ru.nsu.fit.jbr.simplifier.ast.*
import ru.nsu.fit.jbr.simplifier.generation.ChainCodeVisitor
import ru.nsu.fit.jbr.simplifier.transformation.CallOptimizer
import ru.nsu.fit.jbr.simplifier.transformation.FilterCreator
import ru.nsu.fit.jbr.simplifier.transformation.MapCreator

fun main(args: Array<String>) {
    val (helpNeeded, optimizationDisabled, source) = try {
        parseArgs(args)
    } catch (ex: IllegalArgumentException) {
        println("Passed arguments are invalid: ${ex.message}")
        return
    }

    if (helpNeeded) {
        printHelp()
        return
    }

    val result = try {
        transform(source, optimizationDisabled)
    } catch (ex: ParseCancellationException) {
        println("SYNTAX ERROR: ${ex.message}")
        return
    } catch (ex: IncorrectTypeCancellationException) {
        println("TYPE ERROR: ${ex.message}")
        return
    }

    println("---")
    println("Source: $source")
    println("Result: $result")
    println("---")
}

fun printHelp() {
    println("Usage:")
    println("\t-h -> show this help (help also shows when no args passed).")
    println("\tanystring -> simplify 'anystring' source with optimization.")
    println("\t-w anystring -> simplify 'anystring' source without optimization. " +
                "Please note that it can produce very complex expressions with nonsense parts.")
    println()
    println("Possible outputs:")
    println("\tIn case of successful transformation - transformed source.")
    println("\tIn case of syntax error in source - SYNTAX ERROR: reason of error.")
    println("\tIn case of type error in source - TYPE ERROR: reason of error.")
}

fun parseArgs(args: Array<String>): Triple<Boolean, Boolean, String> {
    if (args.isEmpty()) return Triple(true, false, "")

    return when (args[0]) {
        "-h" -> Triple(true, false, "")
        "-w" -> if (args.size < 2) {
            throw IllegalArgumentException("No source")
        } else {
            Triple(false, true, args[1])
        }
        else -> Triple(false, false, args[0])
    }
}

fun transform(source: String, optimizationDisabled: Boolean): String {
    val ast = getAst(source)

    val transformed = simplifyCallChain(ast)

    val optimized = if (optimizationDisabled) {
        transformed
    } else {
        CallOptimizer().visit(transformed)
    }

    val chainCodeVisitor = ChainCodeVisitor()
    chainCodeVisitor.visit(optimized)
    return chainCodeVisitor.stringBuilder.toString()
}

fun getAst(source: String): CallChain {
    val lexer = CallChainLexer(CharStreams.fromString(source))
    lexer.removeErrorListeners()
    lexer.addErrorListener(ThrowingErrorListener.INSTANCE)
    val tokens = CommonTokenStream(lexer)

    val parser = CallChainParser(tokens)
    parser.removeErrorListeners()
    parser.addErrorListener(ThrowingErrorListener.INSTANCE)
    val callChain = parser.callChain()

    return AstCreator().visitCallChain(callChain)
}

fun simplifyCallChain(ast: CallChain): CallChain {
    val mapCreator = MapCreator()
    mapCreator.visit(ast.copy())

    val filterCreator = FilterCreator()
    filterCreator.visit(ast.copy())

    val map = if (mapCreator.map == null) {
        MapCall(Element)
    } else {
        mapCreator.map!!
    }

    val filter = if (filterCreator.filter == null) {
        FilterCall(CallOptimizer.trueCmp())
    } else {
        filterCreator.filter!!
    }

    return CallChain(mutableListOf(filter, map))
}