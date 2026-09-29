package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = isDarkMode) {
                MainContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainContent(viewModel: MainViewModel) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val selectedGrade by viewModel.selectedGrade.collectAsStateWithLifecycle()
    val selectedSubject by viewModel.selectedSubject.collectAsStateWithLifecycle()
    val progressList by viewModel.progressList.collectAsStateWithLifecycle()
    val badges by viewModel.badges.collectAsStateWithLifecycle()
    val reports by viewModel.reports.collectAsStateWithLifecycle()
    val classroomTasks by viewModel.classroomTasks.collectAsStateWithLifecycle()
    val discussions by viewModel.discussions.collectAsStateWithLifecycle()
    val quizState by viewModel.quizState.collectAsStateWithLifecycle()
    val isSyncingClassroom by viewModel.isSyncingClassroom.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()
    val leaderboardFilter by viewModel.leaderboardFilter.collectAsStateWithLifecycle()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    // Request Notification permission for Android 13+
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    LaunchedEffect(userMessage) {
        userMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearUserMessage()
        }
    }

    // Handle Back Press when on sub-screens
    BackHandler(enabled = currentScreen != AppScreen.ADVENTURE && currentScreen != AppScreen.QUIZ) {
        viewModel.navigateTo(AppScreen.ADVENTURE)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (currentScreen != AppScreen.QUIZ) {
                NavigationBar(
                    modifier = Modifier.testTag("bottom_nav")
                ) {
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.ADVENTURE,
                        onClick = { viewModel.navigateTo(AppScreen.ADVENTURE) },
                        icon = { Icon(Icons.Default.SportsEsports, contentDescription = "Petualang") },
                        label = { Text("Petualang") },
                        modifier = Modifier.testTag("nav_adventure")
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.AVATAR,
                        onClick = { viewModel.navigateTo(AppScreen.AVATAR) },
                        icon = { Icon(Icons.Default.Face, contentDescription = "Avatar") },
                        label = { Text("Avatar") },
                        modifier = Modifier.testTag("nav_avatar")
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.LEADERBOARD,
                        onClick = { viewModel.navigateTo(AppScreen.LEADERBOARD) },
                        icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Juara") },
                        label = { Text("Juara") },
                        modifier = Modifier.testTag("nav_leaderboard")
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.STUDY_GROUP,
                        onClick = { viewModel.navigateTo(AppScreen.STUDY_GROUP) },
                        icon = { Icon(Icons.Default.Groups, contentDescription = "Kelompok") },
                        label = { Text("Kelompok") },
                        modifier = Modifier.testTag("nav_study_group")
                    )
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.TEACHER_DASHBOARD,
                        onClick = { viewModel.navigateTo(AppScreen.TEACHER_DASHBOARD) },
                        icon = { Icon(Icons.Default.Assessment, contentDescription = "Guru") },
                        label = { Text("Guru") },
                        modifier = Modifier.testTag("nav_teacher_dashboard")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (currentScreen == AppScreen.QUIZ) 0.dp else innerPadding.calculateBottomPadding())
        ) {
            when (currentScreen) {
                AppScreen.ADVENTURE -> {
                    AdventureMapScreen(
                        userProfile = userProfile,
                        selectedGrade = selectedGrade,
                        selectedSubject = selectedSubject,
                        progressList = progressList,
                        onSelectGrade = { viewModel.setGrade(it) },
                        onSelectSubject = { viewModel.selectSubject(it) },
                        onStartLevel = { level -> viewModel.startQuiz(level, context) },
                        onOpenAvatarShop = { viewModel.navigateTo(AppScreen.AVATAR) },
                        onOpenTeacherDashboard = { viewModel.navigateTo(AppScreen.TEACHER_DASHBOARD) },
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { viewModel.toggleDarkMode() },
                        onSendDailyReminder = { viewModel.sendDailyStudyReminderNow(context) }
                    )
                }
                AppScreen.QUIZ -> {
                    QuizGameScreen(
                        quizState = quizState,
                        onSelectOption = { idx, ctx -> viewModel.selectOption(idx, ctx) },
                        onSubmitAnswer = { ctx -> viewModel.submitAnswer(ctx) },
                        onNextQuestion = { ctx -> viewModel.nextQuestion(ctx) },
                        onExitQuiz = { viewModel.exitQuiz() }
                    )
                }
                AppScreen.AVATAR -> {
                    AvatarCustomizerScreen(
                        userProfile = userProfile,
                        onEquipAvatar = { char, hat, outfit, acc, ctx ->
                            viewModel.equipAvatar(char, hat, outfit, acc, ctx)
                        },
                        onBuyItem = { itemId, cost, ctx ->
                            viewModel.buyAvatarItem(itemId, cost, ctx)
                        }
                    )
                }
                AppScreen.LEADERBOARD -> {
                    LeaderboardScreen(
                        leaderboardEntries = viewModel.getLeaderboard(),
                        badges = badges,
                        selectedGrade = selectedGrade,
                        filter = leaderboardFilter,
                        onFilterChange = { viewModel.setLeaderboardFilter(it) }
                    )
                }
                AppScreen.STUDY_GROUP -> {
                    StudyGroupScreen(
                        userProfile = userProfile,
                        selectedGrade = selectedGrade,
                        discussions = discussions,
                        onPostDiscussion = { title, msg, subj, ctx ->
                            viewModel.postDiscussion(title, msg, subj, ctx)
                        },
                        onLikeDiscussion = { id, ctx ->
                            viewModel.likeDiscussion(id, ctx)
                        }
                    )
                }
                AppScreen.TEACHER_DASHBOARD -> {
                    TeacherParentDashboardScreen(
                        userProfile = userProfile,
                        selectedGrade = selectedGrade,
                        reports = reports,
                        classroomTasks = classroomTasks,
                        isSyncingClassroom = isSyncingClassroom,
                        onSyncClassroom = { ctx -> viewModel.syncWithGoogleClassroom(ctx) },
                        onSubmitClassroomTask = { task, ctx -> viewModel.submitClassroomTask(task, ctx) },
                        onSendDailyReminder = { ctx -> viewModel.sendDailyStudyReminderNow(ctx) },
                        onSendClassReminder = { ctx -> viewModel.sendClassScheduleReminderNow(ctx) }
                    )
                }
            }
        }
    }
}
