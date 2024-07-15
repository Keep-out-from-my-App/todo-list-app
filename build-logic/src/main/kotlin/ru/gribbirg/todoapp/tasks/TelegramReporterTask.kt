package ru.gribbirg.todoapp.tasks

import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import ru.gribbirg.todoapp.api.TelegramApi
import ru.gribbirg.todoapp.utils.findApk
import javax.inject.Inject

abstract class TelegramReporterTask @Inject constructor(
    private val telegramApi: TelegramApi
) : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:OutputFile
    abstract val apkSizeFile: RegularFileProperty

    @get:Input
    abstract val token: Property<String>

    @get:Input
    abstract val chatId: Property<String>

    @TaskAction
    fun report() {
        val token = token.get()
        val chatId = chatId.get()
        val file = apkDir.get().findApk()

        runBlocking {
            telegramApi.sendMessage(
                "Build finished!\nApk size: ${apkSizeFile.get().asFile.readText()} KB",
                token,
                chatId
            ).apply {
                println(bodyAsText())
            }
        }
        runBlocking {
            telegramApi.upload(file, token, chatId).apply {
                println(bodyAsText())
            }
        }
    }
}