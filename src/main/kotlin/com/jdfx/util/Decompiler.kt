package com.jdfx.util

import com.jdfx.util.Loader.loadRes
import com.jdfx.util.Logger.infoln
import java.io.File
import java.io.IOException


class Decompiler(private val jarFile: File, private val projectDir: File) : Runnable {

    private val fernflowerJar = File(loadRes("jars/fernflower.jar"))

    /**
     * STEP 1. decompile with fernflowerJar and place decompiled zip file in project dir
     * STEP 2. unpack jar in project dir
     * STEP 3. clean up decompiled jar
     */
    override fun run() {

        if (projectDir.exists()) {
            // clear project directory
            // TODO: Make dialog to ask the user if they are sure they want the directory to be cleared
            cleanup(projectDir)
        } else {
            // Create project directory
            projectDir.mkdir()
        }
        projectDir.deleteOnExit()

        // Dump jar file to project location
        "jar xf $jarFile".runCommand(projectDir)

        // STEP: 1
//        infoln("java -jar $fernflowerJar $options $jarFile $projectDir".runCommand(projectDir))
        infoln("java -jar $fernflowerJar $options $projectDir".runCommand(projectDir))
        // STEP: 2
        // Get the decompiled jar as a File object
        val decompiledJar = File("$projectDir/${jarFile.name}")
//        infoln("jar xf $decompiledJar".runCommand(projectDir))
        // STEP: 3
        infoln("Deleting source Jar: ${decompiledJar.name}")
        decompiledJar.delete()
    }

    fun cleanup(projectDir: File) {
        projectDir.delete()
    }

    private fun String.runCommand(workingDir: File): String {
        return try {
            // TODO: Investigate if there is a need to add in a time for the execution of a
            //  command and the option for an override time if it fails
            val parts = this.split("\\s".toRegex())
            val proc = ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()
//                .waitFor(executionTimeOverride, TimeUnit.SECONDS)

            proc.inputStream.bufferedReader().readText()
        } catch (e: IOException) {
            e.printStackTrace()
            "running command"
        }
    }

    companion object {
        var options = ""
    }
}
