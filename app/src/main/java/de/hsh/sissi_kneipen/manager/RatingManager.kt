package de.hsh.sissi_kneipen.manager

import de.hsh.sissi_kneipen.model.Arrival
import kotlin.math.sqrt

class RatingManager {
    fun getRating(path: ArrayList<Arrival>): HashMap<String, Double> {
        val sojournTimes = getSojournTimes(path)
        if (sojournTimes.size == 0) {
            return HashMap<String, Double>()
            //  return hashMapOf("1" to 2.5)
        }

        val mean = getMeanOfAllSojournTimes(sojournTimes)
        val standardDeviation = getStandardDeviation(sojournTimes, mean)
        val rating = normalize(sojournTimes, mean, standardDeviation)

        return rating
    }

    fun getSojournTimes(path: ArrayList<Arrival>): HashMap<String, Long> {
        val sojournTimes = HashMap<String, Long>()

        for (i in 0..(path.size - 2) step 2) {
            val sojournTime = path[i + 1].time - path[i].time
            //clean Data
            if (sojournTime >= 0) {
                sojournTimes[path[i].bar.id] = sojournTime
            }
        }

        return sojournTimes
    }

    fun getMeanOfAllSojournTimes(sojournTimes: HashMap<String, Long>): Long {
        var allSojournTime: Long = 0
        for (t in sojournTimes) {
            allSojournTime = allSojournTime + t.value
        }
        return allSojournTime / sojournTimes.size
    }

    fun getStandardDeviation(sojournTimes: HashMap<String, Long>, mean: Long): Double {
        var sum: Long = 0
        for (t in sojournTimes) {
            sum = sum + (t.value - mean) * (t.value - mean)
        }
        return sqrt((sum / sojournTimes.size).toDouble())
    }

    fun normalize(
        sojournTimes: HashMap<String, Long>,
        mean: Long,
        standardDeviation: Double
    ): HashMap<String, Double> {
        val rating: HashMap<String, Double> = HashMap()

        for (t in sojournTimes) {
            val zScore =
                ((((t.value - mean) / standardDeviation) * 1000).toInt()).toDouble() / 1000
            rating[t.key] = zScore

        }
        return rating
    }
}