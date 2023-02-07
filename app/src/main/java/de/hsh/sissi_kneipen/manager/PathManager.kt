package de.hsh.sissi_kneipen.manager

import android.content.Context
import android.util.Log
import de.hsh.sissi_kneipen.model.Arrival
import de.hsh.sissi_kneipen.util.IOComponent
import de.hsh.sissi_kneipen.util.PATH_FILENAME
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import java.io.File

class PathManager {
    private val ioComponent = IOComponent()
    private val myJson = Json(JsonConfiguration.Stable)


    fun getPath(context: Context): ArrayList<Arrival> {
        val file = File(context.filesDir, PATH_FILENAME)

        return if (file.exists()) {
            Log.i(PATH_FILENAME, "read")
            read(context)
        } else {
            ArrayList<Arrival>()
        }
    }

    private fun read(context: Context): ArrayList<Arrival> {
        return ArrayList(jsonToPath(ioComponent.read(context, PATH_FILENAME)))
    }

    fun savePath(context: Context, path: ArrayList<Arrival>) {
        ioComponent.write(context, PATH_FILENAME, pathToJson(path))
    }

    private fun pathToJson(path: ArrayList<Arrival>): String {
        return myJson.stringify(Arrival.serializer().list, path)
    }

    private fun jsonToPath(json: String): List<Arrival> {
        return myJson.parse(ArrayListSerializer(Arrival.serializer()), json)
    }
}