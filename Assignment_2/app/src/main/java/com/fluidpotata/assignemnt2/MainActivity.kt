package com.fluidpotata.assignemnt2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.fluidpotata.assignemnt2.ui.theme.Assignemnt2Theme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignemnt2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .systemBarsPadding()
                    .padding(16.dp)
            ) {
                DrawerContent { route ->
                    scope.launch {
                        navController.navigate(route) {
                            popUpTo(ScreenRoutes.Home)
                            launchSingleTop = true
                        }
                        drawerState.close()
                    }
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            topBar = {
                TopAppBar(
                    title = { Text("My App") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            contentWindowInsets = WindowInsets.systemBars,
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = ScreenRoutes.Home,
                modifier = Modifier
                    .padding(innerPadding)
                    .systemBarsPadding()
            ) {
                composable(ScreenRoutes.Home) { HomeScreen() }
                composable(ScreenRoutes.Settings) { ImageViewerScreen() }
                composable(ScreenRoutes.About) { AboutScreen() }
                composable(ScreenRoutes.Video) { VideoScreen() }
            }
        }
    }
}



@Composable
fun DrawerContent(onItemClicked: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Navigation", style = MaterialTheme.typography.titleLarge)

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        DrawerItem("Broadcast Reciever", onClick = { onItemClicked(ScreenRoutes.Home) })
        DrawerItem("Image", onClick = { onItemClicked(ScreenRoutes.Settings) })
        DrawerItem("Video", onClick = { onItemClicked(ScreenRoutes.Video) })
        DrawerItem("Audio", onClick = { onItemClicked(ScreenRoutes.About) })
    }
}

@Composable
fun DrawerItem(label: String, onClick: () -> Unit) {
    TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Text(text = label)
    }
}


//@Composable
//fun HomeScreen() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text("Home Screen", style = MaterialTheme.typography.headlineMedium)
//    }
//}

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Settings Screen", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun AboutScreen() {
    val context = LocalContext.current
    val audioUri = "android.resource://${context.packageName}/raw/mysong"

    AudioPlayerUI(audioUrl = audioUri)
}

@Composable
fun VideoScreen() {
    val context = LocalContext.current
    val videoUri = "android.resource://${context.packageName}/raw/samplevideo"

    VideoPlayerUI(videoUrl = videoUri)
}



//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    Assignemnt2Theme {
//        Greeting("Android")
//    }
//}