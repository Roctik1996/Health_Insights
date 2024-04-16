package roctik.app.health.data.repository

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import roctik.app.health.domain.repository.HealthDataRepository

class HealthDataRepositoryImpl(private val healthConnectClient: HealthConnectClient) :
    HealthDataRepository {
    override suspend fun aggregateGroupByPeriod(request: AggregateGroupByPeriodRequest): List<AggregationResultGroupedByPeriod> {
        return healthConnectClient.aggregateGroupByPeriod(request)
    }
}