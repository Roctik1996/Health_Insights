package roctik.app.health.ui.action

sealed interface MainActivityAction: Action

data object RequestPermissionAction: MainActivityAction
data object NavigateToPermissionScreen : MainActivityAction
data object NavigateToLoadScreen : MainActivityAction
data object NavigateToMainScreen : MainActivityAction
data object NavigateToSleepDetailScreen : MainActivityAction
