package com.adlerd.jdfx.util

import com.adlerd.jdfx.JDFX
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.system.exitProcess

/**
 * Class to add better forms of output to the system console
 */
object Logger {
    private val DEFAULT_LOG_DIR = "${System.getProperty("user.home")}/.logs/"
    private var isLogOpen = false
    private var logFolder = File(DEFAULT_LOG_DIR)
        set(value) {
            field = when {
                // Check if the input is a valid directory
                value.isDirectory -> value
                // Check if the input is a file and set it to default directory
                value.isFile -> {
                    errorln(
                        "Log directory cannot be a file: \"${value.absolutePath}\"",
                        false
                    )
                    File(DEFAULT_LOG_DIR)
                }
                // Otherwise set the directory to the default directory
                else -> {
                    errorln("Invalid log directory: \"${value.absolutePath}\"", false)
                    infoln("Defaulting to log directory of: \"$DEFAULT_LOG_DIR\"")
                    File(DEFAULT_LOG_DIR)
                }
            }
        }
    private var logWriter: PrintWriter? = null


    /**
     * Prints the given message to the standard output stream.
     */
    fun outln(message: Any, shouldLog: Boolean = true) {
        if (shouldLog) {
            println(log(">>> $message"))
        } else {
            println(">>> $message")
        }
    }

    /**
     * Prints the given message to the standard output stream.
     */
    fun infoln(message: Any) {
        println(log("[ ${getTime()} INFO ]: $message"))
    }

    /**
     * Prints the given message to the standard output stream.
     * YES THE LINE NUMBER PORTION IS BROKEN!
     * TODO: Fix line number issue
     */
    fun debugln(message: Any?, classObject: Class<out Any>) {
        println(
            log(
                "[ ${getTime()} DEBUG ]: (${classObject.name.split(
                    '.'
                ).asReversed()[0]}: Line ${Thread.currentThread().stackTrace[1].lineNumber - 9} ) $message"
            )
        )
    }

    /**
     * Prints the given message to the standard output stream.
     */
    fun warningln(message: Any?) {
        System.err.println(log("[ ${getTime()} WARNING ]: $message"))
    }

    /**
     * Prints the given message to the standard output stream.
     */
    fun errorln(message: Any?, exit: Boolean = false, status: Int = -1) {
        if (exit) {
            System.err.println(log("[ ${getTime()} FATAL ERROR ]: $message"))
            exitProcess(status = status)
        } else System.err.println(log("[ ${getTime()} FATAL ERROR ]: $message"))
    }

    fun getTime(): String {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    }

    private fun getDate(): String {
        val rawTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val time = StringBuilder()
        for (char in rawTime) {
            if (char == ':') {
                time.append('_')
            } else {
                time.append(char)
            }
        }

        return time.toString()
    }

    /**
     * Initializes the output stream and file for logging
     *
     * This should be declared on the first line of your main method for easy use
     */
    fun start() {
//        if (!isLogOpen) {
        if (!logFolder.exists()) {
            assert(logFolder.mkdirs())
        }

        try {
            isLogOpen = true
//                logWriter = PrintWriter("${logFolder.absolutePath}/tcsim ${getDate()} ${getTime()}.txt")
            logWriter = PrintWriter("${JDFX.TITLE} ${getDate()} ${getTime()}.txt")
            debugln("Successfully created log writer!", this::class.java)
        } catch (e: IOException) {
            errorln(
                "Failed to initialize the log file at $logFolder",
                true
            )
        }
//        } else if (isLogOpen) {
//            debugln("Log is already open!", this::class.java)
//        } else {
//            warningln("Unknown log state!")
//            if (logWriter != null) {
//                logWriter?.close()
//            }
//            isLogOpen = false
//        }
    }

    fun close() {
        logWriter?.close()
    }

    fun log(message: Any?): Any {
        if (logWriter != null) {
            logWriter?.println(message.toString())
        }
        return message!!
    }
}