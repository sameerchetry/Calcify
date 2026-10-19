package com.sameer.calcify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sameer.calcify.presentation.theme.CalcifyTheme

private val Background = Color(0xFF151719)
private val SurfaceDark = Color(0xFF222629)
private val Accent = Color(0xFF57D6C3)
private val Muted = Color(0xFFA9B1B5)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalcifyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Background
                ) {
                    CalcifyRoot()
                }
            }
        }
    }
}

private enum class AppDestination(val label: String) {
    Home("Home"),
    Progress("Progress"),
    History("History")
}

@Composable
private fun CalcifyRoot() {
    var destination by remember { mutableStateOf(AppDestination.Home) }
    var selectedMode by remember { mutableStateOf("Fast Arithmetic") }

    Scaffold(
        containerColor = Background,
        bottomBar = {
            NavigationBar(containerColor = SurfaceDark) {
                AppDestination.entries.forEach { item ->
                    NavigationBarItem(
                        selected = destination == item,
                        onClick = { destination = item },
                        icon = {},
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        when (destination) {
            AppDestination.Home -> HomeScreen(
                modifier = Modifier.padding(innerPadding),
                onStartTraining = { destination = AppDestination.Progress },
                onModeSelected = { selectedMode = it }
            )
            AppDestination.Progress -> PlaceholderScreen(
                modifier = Modifier.padding(innerPadding),
                title = "Training",
                description = "Selected mode: $selectedMode",
                actionLabel = "Back to Home",
                onAction = { destination = AppDestination.Home }
            )
            AppDestination.History -> PlaceholderScreen(
                modifier = Modifier.padding(innerPadding),
                title = "History",
                description = "Completed sessions will appear here once session storage is implemented.",
                actionLabel = "Back to Home",
                onAction = { destination = AppDestination.Home }
            )
        }
    }
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    onStartTraining: () -> Unit,
    onModeSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CALCIFY",
                        color = Accent,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Train your calculation.",
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text("⚙", color = Color.White, fontSize = 24.sp)
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("TODAY'S SCORE", color = Muted, fontSize = 12.sp, letterSpacing = 1.sp)
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("0", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Bold)
                        Text(" pts", color = Accent, modifier = Modifier.padding(bottom = 7.dp))
                    }
                    Text(
                        "Your progress starts with one session.",
                        color = Muted,
                        fontSize = 13.sp
                    )
                }
            }
        }
        item {
            Button(
                onClick = onStartTraining,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Accent,
                    contentColor = Color(0xFF10211F)
                )
            ) {
                Text("Start Training", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
        item {
            Text("Choose a training mode", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
        }
        item {
            ModeCard(
                title = "Fast Arithmetic",
                description = "Build speed with the four operations.",
                tag = "START HERE",
                onClick = { onModeSelected("Fast Arithmetic") }
            )
        }
        item {
            ModeCard(
                title = "Division Mastery",
                description = "Practise decimals and rounded quotients.",
                tag = "DECIMALS",
                onClick = { onModeSelected("Division Mastery") }
            )
        }
        item {
            ModeCard(
                title = "Mixed Calculations",
                description = "Combine operations using BODMAS.",
                tag = "LOGIC",
                onClick = { onModeSelected("Mixed Calculations") }
            )
        }
        item {
            ModeCard(
                title = "Calculation Sprint",
                description = "Challenge yourself against the clock.",
                tag = "TIMED",
                onClick = { onModeSelected("Calculation Sprint") }
            )
        }
        item {
            Text(
                "More modes, adaptive practice, and real progress tracking will be added in later milestones.",
                color = Muted,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 18.dp)
            )
        }
    }
}

@Composable
private fun ModeCard(
    title: String,
    description: String,
    tag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier.size(44.dp).background(
                    color = Accent.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(14.dp)
                ),
                contentAlignment = Alignment.Center
            ) {
                Text("＋", color = Accent, fontSize = 23.sp, fontWeight = FontWeight.Medium)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(Modifier.height(4.dp))
                Text(description, color = Muted, fontSize = 12.sp)
            }
            Text(tag, color = Accent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PlaceholderScreen(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    actionLabel: String,
    onAction: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize().background(Background).padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(title, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text(description, color = Muted, fontSize = 15.sp)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onAction) { Text(actionLabel) }
    }
}
