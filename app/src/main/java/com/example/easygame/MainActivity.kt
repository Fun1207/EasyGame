package com.example.easygame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.easygame.navigation.Navigator
import org.koin.android.ext.android.get
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.compose.navigation3.getEntryProvider
import org.koin.androidx.scope.activityScope
import org.koin.core.annotation.KoinExperimentalAPI

class MainActivity : ComponentActivity(), AndroidScopeComponent {

    override val scope by activityScope()

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navigator: Navigator = get<Navigator>().apply {
                setOnQuitApplication { finishAndRemoveTask() }
            }

            NavDisplay(
                backStack = navigator.backStack,
                onBack = navigator::popBackStack,
                entryProvider = getEntryProvider(),
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                )
            )
        }
    }
}
