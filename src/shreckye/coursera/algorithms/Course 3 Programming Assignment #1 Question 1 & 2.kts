package shreckye.coursera.algorithms

import java.io.File

val filename = args[0]

data class Job(val weight: Int, val length: Int)
val (numJobs, jobs) = File(filename).inputStream().bufferedReader().use {
    val numJobs = it.readLine().toInt()
    val jobs = it.lineSequence().map { it.splitToInts().run { Job(get(0), get(1)) } }.toList()
    numJobs to jobs
}

// Schedule jobs and return the sum of weighted completion times
fun scheduleJobs(jobs: List<Job>, comparator: Comparator<Job>): Long {
    val scheduledJobs = jobs.sortedWith(comparator)

    var completionTime = 0
    var sumWeightedCompletionTimes = 0L
    for (scheduledJob in scheduledJobs) {
        completionTime += scheduledJob.length
        sumWeightedCompletionTimes += scheduledJob.weight * completionTime
    }
    return sumWeightedCompletionTimes

    /*// Do the same work in functional programming
    return scheduledJobs.fold(0 to 0L) { (lastCompletionTime, lastSumWeightedCompletionTimes), (length, weight) ->
        val completionTime = lastCompletionTime + length
        completionTime to lastSumWeightedCompletionTimes + weight * completionTime
    }.second*/
}

println(
    "1. " + scheduleJobs(jobs, compareBy<Job> { it.weight - it.length }.thenBy(Job::weight).reversed())
)
println(
    "2. " + scheduleJobs(jobs, Comparator<Job> { o1, o2 -> o1.weight * o2.length - o2.weight * o1.length }.reversed())
)