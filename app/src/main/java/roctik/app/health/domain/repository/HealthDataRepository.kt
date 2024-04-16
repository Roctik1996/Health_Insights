package roctik.app.health.domain.repository

import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest

interface HealthDataRepository {

    suspend fun aggregateGroupByPeriod(request: AggregateGroupByPeriodRequest): List<AggregationResultGroupedByPeriod>

}