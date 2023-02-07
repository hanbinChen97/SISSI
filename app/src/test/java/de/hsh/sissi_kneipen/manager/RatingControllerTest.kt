package de.hsh.sissi_kneipen.manager

import de.hsh.sissi_kneipen.model.Arrival
import de.hsh.sissi_kneipen.model.Bar
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

class RatingControllerTest {

    @Test
    fun getRating() {
    }

    @Test
    fun getSojournTimes() {
        val path = mockk<ArrayList<Arrival>>()

        every { path.isEmpty() } returns false
        every { path.size } returns 8

        val bar = mockk<Bar>()
        every { bar.id } returns "1" andThen "2" andThen "4" andThen "5"
        every { path[0].bar } returns bar
        every { path[1].bar } returns bar
        every { path[2].bar } returns bar
        every { path[3].bar } returns bar
        every { path[4].bar } returns bar
        every { path[5].bar } returns bar
        every { path[6].bar } returns bar
        every { path[7].bar } returns bar

        every { path[0].time } returns 0
        every { path[1].time } returns 1
        every { path[2].time } returns 3
        every { path[3].time } returns 6
        every { path[4].time } returns 10
        every { path[5].time } returns 15
        every { path[6].time } returns 21
        every { path[7].time } returns 28

        val sojournTimes: HashMap<String, Long> = HashMap()
        sojournTimes["1"] = 1
        sojournTimes["2"] = 3
        sojournTimes["4"] = 5
        sojournTimes["5"] = 7

        assertEquals(sojournTimes, RatingManager().getSojournTimes(path))
    }

    @Test
    fun getMeanOfAllSojournTimes() {
        val sojournTimes: HashMap<String, Long> = HashMap()
        sojournTimes["1"] = 30000
        sojournTimes["2"] = 60000
        sojournTimes["4"] = 30000

        assertEquals(40000, RatingManager().getMeanOfAllSojournTimes(sojournTimes))
    }

    @Test
    fun getStandardDeviation() {
        val sojournTimes: HashMap<String, Long> = HashMap()
        sojournTimes["1"] = 30000
        sojournTimes["2"] = 60000
        sojournTimes["4"] = 30000

        val mean: Long = 40000
        assertEquals(14142.135, RatingManager().getStandardDeviation(sojournTimes, mean), 0.1)
    }

    @Test
    fun normalize() {
        val sojournTimes: HashMap<String, Long> = HashMap()
        sojournTimes["1"] = 30000
        sojournTimes["2"] = 60000
        sojournTimes["4"] = 30000

        val mean: Long = 40000
        val standardDeviation = 14142.135

        val rating: HashMap<String, Double> = HashMap()
        rating["1"] = -0.707
        rating["2"] = 1.414
        rating["4"] = -0.707

        assertEquals(rating, RatingManager().normalize(sojournTimes, mean, standardDeviation))
    }
}