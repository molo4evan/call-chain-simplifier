import org.antlr.v4.runtime.misc.ParseCancellationException
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.nsu.fit.jbr.simplifier.ast.IncorrectTypeCancellationException
import ru.nsu.fit.jbr.simplifier.generation.CallChainEvaluator
import ru.nsu.fit.jbr.simplifier.getAst
import ru.nsu.fit.jbr.simplifier.transform

class CallChainTests {

    private val syntaxErrorCases = listOf(
        "",
        "map(element+1)",
        "filter{(eleent>3)}",
        "map{element}%>filter{(1=1)}",
        "map{(element-+3)}"
    )

    private val typeErrorCases = listOf(
        "map{(element>3)}",
        "map{((element>1)|(3<2))}",
        "filter{element}",
        "filter{((element>3)<4)}",
        "filter{((element>1)|(3+2))}"
    )

    private val commonCases = listOf(
        "filter{(element>10)}%>%filter{(element<20)}",
        "map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}",
        "filter{(element>0)}%>%filter{(element<0)}%>%map{(element*element)}",
        "filter{(element<30)}%>%map{(element+-10)}%>%filter{(element>10)}%>%map{(element*element)}",
        "filter{(element<-3)}%>%map{(element+10)}%>%filter{(element>10)}%>%map{(element*element)}"
    )

    private val testList = (-100..100).toList()

    @Test
    fun testCommonCases() {
        for ((index, case) in commonCases.withIndex()) {
            val transformed = transform(case, true)
            val optimized = transform(case, false)

            println()
            println("Case ${index + 1}")
            println("Source: $case")
            println("Transformed: $transformed")
            println("Optimized: $optimized")
            println("-----")

            val sOutput = mutableListOf<Int>()
            val tOutput = mutableListOf<Int>()
            val oOutput = mutableListOf<Int>()
            testList.forEach {
                val evaluator = CallChainEvaluator(it)
                evaluator.evaluate(case, sOutput)
                evaluator.evaluate(transformed, tOutput)
                evaluator.evaluate(optimized, oOutput)
            }

            assertIterableEquals(sOutput, tOutput)
            assertIterableEquals(sOutput, oOutput)
        }
    }

    private fun CallChainEvaluator.evaluate(callChain: String, output: MutableList<Int>) {
        getAst(callChain).accept(this)
        if (result != null) {
            output.add(result!!)
        }
    }

    @Test
    fun testSyntaxErrors() {
        for (case in syntaxErrorCases) {
            assertThrows<ParseCancellationException> {
                transform(case, true)
            }
        }
    }

    @Test
    fun testTypeErrors() {
        for (case in typeErrorCases) {
            assertThrows<IncorrectTypeCancellationException> {
                transform(case, true)
            }
        }
    }
}