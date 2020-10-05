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
    if (args.isEmpty()) {
        println("NO SOURCE")
        return
    }

    val result = try {
        transform(args[0], true)
    } catch (ex: ParseCancellationException) {
        println("SYNTAX ERROR")
        return
    } catch (ex: IncorrectTypeCancellationException) {
        println("TYPE ERROR")
        return
    }

    println(result)
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