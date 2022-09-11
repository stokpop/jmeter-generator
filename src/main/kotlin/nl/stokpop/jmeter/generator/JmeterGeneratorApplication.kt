package nl.stokpop.jmeter.generator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JmeterGeneratorApplication

fun main(args: Array<String>) {
	runApplication<JmeterGeneratorApplication>(*args)
}
