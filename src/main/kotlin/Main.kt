import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

const val firstNumDelay = 2500L
const val secondNumDelay = 3000L
const val sleepingDelay = 500L

// Suspend functions
suspend fun firstNumber(): Int {
    delay(firstNumDelay)
    return (0..10).random()
}
suspend fun secondNumber(): Int {
    delay(secondNumDelay)
    return (0..10).random()
}

fun main() = runBlocking {

    val currentThread = Thread.currentThread().name

    launch {
        delay(1000L)
        println("World")
    }
    println("Hello, ")
    Thread.sleep(1000L)

    val executionTimeSequential = measureTimeMillis {
        val firstNum = firstNumber()
        val secondNum = secondNumber()
        println("Sum (sequential) = ${firstNum + secondNum}")
    }

    val executionTimeConcurrent = measureTimeMillis {
        coroutineScope {
            val firstNum: Deferred<Int> = async {
                firstNumber()
            }
            val secondNum: Deferred<Int> = async {
                secondNumber()
            }
            val result = firstNum.await() + secondNum.await()
            println("Sum (concurrent) = $result")
        }
    }

    println("Execution time (concurrent calls): $executionTimeConcurrent")
    println("Execution time (sequential calls): $executionTimeSequential")
    println("Sequential execution time equals to secondNumDelay const as it`s the longest execution of concurrent calls")

    GlobalScope.launch {
        repeat(3) { i ->
            println("I`m sleeping $i...")
            delay(sleepingDelay)
        }
    }
    delay(1700L)
    println("${currentThread}: I'm tired of waiting! I'm running finally")
    delay(sleepingDelay)
    println("${currentThread}: Now I can quit.")
}