package com.jdfx

import com.jdfx.gui.fx.AppGUI
import com.jdfx.util.CommandLine
import com.jdfx.util.CustomException
import com.jdfx.util.Logger.debugln
import com.jdfx.util.Logger.infoln
import com.jdfx.util.Logger.warningln
import javafx.application.Application
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object JDFX {
    const val TITLE = "Java Decompiler - FX"
    const val VERSION = "1.0.0"
    private var GRAPHICAL_MODE = true
    private var isContinueMode = false

    private fun getVersion(): String {
        return "$TITLE v$VERSION"
    }

    private fun printUsage() {
        println("\nUsage: java JDFX [-t] [-file] [-o]")
        println("\t-t : start in terminal mode")
        println("\t-file : file to be decompiled")
        println("\t-o : fernflower options for decompiling")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        infoln("${getVersion()}\n")

        var argPos = 0
        while (argPos < args.size) {
            when {
                args[argPos].equals("-t", ignoreCase = true) -> GRAPHICAL_MODE = false
                args[argPos].equals("-file", ignoreCase = true) -> {
                    //TODO: Logic for dealing with immediate decompiling of file
                }
                else -> {
                    if (args[argPos].equals("-o", ignoreCase = true)) {

                    } else {
                        warningln("Argument '${args[argPos]}' not recognized")
                        printUsage()
                        return
                    }

                }
            }
            ++argPos
        }

        val commandLine = CommandLine()

        if (GRAPHICAL_MODE) {
            debugln("Loading graphical interface...\n", this::class.java)
            Application.launch(AppGUI::class.java)
            debugln("Closing graphical interface...\n", this::class.java)
        } else {
            try {
                val reader = BufferedReader(InputStreamReader(System.`in`))
                var commandOutput: String

                while (true) {
                    if (!isContinueMode) {
                        print(CommandLine.PROMPT)
                    }

                    val line = reader.readLine()
                    if (line != null) {
                        commandLine.scheduleCommand(line)
                    }

                    while (commandLine.hasMoreCommands() && (!isContinueMode || commandLine.hasQueuedStop())) {
                        val command = commandLine.nextCommand

                        commandOutput = try {
                            commandLine.runCommand(command)
                        } catch (customException: CustomException) {
                            customException.exceptionDescription
                        } catch (numberFormatException: NumberFormatException) {
                            "NumberFormatException: ${numberFormatException.message}"
                        }

                        if (commandOutput.isEmpty()) {
                            println("Bye!")
                            return
                        }

                        println(commandOutput)
                    }
                }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }
        }
    }
}
