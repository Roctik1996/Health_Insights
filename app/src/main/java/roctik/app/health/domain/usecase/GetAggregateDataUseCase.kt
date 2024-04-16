package roctik.app.health.domain.usecase

import androidx.health.connect.client.aggregate.AggregateMetric
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.time.TimeRangeFilter
import roctik.app.health.di.Preferences
import roctik.app.health.domain.repository.HealthDataRepository
import roctik.app.health.utils.Const.APP_PREFS_START_DATE
import java.time.Instant
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId

class GetAggregateDataUseCase(
    private val healthDataRepository: HealthDataRepository,
    private val sharedPreferences: Preferences
) {
    suspend fun getAggregateData(metric: Set<AggregateMetric<*>>): List<AggregationResultGroupedByPeriod> {
        val millis = sharedPreferences.getStartDate()
        val startTime =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())
        val endTime = LocalDateTime.now()
        return healthDataRepository.aggregateGroupByPeriod(
            AggregateGroupByPeriodRequest(
                metrics = metric,
                timeRangeFilter = TimeRangeFilter.between(
                    startTime,
                    endTime
                ),
                timeRangeSlicer = Period.ofDays(1)
            )
        )
    }
}