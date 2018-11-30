package com.adlerd

import javafx.scene.image.Image
import java.net.URI
import java.net.URL

class Utils {
    companion object {

        fun loadImage(path: String): Image {
            return Image(this::class.java.getResource(path).toExternalForm())
        }

        fun loadImage(path: String, width: Int, height: Int): Image {
            return Image(
                Utils::class.java.getResource(path).toExternalForm(),
                width.toDouble(),
                height.toDouble(),
                false,
                true
            )
        }

        fun loadImage(path: String, width: Double, height: Double): Image {
            return Image(Utils::class.java.getResource(path).toExternalForm(), width, height, false, true)
        }

        fun loadToString(path: String): String {
            return Utils::class.java.getResource(path).toExternalForm()
        }

        fun loadToURL(path: String): URL {
            return Utils::class.java.getResource(path)
        }

        fun loadToURI(path: String): URI {
            return Utils::class.java.getResource(path).toURI()
        }
    }
}