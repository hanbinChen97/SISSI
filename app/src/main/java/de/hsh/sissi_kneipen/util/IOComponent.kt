package de.hsh.sissi_kneipen.util

import android.content.Context
import java.io.File

class IOComponent {
    fun read(context: Context, filename: String): String {
        return File(context.filesDir, filename).readText(Charsets.UTF_8)
    }

    fun write(context: Context, filename: String, content: String): String {
        File(context.filesDir, filename).writeText(content)
        return content
    }
}