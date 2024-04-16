package roctik.app.health.ui.main

import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import roctik.app.health.R
import roctik.app.health.base.BaseViewModel
import roctik.app.health.domain.model.AggregateDataItem
import roctik.app.health.domain.usecase.GetAggregateDataUseCase
import roctik.app.health.ui.action.MainActivityAction
import roctik.app.health.utils.Util

class MainViewModel(
    private val getAggregateDataUseCase: GetAggregateDataUseCase
) : BaseViewModel() {

    val aggregateDataItems = MutableLiveData<List<AggregateDataItem>>()

    private val _mainActivityAction = Channel<MainActivityAction>(capacity = Channel.UNLIMITED)
    val mainActivityAction = _mainActivityAction.receiveAsFlow()

    fun getAggregateData() = launch {
        val steps = getAggregateStepsRecord().toInt().toString()
        val distanceInKm = String.format("%.2f", getAggregateDistanceRecord() / 1000)
        val sleepTime = Util.formatTimeFromSeconds(getAggregateSleepTimeRecord())
        val kcalories = (getAggregateCaloriesRecord().toInt() / 1000).toString()
        val activity = Util.formatTimeFromSeconds(getAggregateActivityRecord())

        val items = listOf(
            AggregateDataItem(
                R.string.label_my_steps, steps,
                R.string.label_unit_steps_day
            ),
            AggregateDataItem(
                R.string.label_distance, distanceInKm,
                R.string.label_unit_km_day
            ),
            AggregateDataItem(
                R.string.label_time_in_bed, sleepTime,
                R.string.label_unit_daily_sleep_time
            ),
            AggregateDataItem(
                R.string.label_calories_burned, kcalories,
                R.string.label_unit_kcal_day
            ),
            AggregateDataItem(
                R.string.label_total_activity, activity,
                R.string.label_unit_minutes_day
            )
        )

        aggregateDataItems.postValue(items)
    }

    private suspend fun getAggregateStepsRecord(): Double {
        val result = getAggregateDataUseCase.getAggregateData(setOf(StepsRecord.COUNT_TOTAL))

        var totalSteps = 0.0
        var daysWithData = 0
        for (monthlyResult in result) {
            val stepsRecord = monthlyResult.result[StepsRecord.COUNT_TOTAL]
            if (stepsRecord != null) {
                totalSteps += stepsRecord
                daysWithData++
            }
        }

        return if (daysWithData > 0) totalSteps / daysWithData else 0.0
    }

    private suspend fun getAggregateDistanceRecord(): Double {
        val result = getAggregateDataUseCase.getAggregateData(setOf(DistanceRecord.DISTANCE_TOTAL))

        var totalDistance = 0.0
        var daysWithData = 0
        for (monthlyResult in result) {
            val distanceRecord = monthlyResult.result[DistanceRecord.DISTANCE_TOTAL]
            if (distanceRecord != null) {
                totalDistance += distanceRecord.inMeters
                daysWithData++
            }
        }

        return if (daysWithData > 0) totalDistance / daysWithData else 0.0
    }

    private suspend fun getAggregateSleepTimeRecord(): Double {
        val result =
            getAggregateDataUseCase.getAggregateData(setOf(SleepSessionRecord.SLEEP_DURATION_TOTAL))

        var totalSleep = 0.0
        var daysWithData = 0
        for (monthlyResult in result) {
            val sleepRecord = monthlyResult.result[SleepSessionRecord.SLEEP_DURATION_TOTAL]

            if (sleepRecord != null) {
                totalSleep += sleepRecord.seconds
                daysWithData++
            }
        }

        return if (daysWithData > 0) totalSleep / daysWithData else 0.0
    }

    private suspend fun getAggregateCaloriesRecord(): Double {
        val result =
            getAggregateDataUseCase.getAggregateData(setOf(TotalCaloriesBurnedRecord.ENERGY_TOTAL))

        var totalCalories = 0.0
        var daysWithData = 0
        for (monthlyResult in result) {
            val caloriesRecord = monthlyResult.result[TotalCaloriesBurnedRecord.ENERGY_TOTAL]
            if (caloriesRecord != null) {
                totalCalories += caloriesRecord.inCalories
                daysWithData++
            }
        }

        return if (daysWithData > 0) totalCalories / daysWithData else 0.0
    }


    private suspend fun getAggregateActivityRecord(): Double {
        val result =
            getAggregateDataUseCase.getAggregateData(setOf(ExerciseSessionRecord.EXERCISE_DURATION_TOTAL))

        var totalExercise = 0.0
        var daysWithData = 0
        for (monthlyResult in result) {
            val exerciseRecord = monthlyResult.result[ExerciseSessionRecord.EXERCISE_DURATION_TOTAL]
            if (exerciseRecord != null) {
                totalExercise += exerciseRecord.seconds
                daysWithData++
            }
        }

        return if (daysWithData > 0) totalExercise / daysWithData else 0.0
    }

    fun sendAction(action: MainActivityAction) = launch {
        _mainActivityAction.send(action)
    }
}