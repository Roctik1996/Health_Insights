package roctik.app.health.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import roctik.app.health.R
import roctik.app.health.databinding.ActivityMainBinding
import roctik.app.health.di.Preferences
import roctik.app.health.ui.action.MainActivityAction
import roctik.app.health.ui.action.NavigateToLoadScreen
import roctik.app.health.ui.action.NavigateToMainScreen
import roctik.app.health.ui.action.NavigateToPermissionScreen
import roctik.app.health.ui.action.NavigateToSleepDetailScreen
import roctik.app.health.ui.action.RequestPermissionAction
import roctik.app.health.ui.main.fragment.ImportDataFragment
import roctik.app.health.ui.main.fragment.MainFragment
import roctik.app.health.ui.main.fragment.PermissionFragment
import roctik.app.health.utils.Const.PERMISSIONS
import roctik.app.health.utils.Util
import java.time.LocalDateTime
import java.time.ZoneId

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModel()
    private val healthClient: HealthConnectClient by inject()
    private val preferences: Preferences by inject()

    private var missingPermissions: Set<String> = setOf()

    private val requestPermissionActivityContract =
        PermissionController.createRequestPermissionResultContract()


    private val requestPermissions =
        registerForActivityResult(requestPermissionActivityContract) { granted ->
            if (granted.containsAll(PERMISSIONS)) {
                viewModel.sendAction(NavigateToLoadScreen)
            } else {
                preferences.setPermissionDenied(true)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        availabilityCheck()
        setupObserver()

    }

    override fun onResume() {
        super.onResume()
        checkPermissionsAndRun()
    }

    private fun setupObserver() {
        with(lifecycleScope) {
            viewModel.mainActivityAction.onEach(::onAction).launchIn(this)
        }
    }

    private fun onAction(action: MainActivityAction) {
        when (action) {

            is RequestPermissionAction -> {
                if (!preferences.isPermissionDenied())
                    requestPermissions.launch(missingPermissions)
                else
                    initPermissionDialog()
            }

            is NavigateToPermissionScreen -> {
                replaceFragment(PermissionFragment.newInstance())
            }

            is NavigateToLoadScreen -> {
                replaceFragment(ImportDataFragment.newInstance())
            }

            is NavigateToMainScreen -> {
                replaceFragment(MainFragment.newInstance())
            }

            is NavigateToSleepDetailScreen -> {
            }
        }
    }

    private fun availabilityCheck() {
        val availabilityStatus = HealthConnectClient.getSdkStatus(this, packageName)
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE) {
            return
        }
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
            val uriString =
                "market://details?id=${this.packageName}&url=healthconnect%3A%2F%2Fonboarding"
            startActivity(
                Intent(Intent.ACTION_VIEW).apply {
                    setPackage("com.android.vending")
                    data = Uri.parse(uriString)
                    putExtra("overlay", true)
                    putExtra("callerId", packageName)
                }
            )
            return
        }
    }

    private fun checkPermissionsAndRun() {
        lifecycleScope.launch {
            val grantedPermissions = healthClient.permissionController.getGrantedPermissions()
            missingPermissions = PERMISSIONS.minus(grantedPermissions)

            if (missingPermissions.isEmpty()) {
                saveStartPeriodDate()
                viewModel.sendAction(NavigateToLoadScreen)
            } else {
                viewModel.sendAction(NavigateToPermissionScreen)
            }
        }
    }

    private fun initPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.label_permissions_required))
            .setMessage(getString(R.string.label_permission_need_desk))
            .setPositiveButton(getString(R.string.label_go_to_settings)) { _, _ ->
                openSetting()
            }
            .setNegativeButton(getString(R.string.label_cancel), null)
            .show()
    }

    private fun openSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun saveStartPeriodDate() {
        val startDate = LocalDateTime.now().minusDays(30)
        val millis = startDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        preferences.setStartDate(millis)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            Util.setFragmentAnimations(this)
            replace(R.id.container, fragment, fragment.javaClass.simpleName)
        }
    }


    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            Util.setFragmentAnimations(this)
            add(R.id.container, fragment, fragment.javaClass.simpleName)
            addToBackStack(fragment.javaClass.simpleName)
        }
    }
}