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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sameer.calcify.data.TrainingSessionRepository
import com.sameer.calcify.data.local.TrainingSessionEntity
import com.sameer.calcify.domain.engine.AnswerChecker
import com.sameer.calcify.domain.engine.ArithmeticQuestionGenerator
import com.sameer.calcify.domain.model.ArithmeticQuestion
import com.sameer.calcify.domain.scoring.AnswerOutcome
import com.sameer.calcify.domain.scoring.ScoreCalculator
import com.sameer.calcify.presentation.theme.CalcifyTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val Background = Color(0xFF151719)
private val SurfaceDark = Color(0xFF222629)
private val Accent = Color(0xFF57D6C3)
private val Muted = Color(0xFFA9B1B5)
private const val QUESTIONS_PER_SESSION = 10
private const val SPRINT_SECONDS = 60

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
    val context = LocalContext.current
    val repository = remember { TrainingSessionRepository(context.applicationContext) }
    var destination by remember { mutableStateOf(AppDestination.Home) }
    var selectedMode by remember { mutableStateOf("Fast Arithmetic") }
    var sessions by remember { mutableStateOf(emptyList<TrainingSessionEntity>()) }

    fun refreshSessions() {
        repository.getSessions { sessions = it }
    }

    LaunchedEffect(repository) {
        refreshSessions()
    }

    Scaffold(
        containerColor = Background,
        bottomBar = {
            if (destination != AppDestination.Training) {
                NavigationBar(containerColor = SurfaceDark) {
                    listOf(AppDestination.Home, AppDestination.Progress, AppDestination.History).forEach { item ->
                        NavigationBarItem(
                            selected = destination == item,
                            onClick = {
                                destination = item
                                if (item == AppDestination.Progress || item == AppDestination.History) refreshSessions()
                            },
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
                sessions = sessions,
                onStartTraining = { destination = AppDestination.Training },
                onModeSelected = { selectedMode = it }
            )
            AppDestination.Progress -> ProgressScreen(
                modifier = Modifier.padding(innerPadding),
                sessions = sessions
            )
            AppDestination.History -> HistoryScreen(
                modifier = Modifier.padding(innerPadding),
                sessions = sessions
            )
            AppDestination.Training -> TrainingScreen(
                modifier = Modifier.padding(innerPadding),
                mode = selectedMode,
                repository = repository,
                onSessionSaved = { refreshSessions() },
                onExit = {
                    destination = AppDestination.Home
                    refreshSessions()
                }
            )
        }
    }
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    selectedMode: String,
    sessions: List<TrainingSessionEntity>,
    onStartTraining: () -> Unit,
    onModeSelected: (String) -> Unit
) {
    val selectedModeAvailable = selectedMode in listOf(
        "Fast Arithmetic", "Division Mastery", "Mixed Calculations", "Calculation Sprint"
    )
    val todayScore = sessions.filter { isSameLocalDay(it.completedAt, System.currentTimeMillis()) }.sumOf { it.score }

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
                        Text("$todayScore", color = Color.White, fontSize = 38.sp, fontWeight = FontWeight.Bold)
                        Text(" pts", color = Accent, modifier = Modifier.padding(bottom = 7.dp))
                    }
                    Text("${sessions.size} completed sessions saved on this device.", color = Muted, fontSize = 13.sp)
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
                Text("Start $selectedMode", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
        item {
            Text("Choose a training mode", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
        }
        item {
            ModeCard("Fast Arithmetic", "Addition, subtraction, multiplication, and exact division.", "10 QUESTIONS", selectedMode == "Fast Arithmetic") {
                onModeSelected("Fast Arithmetic")
            }
        }
        item {
            ModeCard("Division Mastery", "Practise quotients rounded to two decimal places.", "DECIMALS", selectedMode == "Division Mastery") {
                onModeSelected("Division Mastery")
            }
        }
        item {
            ModeCard("Mixed Calculations", "Practise order of operations with combined expressions and parentheses.", "BODMAS", selectedMode == "Mixed Calculations") {
                onModeSelected("Mixed Calculations")
            }
        }
        item {
            ModeCard("Calculation Sprint", "Solve as many arithmetic questions as possible in 60 seconds.", "TIMED", selectedMode == "Calculation Sprint") {
                onModeSelected("Calculation Sprint")
            }
        }
        item {
            Text(
                "Completed sessions are saved locally. Progress and History use your saved session results.",
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
    repository: TrainingSessionRepository,
    onSessionSaved: () -> Unit,
    onExit: () -> Unit
) {
    val generator = remember(mode) { ArithmeticQuestionGenerator() }
    var question by remember(mode) { mutableStateOf(makeQuestion(generator, mode)) }
    var answerInput by remember(mode) { mutableStateOf("") }
    var outcomes by remember(mode) { mutableStateOf(emptyList<AnswerOutcome>()) }
    var feedback by remember(mode) { mutableStateOf<String?>(null) }
    var finished by remember(mode) { mutableStateOf(false) }
    var sessionSaved by remember(mode) { mutableStateOf(false) }
    var remainingSeconds by remember(mode) { mutableStateOf(SPRINT_SECONDS) }
    val startedAt = remember(mode) { System.currentTimeMillis() }

    val score = ScoreCalculator.sessionScore(outcomes)
    val accuracy = ScoreCalculator.accuracyPercent(outcomes)
    val answeredCount = outcomes.size
    val isSprint = mode == "Calculation Sprint"

    fun saveSessionOnce() {
        if (sessionSaved) return
        sessionSaved = true
        val elapsedSeconds = ((System.currentTimeMillis() - startedAt) / 1000L).toInt()
        val session = TrainingSessionEntity(
            mode = mode,
            completedAt = System.currentTimeMillis(),
            score = score,
            accuracyPercent = accuracy.toInt(),
            correctCount = outcomes.count { it == AnswerOutcome.CORRECT },
            incorrectCount = outcomes.count { it == AnswerOutcome.INCORRECT },
            skippedCount = outcomes.count { it == AnswerOutcome.SKIPPED },
            totalQuestions = outcomes.size,
            durationSeconds = if (isSprint) (SPRINT_SECONDS - remainingSeconds).coerceAtLeast(0) else elapsedSeconds
        )
        repository.saveSession(session, onSessionSaved)
    }

    fun finishSession() {
        if (finished) return
        finished = true
        saveSessionOnce()
    }

    LaunchedEffect(isSprint, remainingSeconds, finished) {
        if (isSprint && remainingSeconds > 0 && !finished) {
            delay(1000)
            if (!finished) remainingSeconds = (remainingSeconds - 1).coerceAtLeast(0)
        } else if (isSprint && remainingSeconds == 0 && !finished) {
            finishSession()
        }
    }

    fun recordOutcome(outcome: AnswerOutcome, message: String) {
        if (feedback != null || finished || (isSprint && remainingSeconds == 0)) return
        outcomes = outcomes + outcome
        feedback = message
    }

    fun advance() {
        if (finished) return
        if (!isSprint && outcomes.size >= QUESTIONS_PER_SESSION) {
            finishSession()
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
                Text(
                    if (isSprint) "Time left: $remainingSeconds seconds"
                    else "Question ${(answeredCount + if (feedback == null) 1 else 0).coerceAtMost(QUESTIONS_PER_SESSION)} of $QUESTIONS_PER_SESSION",
                    color = Muted,
                    fontSize = 13.sp
                )
            }
            Button(onClick = onExit) { Text("Exit") }
        }

        if (finished) {
            Spacer(Modifier.height(20.dp))
            Text(if (isSprint) "Sprint complete" else "Session complete", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Score: $score points", color = Accent, fontSize = 23.sp, fontWeight = FontWeight.Bold)
            Text("Accuracy: ${accuracy.toInt()}%", color = Color.White, fontSize = 18.sp)
            Text(
                "${outcomes.count { it == AnswerOutcome.CORRECT }} correct · ${outcomes.count { it == AnswerOutcome.INCORRECT }} incorrect · ${outcomes.count { it == AnswerOutcome.SKIPPED }} skipped",
                color = Muted
            )
            Text("This completed session has been saved on this device.", color = Muted, fontSize = 13.sp)
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
                enabled = feedback == null && (!isSprint || remainingSeconds > 0),
                label = { Text("Your answer") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            if (feedback != null) {
                Text(feedback.orEmpty(), color = if (feedback!!.startsWith("Correct")) Accent else Muted, fontSize = 15.sp)
                Button(
                    onClick = { advance() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isSprint || remainingSeconds > 0
                ) {
                    Text(if (!isSprint && answeredCount >= QUESTIONS_PER_SESSION) "See Results" else "Next Question")
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
                    enabled = answerInput.isNotBlank() && (!isSprint || remainingSeconds > 0),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Check Answer") }
                Button(
                    onClick = { recordOutcome(AnswerOutcome.SKIPPED, "Question skipped. 0 points") },
                    enabled = !isSprint || remainingSeconds > 0,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark, contentColor = Color.White)
                ) { Text("Skip Question") }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Recorded: $answeredCount", color = Muted, fontSize = 12.sp)
                Text("Accuracy: ${accuracy.toInt()}%", color = Muted, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun ProgressScreen(
    modifier: Modifier = Modifier,
    sessions: List<TrainingSessionEntity>
) {
    val todayScore = sessions.filter { isSameLocalDay(it.completedAt, System.currentTimeMillis()) }.sumOf { it.score }
    val allTimeScore = sessions.sumOf { it.score }
    val bestScore = sessions.maxOfOrNull { it.score } ?: 0
    val averageAccuracy = if (sessions.isEmpty()) 0 else sessions.map { it.accuracyPercent }.average().toInt()
    val weakestMode = sessions.groupBy { it.mode }
        .mapValues { (_, items) -> items.map { it.accuracyPercent }.average().toInt() }
        .minByOrNull { it.value }

    LazyColumn(
        modifier = modifier.fillMaxSize().background(Background).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Progress", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Your results, based on completed sessions.", color = Muted, fontSize = 14.sp)
        }
        item {
            StatCard("TODAY'S POINTS", todayScore.toString(), "Sum of completed sessions today")
        }
        item {
            StatCard("ALL-TIME POINTS", allTimeScore.toString(), "Points from every saved session")
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f)) { StatCard("SESSIONS", sessions.size.toString(), "Completed") }
                Box(Modifier.weight(1f)) { StatCard("BEST SESSION", bestScore.toString(), "Points") }
            }
        }
        item {
            StatCard("AVERAGE ACCURACY", "$averageAccuracy%", "Average session accuracy")
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = SurfaceDark), shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("AREA TO IMPROVE", color = Accent, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    if (weakestMode == null) {
                        Text("Complete a session to start identifying your weaker training modes.", color = Muted, fontSize = 14.sp)
                    } else {
                        Text(weakestMode.key, color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
                        Text("Average accuracy: ${weakestMode.value}%", color = Muted, fontSize = 14.sp)
                        Text("Practise this mode and compare its accuracy over your next sessions.", color = Muted, fontSize = 12.sp)
                    }
                }
            }
        }
        if (sessions.isEmpty()) {
            item { Text("Your statistics will update after your first completed session.", color = Muted, fontSize = 13.sp) }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, description: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(label, color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Text(value, color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text(description, color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun HistoryScreen(
    modifier: Modifier = Modifier,
    sessions: List<TrainingSessionEntity>
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().background(Background).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("History", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Your completed training sessions, newest first.", color = Muted, fontSize = 14.sp)
        }
        if (sessions.isEmpty()) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = SurfaceDark), shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("No sessions yet", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        Text("Finish a 10-question session or let a 60-second sprint end to save it here.", color = Muted, fontSize = 13.sp)
                    }
                }
            }
        } else {
            items(sessions, key = { it.id }) { session ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(session.mode, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                            Text("${session.score} pts", color = Accent, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(formatSessionDate(session.completedAt), color = Muted, fontSize = 12.sp)
                        Text(
                            "${session.accuracyPercent}% accuracy · ${session.totalQuestions} questions · ${session.durationSeconds}s",
                            color = Color.White,
                            fontSize = 13.sp
                        )
                        Text(
                            "${session.correctCount} correct · ${session.incorrectCount} incorrect · ${session.skippedCount} skipped",
                            color = Muted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

private fun makeQuestion(generator: ArithmeticQuestionGenerator, mode: String): ArithmeticQuestion =
    when (mode) {
        "Division Mastery" -> generator.generateDivisionQuestion()
        "Mixed Calculations" -> generator.generateMixedQuestion()
        "Calculation Sprint" -> generator.generate(min = 1, max = 50)
        else -> generator.generate()
    }

private fun isSameLocalDay(firstMillis: Long, secondMillis: Long): Boolean {
    val first = Calendar.getInstance().apply { timeInMillis = firstMillis }
    val second = Calendar.getInstance().apply { timeInMillis = secondMillis }
    return first.get(Calendar.YEAR) == second.get(Calendar.YEAR) &&
        first.get(Calendar.DAY_OF_YEAR) == second.get(Calendar.DAY_OF_YEAR)
}

private fun formatSessionDate(timestamp: Long): String =
    SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(timestamp))
