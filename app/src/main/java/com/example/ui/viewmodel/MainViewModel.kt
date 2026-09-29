package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.*
import com.example.data.repository.GameRepository
import com.example.util.NotificationHelper
import com.example.util.SoundHapticHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppScreen {
    ADVENTURE,
    QUIZ,
    AVATAR,
    LEADERBOARD,
    STUDY_GROUP,
    TEACHER_DASHBOARD
}

data class QuizSessionState(
    val levelNumber: Int = 1,
    val subject: String = "MATH",
    val gradeLevel: Int = 3,
    val questions: List<QuizQuestionEntity> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val isCorrect: Boolean = false,
    val correctCount: Int = 0,
    val comboStreak: Int = 0,
    val isFinished: Boolean = false,
    val starsEarned: Int = 0,
    val earnedCoins: Int = 0,
    val earnedExp: Int = 0
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GameRepository

    val userProfile: StateFlow<UserProfileEntity?>
    val badges: StateFlow<List<AchievementBadgeEntity>>
    val progressList: StateFlow<List<SubjectProgressEntity>>
    val reports: StateFlow<List<StudentReportEntity>>
    val classroomTasks: StateFlow<List<ClassroomTaskEntity>>
    val discussions: StateFlow<List<GroupDiscussionEntity>>

    private val _selectedGrade = MutableStateFlow(3)
    val selectedGrade: StateFlow<Int> = _selectedGrade.asStateFlow()

    private val _selectedSubject = MutableStateFlow("MATH")
    val selectedSubject: StateFlow<String> = _selectedSubject.asStateFlow()

    private val _currentScreen = MutableStateFlow(AppScreen.ADVENTURE)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _quizState = MutableStateFlow<QuizSessionState?>(null)
    val quizState: StateFlow<QuizSessionState?> = _quizState.asStateFlow()

    private val _isSyncingClassroom = MutableStateFlow(false)
    val isSyncingClassroom: StateFlow<Boolean> = _isSyncingClassroom.asStateFlow()

    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    private val _leaderboardFilter = MutableStateFlow("MINGGUAN") // HARIAN, MINGGUAN, SEMUA
    val leaderboardFilter: StateFlow<String> = _leaderboardFilter.asStateFlow()

    init {
        val db = AppDatabase.getInstance(application)
        repository = GameRepository(db)

        NotificationHelper.createNotificationChannels(application)

        userProfile = repository.userProfile
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

        badges = repository.getBadges()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        progressList = _selectedGrade.flatMapLatest { grade ->
            repository.getAllProgressByGrade(grade)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        reports = _selectedGrade.flatMapLatest { grade ->
            repository.getReports(grade)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        classroomTasks = _selectedGrade.flatMapLatest { grade ->
            repository.getTasks(grade)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        discussions = _selectedGrade.flatMapLatest { grade ->
            repository.getDiscussions(grade)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        viewModelScope.launch {
            repository.ensureInitialized()
        }
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun setGrade(grade: Int) {
        _selectedGrade.value = grade
        viewModelScope.launch {
            repository.switchGrade(grade)
        }
    }

    fun selectSubject(subject: String) {
        _selectedSubject.value = subject
    }

    fun setLeaderboardFilter(filter: String) {
        _leaderboardFilter.value = filter
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    // --- Quiz Logic ---
    fun startQuiz(levelNumber: Int, context: Context) {
        SoundHapticHelper.playClickHaptic(context)
        val grade = _selectedGrade.value
        val subject = _selectedSubject.value

        viewModelScope.launch {
            val questions = repository.getQuestionsForLevel(grade, subject, levelNumber)
            if (questions.isNotEmpty()) {
                _quizState.value = QuizSessionState(
                    levelNumber = levelNumber,
                    subject = subject,
                    gradeLevel = grade,
                    questions = questions,
                    currentIndex = 0,
                    selectedOptionIndex = null,
                    isAnswerSubmitted = false,
                    isCorrect = false,
                    correctCount = 0,
                    comboStreak = 0,
                    isFinished = false
                )
                _currentScreen.value = AppScreen.QUIZ
            } else {
                _userMessage.value = "Tantangan sedang disiapkan oleh guru!"
            }
        }
    }

    fun selectOption(index: Int, context: Context) {
        val current = _quizState.value ?: return
        if (current.isAnswerSubmitted) return
        SoundHapticHelper.playClickHaptic(context)
        _quizState.value = current.copy(selectedOptionIndex = index)
    }

    fun submitAnswer(context: Context) {
        val current = _quizState.value ?: return
        val selected = current.selectedOptionIndex ?: return
        if (current.isAnswerSubmitted) return

        val currentQ = current.questions.getOrNull(current.currentIndex) ?: return
        val isCorrect = selected == currentQ.correctIndex

        if (isCorrect) {
            SoundHapticHelper.playSuccessHaptic(context)
        } else {
            SoundHapticHelper.playErrorHaptic(context)
        }

        val newStreak = if (isCorrect) current.comboStreak + 1 else 0
        val newCorrectCount = if (isCorrect) current.correctCount + 1 else current.correctCount

        _quizState.value = current.copy(
            isAnswerSubmitted = true,
            isCorrect = isCorrect,
            comboStreak = newStreak,
            correctCount = newCorrectCount
        )
    }

    fun nextQuestion(context: Context) {
        SoundHapticHelper.playClickHaptic(context)
        val current = _quizState.value ?: return
        val nextIdx = current.currentIndex + 1

        if (nextIdx < current.questions.size) {
            _quizState.value = current.copy(
                currentIndex = nextIdx,
                selectedOptionIndex = null,
                isAnswerSubmitted = false,
                isCorrect = false
            )
        } else {
            // Quiz completed!
            val total = current.questions.size
            val ratio = if (total > 0) current.correctCount.toFloat() / total else 0f
            val stars = when {
                ratio >= 0.85f -> 3
                ratio >= 0.5f -> 2
                ratio >= 0.25f -> 1
                else -> 0
            }
            val coins = current.correctCount * 25 + (stars * 20)
            val exp = current.correctCount * 50 + (stars * 35)

            _quizState.value = current.copy(
                isFinished = true,
                starsEarned = stars,
                earnedCoins = coins,
                earnedExp = exp
            )

            SoundHapticHelper.playSuccessHaptic(context)

            // Save progress to database
            viewModelScope.launch {
                val score = if (total > 0) (current.correctCount * 100) / total else 0
                repository.completeLevel(
                    grade = current.gradeLevel,
                    subject = current.subject,
                    level = current.levelNumber,
                    stars = stars,
                    score = score,
                    earnedCoins = coins,
                    earnedExp = exp
                )

                // Save report for teacher & parent analysis
                val subjName = when (current.subject) {
                    "MATH" -> "Matematika"
                    "BAHASA" -> "Bahasa Indonesia"
                    else -> "Sains"
                }
                val report = StudentReportEntity(
                    studentName = userProfile.value?.name ?: "Siswa SD",
                    gradeLevel = current.gradeLevel,
                    dateString = "Hari ini",
                    subject = subjName,
                    scorePercentage = score,
                    correctCount = current.correctCount,
                    totalCount = total,
                    durationSeconds = 180,
                    strongTopics = "Level ${current.levelNumber} - $subjName",
                    weakTopics = if (score < 100) "Perlu review 1 soal" else "Sempurna"
                )
                repository.addStudyReport(report)
            }
        }
    }

    fun exitQuiz() {
        _quizState.value = null
        _currentScreen.value = AppScreen.ADVENTURE
    }

    // --- Avatar Customization & Shop ---
    fun equipAvatar(character: String, hat: String, outfit: String, accessory: String, context: Context) {
        SoundHapticHelper.playClickHaptic(context)
        viewModelScope.launch {
            repository.updateAvatar(character, hat, outfit, accessory)
            _userMessage.value = "Avatar berhasil diperbarui! Keren sekali! ✨"
        }
    }

    fun buyAvatarItem(itemId: String, cost: Int, context: Context) {
        val profile = userProfile.value ?: return
        viewModelScope.launch {
            val success = repository.buyAvatarItem(itemId, cost, profile.unlockedItems, profile.coins)
            if (success) {
                SoundHapticHelper.playSuccessHaptic(context)
                _userMessage.value = "Berhasil membuka item baru! Selamat! 🎉"
            } else {
                SoundHapticHelper.playErrorHaptic(context)
                _userMessage.value = "Koinmu belum cukup. Ayo selesaikan level petualangan untuk dapat koin!"
            }
        }
    }

    // --- Classroom Sync & Assignment Submission ---
    fun syncWithGoogleClassroom(context: Context) {
        SoundHapticHelper.playClickHaptic(context)
        viewModelScope.launch {
            _isSyncingClassroom.value = true
            delay(1500) // Realistic sync animation
            repository.syncGoogleClassroom()
            _isSyncingClassroom.value = false
            NotificationHelper.sendTaskSyncSuccess(context, "Sains SD: Daur Air & Cuaca")
            _userMessage.value = "Tugas Google Classroom berhasil disinkronkan! 📚"
        }
    }

    fun submitClassroomTask(task: ClassroomTaskEntity, context: Context) {
        SoundHapticHelper.playSuccessHaptic(context)
        viewModelScope.launch {
            repository.submitClassroomTask(task.id, 95)
            _userMessage.value = "Tugas '${task.title}' berhasil dikumpulkan ke Guru! Nilai: 95/100 🌟"
        }
    }

    // --- Group Discussion & Collaboration ---
    fun postDiscussion(title: String, message: String, subject: String, context: Context) {
        if (title.isBlank() || message.isBlank()) {
            _userMessage.value = "Judul dan pesan tidak boleh kosong!"
            return
        }
        val profile = userProfile.value
        val author = profile?.name ?: "Siswa Berani"
        val avatar = profile?.avatarCharacter ?: "kancil"
        val grade = _selectedGrade.value

        SoundHapticHelper.playSuccessHaptic(context)
        viewModelScope.launch {
            repository.postGroupDiscussion(author, avatar, grade, subject, title, message)
            _userMessage.value = "Pertanyaanmu terkirim ke kelompok belajar! +15 Koin 🎉"
        }
    }

    fun likeDiscussion(id: Int, context: Context) {
        SoundHapticHelper.playClickHaptic(context)
        viewModelScope.launch {
            repository.likeDiscussion(id)
        }
    }

    // --- Notifications trigger ---
    fun sendDailyStudyReminderNow(context: Context) {
        SoundHapticHelper.playSuccessHaptic(context)
        val name = userProfile.value?.name ?: "Siswa Hebat"
        NotificationHelper.sendDailyStudyReminder(context, name)
        _userMessage.value = "Notifikasi pengingat belajar harian dikirim ke status bar!"
    }

    fun sendClassScheduleReminderNow(context: Context) {
        SoundHapticHelper.playSuccessHaptic(context)
        NotificationHelper.sendClassScheduleReminder(context, "Matematika Kelas ${_selectedGrade.value}", "15 menit lagi")
        _userMessage.value = "Notifikasi jadwal kelas terkirim!"
    }

    fun getLeaderboard(): List<LeaderboardEntry> {
        val user = userProfile.value
        val userExp = user?.exp ?: 1350
        val userName = user?.name ?: "Budi Pratama"
        val userChar = user?.avatarCharacter ?: "kancil"
        val userSchool = user?.schoolName ?: "SDN 01 Nusantara"
        val grade = _selectedGrade.value

        return listOf(
            LeaderboardEntry(1, "Alya Putri Nabila", "owl", "SD Harapan Bangsa", grade, 3450, 42, 6),
            LeaderboardEntry(2, "Kenzo Al-Fatih", "lion", "SD Cita Hati", grade, 2890, 38, 5),
            LeaderboardEntry(3, userName, userChar, userSchool, grade, userExp, 29, 3, isCurrentUser = true),
            LeaderboardEntry(4, "Fajar Nugraha", "astronaut", "SDN Cempaka 02", grade, 1180, 24, 2),
            LeaderboardEntry(5, "Nadia Salsabila", "cat", "SD Kartini 01", grade, 980, 19, 2),
            LeaderboardEntry(6, "Rizky Ramadhan", "kancil", "SD Tunas Bangsa", grade, 750, 15, 1)
        )
    }
}
