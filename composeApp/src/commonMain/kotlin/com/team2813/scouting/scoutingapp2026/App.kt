package com.team2813.scouting.scoutingapp2026

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import scoutingapp2026.composeapp.generated.resources.Res
import scoutingapp2026.composeapp.generated.resources.done
import scoutingapp2026.composeapp.generated.resources.quantitative

@Serializable object Home

@Serializable object Quantitative

@Serializable data class QRCode(val url: String)

@Composable
fun QRCodeScreen(url: String, navController: NavHostController) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()) {
            Image(
                rememberQrCodePainter(url),
                "Submit QR Code",
                modifier = Modifier.padding(20.dp).size(300.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text(stringResource(Res.string.done))
            }
        }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        verticalArrangement =
            Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()) {
            Button(onClick = { navController.navigate(Quantitative) }) {
                Text(stringResource(Res.string.quantitative))
            }
        }
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(
            modifier =
                Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                    .safeContentPadding()
                    .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            KoinApplication({ modules(commonModule) }) {
                val navController: NavHostController = rememberNavController()
                NavHost(navController, startDestination = Home) {
                    composable<Home> { HomeScreen(navController) }
                    composable<Quantitative> { Quantitative(navController) }
                    composable<QRCode> { navBackStack ->
                        val qrcode = navBackStack.toRoute<QRCode>()
                        QRCodeScreen(qrcode.url, navController)
                    }
                }
            }
        }
    }
}
