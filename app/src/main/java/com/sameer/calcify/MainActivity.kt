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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sameer.calcify.domain.engine.AnswerChecker
import com.sameer.calcify.domain.engine.ArithmeticQuestionGenerator
import com.sameer.calcify.domain.model.ArithmeticQuestion
import com.sameer.calcify.domain.scoring.AnswerOutcome
import com.sameer.calcify.domain.scoring.ScoreCalculator
import com.sameer.calcify.presentation.theme.CalcifyTheme

private val Background = Color(0xFF151719)
private val SurfaceDark = Color(0xFF222629)
private val Accent = Color(0xFF57D6C3)
private val Muted = Color(0xFFA9B1B5)
private const val QUESTIONS_PER_SESSION = 10

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalcifyTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Background) {
                    CalcifyRoot()
                }
            }
        }
    }
}

private enum class AppDestination(val label: String) {
    Home("Home"),
    Progress("Progress"),
    History("History"),
    Training("Training")
}

@Composable
private fun CalcifyRoot() {
    var destination by remember { mutableStateOf(AppDestination.Home) }
    var selectedMode by remember { mutableStateOf("Fast Arithmetic") }

    Scaffold(
        containerColor = Background,
        bottomBar = {
            if (destination != AppDestination.Training) {
                NavigationBar(containerColor = SurfaceDark) {
                    listOf(AppDestination.Home, AppDestination.Progress, AppDestination.History).forEach { item ->
                        NavigationBarItem(
                            selected = destination == item,
                            onClick = { destination = item },
                            icon = {},
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        when (destination) {
            AppDestination.Home -> HomeScreen(
                modifier = Modifier.padding(innerPadding),
                selectedMode = selectedMode,
                onStartTraining = { destination = AppDestination.Training },
                onModeSelected = { selectedMode = it }
            )
            AppDestination.Progress -> PlaceholderScreen(
                modifier = Modifier.padding(innerPadding),
                title = "Progress",
                description = "Progress summaries will appear after session history is saved.",
                actionLabel = "Back to Home",
                onAction = { destination = AppDestination.Home }
            )
            AppDestination.History -> PlaceholderScreen(
                modifier = Modifier.padding(innerPadding),
                title = "History",
                description = "Completed sessions will appear here after local storage is added.",
                actionLabel = "Back to Home",
                onAction = { destination = AppDestination.Home }
            )
            AppDestination.Training -> TrainingScreen(
                modifier = Modifier.padding(innerPadding),
                mode = selectedMode,
                onExit = { destination = AppDestination.Home }
            )
        }
    }
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    selectedMode: String,
    onStartTraining: () -> Unit,
    onModeSelected: (String) -> Unit
) {
    val selectedModeAvailable = selectedMode == "Fast Arithmetic" || selectedMode == "Division Mastery"

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
                    Text("CALCIFY", color = Accent, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Text("Train your calculation.", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
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
                    Text("Your progress starts with one session.", color = Muted, fontSize = 13.sp)
                }
            }
        }
        item {
            Button(
                onClick = onStartTraining,
                enabled = selectedModeAvailable,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Accent, contentColor = Color(0xFF10211F))
            ) {
                Text(
                    if (selectedModeAvailable) "Start $selectedMode" else "$selectedMode coming soon",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
        item {
            Text("Choose a training mode", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
        }
        item {
            ModeCard("Fast Arithmetic", "Addition, subtraction, multiplication, and division.", "MIXED", selectedMode == "Fast Arithmetic") {
                selectedMode = "Fast Arithmetic"
                onModeSelected(selectedMode)
            }
        }
        item {
            ModeCard("Division Mastery", "Practise quotients rounded to two decimal places.", "DECIMALS", selectedMode == "Division Mastery") {
                selectedMode = "Division Mastery"
                onModeSelected(selectedMode)
            }
        }
        item {
            ModeCard("Mixed Calculations", "Combined operations and BODMAS are planned next.", "COMING NEXT", selectedMode == "Mixed Calculations") {
                selectedMode = "Mixed Calculations"
                onModeSelected(selectedMode)
            }
        }
        item {
            ModeCard("Calculation Sprint", "Timed challenges are planned for a later milestone.", "COMING NEXT", selectedMode == "Calculation Sprint") {
                selectedMode = "Calculation Sprint"
                onModeSelected(selectedMode)
            }
        }
        item {
            Text(
                "The first playable version includes 10-question sessions, answer feedback, scoring, and skipping. Session history is not saved yet.",
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
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = if (selected) Color(0xFF293A39) else SurfaceDark),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier.size(44.dp).background(Accent.copy(alpha = 0.12f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(if (selected) "✓" else "＋", color = Accent, fontSize = 23.sp, fontWeight = FontWeight.Medium)
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
private fun TrainingScreen(
    modifier: Modifier = Modifier,
    mode: String,
    onExit: () -> Unit
) {
    val generator = remember(mode) { ArithmeticQuestionGenerator() }
    var question by remember(mode) { mutableStateOf(makeQuestion(generator, mode)) }
    var answerInput by remember(mode) { mutableStateOf("") }
    var outcomes by remember(mode) { mutableStateOf(emptyList<AnswerOutcome>()) }
    var feedback by remember(mode) { mutableStateOf<String?>(null) }
    var finished by remember(mode) { mutableStateOf(false) }

    val score = ScoreCalculator.sessionScore(outcomes)
    val accuracy = ScoreCalculator.accuracyPercent(outcomes)
    val answeredCount = outcomes.size

    fun recordOutcome(outcome: AnswerOutcome, message: String) {
        if (feedback != null || finished) return
        outcomes = outcomes + outcome
        feedback = message
    }

    fun advance() {
        if (finished) return
        if (outcomes.size >= QUESTIONS_PER_SESSION) {
            finished = true
        } else {
            question = makeQuestion(generator, mode)
            answerInput = ""
            feedback = null
        }
    }

    Column(
        modifier = modifier.fillMaxSize().background(Background).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(mode.uppercase(), color = Accent, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                Text("Question ${(answeredCount + if (feedback == null) 1 else 0).coerceAtMost(QUESTIONS_PER_SESSION)} of $QUESTIONS_PER_SESSION", color = Muted, fontSize = 13.sp)
            }
            Button(onClick = onExit) { Text("Exit") }
        }

        if (finished) {
            Spacer(Modifier.height(20.dp))
            Text("Session complete", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Score: $score points", color = Accent, fontSize = 23.sp, fontWeight = FontWeight.Bold)
            Text("Accuracy: ${accuracy.toInt()}%", color = Color.White, fontSize = 18.sp)
            Text("${outcomes.count { it == AnswerOutcome.CORRECT }} correct · ${outcomes.count { it == AnswerOutcome.INCORRECT }} incorrect · ${outcomes.count { it == AnswerOutcome.SKIPPED }} skipped", color = Muted)
            Text("This score is currently held in memory and is not saved after leaving the app.", color = Muted, fontSize = 13.sp)
            Button(onClick = onExit, modifier = Modifier.fillMaxWidth()) { Text("Back to Home") }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("CURRENT SCORE", color = Muted, fontSize = 12.sp, letterSpacing = 1.sp)
                    Text("$score", color = Accent, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(20.dp))
                    Text(question.expression, color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                }
            }

            OutlinedTextField(
                value = answerInput,
                onValueChange = { if (feedback == null) answerInput = it },
                modifier = Modifier.fillMaxWidth(),
                enabled = feedback == null,
                label = { Text("Your answer") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            if (feedback != null) {
                Text(feedback.orEmpty(), color = if (feedback!!.startsWith("Correct")) Accent else Muted, fontSize = 15.sp)
                Button(onClick = { advance() }, modifier = Modifier.fillMaxWidth()) {
                    Text(if (answeredCount >= QUESTIONS_PER_SESSION) "See Results" else "Next Question")
                }
            } else {
                Button(
                    onClick = {
                        if (AnswerChecker.isCorrect(answerInput, question)) {
                            recordOutcome(AnswerOutcome.CORRECT, "Correct! +10 points")
                        } else {
                            recordOutcome(
                                AnswerOutcome.INCORRECT,
                                "Not quite. The answer is ${question.formattedAnswer}. −2 points"
                            )
                        }
                    },
                    enabled = answerInput.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Check Answer") }
                Button(
                    onClick = { recordOutcome(AnswerOutcome.SKIPPED, "Question skipped. 0 points") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark, contentColor = Color.White)
                ) { Text("Skip Question") }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Answered: ${outcomes.count { it != AnswerOutcome.SKIPPED }}", color = Muted, fontSize = 12.sp)
                Text("Accuracy: ${accuracy.toInt()}%", color = Muted, fontSize = 12.sp)
            }
        }
    }
}

private fun makeQuestion(generator: ArithmeticQuestionGenerator, mode: String): ArithmeticQuestion =
    if (mode == "Division Mastery") generator.generateDivisionQuestion() else generator.generate()

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
