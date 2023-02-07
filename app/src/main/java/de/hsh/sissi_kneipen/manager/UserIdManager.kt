package de.hsh.sissi_kneipen.manager

import android.content.Context
import android.os.AsyncTask
import android.os.SystemClock
import android.util.Log
import de.hsh.sissi_kneipen.util.ID_FILENAME
import de.hsh.sissi_kneipen.util.IOComponent
import de.hsh.sissi_kneipen.util.PUB_PATH
import de.hsh.sissi_kneipen.util.SISSI
import org.json.JSONObject
import java.io.File
import java.net.URL

/**
 * UUID will be create by java class UUID and saved in file, data.data.de.hsh.android.file.UUID
 * to see it, please open Device File Explorer in Android Studio
 */
class UserIdManager {
    private val ioComponent = IOComponent()
    lateinit var userId: String
    var doingJob = false


    fun getId(context: Context): String {
        val file = File(context.filesDir, ID_FILENAME)

        return if (file.exists()) {
            Log.i(ID_FILENAME, "read")
            readId(context)
        } else {
            Log.i(ID_FILENAME, "write")
            createId(context)
        }
    }

    private fun readId(context: Context): String {
        return ioComponent.read(context, ID_FILENAME)
    }

    private fun createId(context: Context): String {
        doingJob = true
        GetIdTask(context).execute()

        while (doingJob) {
            SystemClock.sleep(500)
            Log.i(SISSI, "wait user id loading")
        }
        ioComponent.write(context, ID_FILENAME, userId)

        return userId
    }


    inner class GetIdTask(context: Context) :
        AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            val idResult = URL("$PUB_PATH/test/id").readText()
            val id = JSONObject(idResult)
            userId = id.getString("id")
            Log.i(SISSI, userId)
            doingJob = false

            return null
        }

    }

}