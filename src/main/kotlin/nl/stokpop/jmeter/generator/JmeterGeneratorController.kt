package nl.stokpop.jmeter.generator

import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest


@RestController
class JmeterGeneratorController(val storage: FileStorage) {

    val zipFiles: ConcurrentHashMap<String, Path> = ConcurrentHashMap()

    @PostMapping("/upload")
    fun upload(@RequestParam file: MultipartFile): FileUploadResponse {
        // based on: https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/
        val projectId = "jmeter-gen." + System.currentTimeMillis()
        val workDir = storage.createProject(file, projectId)

        // now call generator
        executeCommand(
            workDir,
            "java -jar /openapi-generator-cli.jar generate -i swagger.json -g jmeter2"
        )

        // now call jmeter-dsl
        executeCommand(
            workDir,
            "/jmeter-dsl/generate-from-dsl.sh /jmeter-dsl jmeter-dsl-script.kts /jmeter-dsl"
        )

        // now zip
        val zipFile = storage.zip(workDir)
        zipFiles[projectId] = zipFile

        // now return zip download url
        return FileUploadResponse(projectId)
    }

    private fun executeCommand(workDir: Path, command: String) {
        val process = ProcessBuilder(command.split(" "))
            .directory(workDir.toFile())
            .redirectErrorStream(true)
            .start();

        try {
            val hasRunToExit = process.waitFor(5, TimeUnit.MINUTES)

            if (!hasRunToExit) throw JmeterGeneratorException()

            val exitValue = process.exitValue()

            if (exitValue != 0) {
                throw JmeterGeneratorException()
            }
        } finally {
            val output = extractOutput(process)
            print(output)
            process.destroy()
        }
    }

    private fun extractOutput(process: Process): String {
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val output = reader.lines().collect(Collectors.joining(System.getProperty("line.separator")))
        return output
    }

    @GetMapping("/download/{projectId:.+}")
    fun download(@PathVariable projectId: String, request: HttpServletRequest): ResponseEntity<Resource> {

        val zipFile = zipFiles[projectId] ?: throw RuntimeException("ProjectId unknown: $projectId")

        val resource = storage.fileAsResource(zipFile)

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$projectId.zip")
            .body(resource)
    }
}