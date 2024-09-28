package fr.burdairon.florian

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import fr.burdairon.florian.ui.theme.PolytechAppMobileTheme
import fr.burdairon.florian.views.NavGraphs


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val snackBarScope = rememberCoroutineScope()
            PolytechAppMobileTheme {
                Scaffold (
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        modifier = Modifier.padding(innerPadding),
                        // Dependencies
                        // Used to share variables between screens
                        // Here we share the SnackbarHostState and the coroutine scope to be able to show snackbars from any screen
                        dependenciesContainerBuilder = {
                            dependency(snackbarHostState)
                            dependency(snackBarScope)
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("MyActivity", "Resume")
    }
}



