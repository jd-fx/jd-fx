package com.jdfx.util

import java.util.*

class CommandLine {

    private var commandQueue = LinkedList<String>()
    private var prevHistoryStack = Stack<String>()
    private var nextHistoryStack = Stack<String>()
    private var commands = Hashtable<String, Command>()
    val nextCommand: String
        get() = this.commandQueue.removeFirst()
    val previousHistory: String
        get() {
            if (this.prevHistoryStack.isEmpty()) {
                return ""
            }
            val str = this.prevHistoryStack.pop()
            this.nextHistoryStack.push(str)
            return str
        }
    val nextHistory: String
        get() {
            if (this.nextHistoryStack.isEmpty()) {
                return ""
            }
            val str = this.nextHistoryStack.pop()
            this.prevHistoryStack.push(str)
            return str
        }

    /**
     * Add a command to the command queue
     * @param command command to be queued
     */
    fun scheduleCommand(command: String) {
        if (command.equals("stop", ignoreCase = true)) {
            this.commandQueue.addFirst(command)
        } else {
            this.commandQueue.add(command)
        }
    }

    /**
     * Returns if the command queue still has commands in it
     */
    fun hasMoreCommands(): Boolean {
        return this.commandQueue.size != 0
    }

    /**
     * returns if the first command in the command queue in "stop"
     */
    fun hasQueuedStop(): Boolean {
        return this.commandQueue.first.equals("stop", ignoreCase = true)
    }

    /**
     * Add a command to the command queue
     * @param commandStr -> command bring added to the queue
     */
    fun addToHistory(commandStr: String) {
        if (this.prevHistoryStack.empty()) { // If the command queue is empty add the command
            this.prevHistoryStack.push(commandStr)
        } else if (!this.prevHistoryStack.peek().equals(
                commandStr,
                ignoreCase = true
            )
        ) {  // If the command queue is empty add the command
            this.prevHistoryStack.push(commandStr)
        }
    }

    /**
     * Resets the history stack and
     */
    fun resetHistoryStack() {
        while (!this.nextHistoryStack.empty()) {
            this.prevHistoryStack.push(this.nextHistoryStack.pop())
        }
    }

    /**
     * Run the passed command
     * @param commandStr -> command to be run
     */
    fun runCommand(commandStr: String): String {
//        var paramString1: String = commandStr ?: return ""

        if (!commandStr.startsWith("@")) {
            resetHistoryStack()
            addToHistory(commandStr)
        } else {
            commandStr.replaceFirst("^@".toRegex(), "")
        }

        // Take the inputed command and turn it into an array of strings
        var commandArray = commandStr.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var size = commandArray.size

        // If the array is empty return a blank String
        if (size == 0) {
            return ""
        }

        // Set the initial command to be lowercase and test if it is not blank
        val initCommand = commandArray[0].toLowerCase()

        if (initCommand == "") {
            return ""
        }

        var j = -1
        for (k in commandArray.indices) {
            if (commandArray[k].startsWith("#")) {
                j = k
                break
            }
        }

        if (j == 0) {
            return ""
        }

        if (j >= 0) {
            val localAny2 = arrayOfNulls<String>(j)
            for (m in 0 until j) {
                localAny2[m] = commandArray[m]
            }
            commandArray = localAny2.requireNoNulls()
            size = commandArray.size
        }

        val command = this.commands[initCommand] ?: return "Unknown command: $initCommand"
        return command.doCommand(commandArray, size)
    }

    companion object {
        const val PROMPT = "\n>>> "
    }

    internal interface Command {
        val usage: String

        val help: String

        @Throws(CustomException::class)
        fun doCommand(paramArrayOfString: Array<String>, paramInt: Int): String
    }
}


