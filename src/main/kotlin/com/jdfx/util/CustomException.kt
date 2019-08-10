package com.jdfx.util

abstract class CustomException : Exception {
    constructor()

    constructor(paramString: String) : super(paramString)

    open val exceptionDescription: String
        get() = "EXCEPTION: $message"
}