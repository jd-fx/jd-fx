package com.adlerd.jdfx.util

internal interface Command {
    val usage: String

    val help: String

    @Throws(CustomException::class)
    fun doCommand(paramArrayOfString: Array<String>, paramInt: Int): String
}