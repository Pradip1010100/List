package com.pavan.list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.pavan.list.ui.theme.ListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation(){
    val navController = rememberNavController()
    val viewaModel: LocationViewModel = viewModel()
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)
    NavHost(navController,startDestination = "shoppinglistscreen"){
        composable ("shoppinglistscreen") { 
            ShoppingListApp(
                locationUtils = locationUtils,
                viewModel = viewaModel,
                navController = navController,
                context = context,
                address = viewaModel.address.value.firstOrNull()?.formatted_address?:"No Address"
            )
        }
        dialog("locationscreen"){
            backstack->
            viewaModel.location.value.let{
                it1->
                LocationSelectionScreen(
                    location = it1,
                    onLocationSelected = {
                        locationdata ->
                        viewaModel.fetchAddress("${locationdata.latitude},${locationdata.longitude}")
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
