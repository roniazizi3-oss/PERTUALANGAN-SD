package com.example.ui.screens

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CharacterAvatarView
import com.example.ui.components.StarRatingRow
import com.example.ui.theme.CoinGold
import com.example.ui.theme.ExpPurple
import com.example.ui.theme.FireStreak
import com.example.ui.theme.StarYellow
import com.example.ui.viewmodel.QuizSessionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizGameScreen(
    quizState: QuizSessionState?,
    onSelectOption: (Int, Context) -> Unit,
    onSubmitAnswer: (Context) -> Unit,
    onNextQuestion: (Context) -> Unit,
    onExitQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    BackHandler { onExitQuiz() }

    if (quizState == null || quizState.questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (quizState.isFinished) {
        QuizVictoryScreen(
            quizState = quizState,
            onExit = onExitQuiz
        )
        return
    }

    val currentQ = quizState.questions.getOrNull(quizState.currentIndex)
    if (currentQ == null) return

    val totalQ = quizState.questions.size
    val progress = (quizState.currentIndex + 1).toFloat() / totalQ
    var showHintDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val subjName = when (quizState.subject) {
                        "MATH" -> "Matematika"
                        "BAHASA" -> "Bahasa Indonesia"
                        else -> "Sains & Alam"
                    }
                    Column {
                        Text(
                            text = "$subjName • Level ${quizState.levelNumber}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Kelas ${quizState.gradeLevel} SD • Soal ${quizState.currentIndex + 1} dari $totalQ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onExitQuiz,
                        modifier = Modifier.testTag("quiz_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Keluar Kuis"
                        )
                    }
                },
                actions = {
                    if (quizState.comboStreak > 1) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = FireStreak.copy(alpha = 0.15f),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "Combo",
                                    tint = FireStreak,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Combo x${quizState.comboStreak}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FireStreak
                                )
                            }
                        }
                    }
                    IconButton(
                        onClick = { showHintDialog = true },
                        modifier = Modifier.testTag("hint_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Petunjuk",
                            tint = Color(0xFFF59E0B)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (!quizState.isAnswerSubmitted) {
                        Button(
                            onClick = { onSubmitAnswer(context) },
                            enabled = quizState.selectedOptionIndex != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("submit_answer_btn"),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Periksa Jawaban",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Button(
                            onClick = { onNextQuestion(context) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("next_question_btn"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (quizState.isCorrect) Color(0xFF10B981) else MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = if (quizState.currentIndex + 1 < totalQ) "Lanjut ke Soal Berikutnya ➡️" else "Lihat Hasil Petualangan 🏆",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Animated Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            // Question Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("question_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = "Tantangan #${quizState.currentIndex + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = currentQ.questionText,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 4 Multiple Choice Options
            val options = listOf(
                Pair("A", currentQ.optionA),
                Pair("B", currentQ.optionB),
                Pair("C", currentQ.optionC),
                Pair("D", currentQ.optionD)
            )

            options.forEachIndexed { index, (letter, text) ->
                val isSelected = quizState.selectedOptionIndex == index
                val isCorrectAnswer = currentQ.correctIndex == index
                val isSubmitted = quizState.isAnswerSubmitted

                val cardColor = when {
                    isSubmitted && isCorrectAnswer -> Color(0xFFD1FAE5) // Soft green
                    isSubmitted && isSelected && !isCorrectAnswer -> Color(0xFFFEE2E2) // Soft red
                    isSelected -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surface
                }

                val borderColor = when {
                    isSubmitted && isCorrectAnswer -> Color(0xFF10B981)
                    isSubmitted && isSelected && !isCorrectAnswer -> Color(0xFFEF4444)
                    isSelected -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outlineVariant
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isSubmitted) {
                            onSelectOption(index, context)
                        }
                        .testTag("option_$letter"),
                    shape = RoundedCornerShape(16.dp),
                    color = cardColor,
                    border = androidx.compose.foundation.BorderStroke(2.dp, borderColor),
                    tonalElevation = if (isSelected) 4.dp else 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isSubmitted && isCorrectAnswer -> Color(0xFF10B981)
                                        isSubmitted && isSelected && !isCorrectAnswer -> Color(0xFFEF4444)
                                        isSelected -> MaterialTheme.colorScheme.primary
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSubmitted && isCorrectAnswer) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Benar",
                                    tint = Color.White
                                )
                            } else if (isSubmitted && isSelected && !isCorrectAnswer) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Salah",
                                    tint = Color.White
                                )
                            } else {
                                Text(
                                    text = letter,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Explanation & Feedback Box after submission
            if (quizState.isAnswerSubmitted) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("explanation_card"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (quizState.isCorrect) Color(0xFFECFDF5) else Color(0xFFFFFBEB)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = if (quizState.isCorrect) "🎉" else "💡",
                            fontSize = 28.sp
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (quizState.isCorrect) "Luar Biasa, Jawabanmu Tepat Sekali!" else "Yuk Pahami Pembahasannya:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (quizState.isCorrect) Color(0xFF065F46) else Color(0xFF92400E)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentQ.explanation,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (quizState.isCorrect) Color(0xFF047857) else Color(0xFF78350F)
                            )
                        }
                    }
                }
            }
        }
    }

    // Hint Dialog
    if (showHintDialog) {
        AlertDialog(
            onDismissRequest = { showHintDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = "Petunjuk Petualang",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = currentQ.hint,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = { showHintDialog = false },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Mengerti! 👍")
                }
            }
        )
    }
}

@Composable
fun QuizVictoryScreen(
    quizState: QuizSessionState,
    onExit: () -> Unit
) {
    val total = quizState.questions.size
    val score = if (total > 0) (quizState.correctCount * 100) / total else 0

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Trophy or Mascot
            Text(
                text = if (score >= 75) "🏆" else "🌟",
                fontSize = 80.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (score >= 75) "Petualangan Berhasil!" else "Kerja Bagus, Terus Berlatih!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Kamu menjawab benar ${quizState.correctCount} dari $total soal!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Star Rating
            StarRatingRow(
                stars = quizState.starsEarned,
                starSize = 36.dp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Rewards Breakdown Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Hadiah Petualangan Diperoleh:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        RewardItemView(
                            icon = Icons.Default.MonetizationOn,
                            color = CoinGold,
                            amount = "+${quizState.earnedCoins}",
                            label = "Koin Emas"
                        )
                        RewardItemView(
                            icon = Icons.Default.Bolt,
                            color = ExpPurple,
                            amount = "+${quizState.earnedExp}",
                            label = "Poin EXP"
                        )
                        RewardItemView(
                            icon = Icons.Default.CheckCircle,
                            color = Color(0xFF10B981),
                            amount = "$score%",
                            label = "Akurasi"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onExit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("finish_quiz_btn"),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Lanjut Berpetualang 🚀",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RewardItemView(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    amount: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = amount,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
