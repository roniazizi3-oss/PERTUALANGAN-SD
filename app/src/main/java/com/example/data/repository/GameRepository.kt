package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.InitialDataGenerator
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class GameRepository(private val db: AppDatabase) {

    val userProfile: Flow<UserProfileEntity?> = db.userProfileDao().getUserProfile()

    fun getBadges(): Flow<List<AchievementBadgeEntity>> = db.achievementBadgeDao().getAllBadges()

    fun getProgressByGradeAndSubject(grade: Int, subject: String): Flow<List<SubjectProgressEntity>> =
        db.subjectProgressDao().getProgressByGradeAndSubject(grade, subject)

    fun getAllProgressByGrade(grade: Int): Flow<List<SubjectProgressEntity>> =
        db.subjectProgressDao().getAllProgressByGrade(grade)

    fun getReports(grade: Int): Flow<List<StudentReportEntity>> =
        db.studentReportDao().getReportsForGrade(grade)

    fun getTasks(grade: Int): Flow<List<ClassroomTaskEntity>> =
        db.classroomTaskDao().getTasksForGrade(grade)

    fun getDiscussions(grade: Int): Flow<List<GroupDiscussionEntity>> =
        db.groupDiscussionDao().getDiscussionsForGrade(grade)

    suspend fun getQuestionsForLevel(grade: Int, subject: String, level: Int): List<QuizQuestionEntity> =
        withContext(Dispatchers.IO) {
            val list = db.quizQuestionDao().getQuestionsForLevel(grade, subject, level)
            if (list.isNotEmpty()) list
            else {
                // Fallback to level 1 questions if level > 1 doesn't have dedicated specific items yet
                db.quizQuestionDao().getQuestionsForLevel(grade, subject, 1)
            }
        }

    suspend fun ensureInitialized() = withContext(Dispatchers.IO) {
        val currentProfile = db.userProfileDao().getUserProfile().firstOrNull()
        if (currentProfile == null) {
            db.userProfileDao().insertOrUpdateProfile(InitialDataGenerator.getDefaultProfile())
        }

        val questionCount = db.quizQuestionDao().getQuestionCount()
        if (questionCount == 0) {
            db.quizQuestionDao().insertQuestions(InitialDataGenerator.getCurriculumQuestions())
            db.subjectProgressDao().insertAll(InitialDataGenerator.getInitialProgress())
            db.achievementBadgeDao().insertBadges(InitialDataGenerator.getInitialBadges())
            db.classroomTaskDao().insertTasks(InitialDataGenerator.getInitialTasks())
            db.groupDiscussionDao().insertDiscussion(InitialDataGenerator.getInitialDiscussions()[0])
            db.groupDiscussionDao().insertDiscussion(InitialDataGenerator.getInitialDiscussions()[1])
            db.groupDiscussionDao().insertDiscussion(InitialDataGenerator.getInitialDiscussions()[2])
            for (report in InitialDataGenerator.getInitialReports()) {
                db.studentReportDao().insertReport(report)
            }
        }
    }

    suspend fun completeLevel(
        grade: Int,
        subject: String,
        level: Int,
        stars: Int,
        score: Int,
        earnedCoins: Int,
        earnedExp: Int
    ) = withContext(Dispatchers.IO) {
        db.subjectProgressDao().completeLevel(grade, subject, level, stars, score)
        // Unlock next level up to level 5
        if (level < 5) {
            db.subjectProgressDao().unlockNextLevel(grade, subject, level + 1)
        }
        db.userProfileDao().addReward(earnedCoins, earnedExp)

        // Update badges progress
        db.achievementBadgeDao().updateBadgeProgress("math_genius", 8)
        db.achievementBadgeDao().updateBadgeProgress("science_explorer", 5)
        if (stars >= 3) {
            db.achievementBadgeDao().unlockBadge("science_explorer", "Hari ini")
        }
    }

    suspend fun switchGrade(grade: Int) = withContext(Dispatchers.IO) {
        db.userProfileDao().updateGradeLevel(grade)
    }

    suspend fun updateAvatar(character: String, hat: String, outfit: String, accessory: String) =
        withContext(Dispatchers.IO) {
            db.userProfileDao().updateAvatar(character, hat, outfit, accessory)
        }

    suspend fun buyAvatarItem(itemId: String, cost: Int, currentUnlocked: String, currentCoins: Int): Boolean =
        withContext(Dispatchers.IO) {
            if (currentCoins >= cost) {
                val newUnlocked = if (currentUnlocked.isEmpty()) itemId else "$currentUnlocked,$itemId"
                db.userProfileDao().updateUnlockedItems(newUnlocked, currentCoins - cost)
                true
            } else {
                false
            }
        }

    suspend fun submitClassroomTask(taskId: String, score: Int) = withContext(Dispatchers.IO) {
        db.classroomTaskDao().submitTask(taskId, score)
        db.userProfileDao().addReward(50, 150)
    }

    suspend fun syncGoogleClassroom(): List<ClassroomTaskEntity> = withContext(Dispatchers.IO) {
        // Return latest tasks after simulated cloud sync
        val newTasks = listOf(
            ClassroomTaskEntity(
                id = "gc_sync_new_${System.currentTimeMillis() % 1000}",
                courseName = "Sains & Teknologi SD - Bu Ratna",
                teacherName = "Ibu Ratna Kumalasari, S.Pd",
                title = "Tugas Lapangan: Pengamatan Daur Air & Cuaca",
                description = "Amati bentuk awan dan proses penguapan di sekitar rumah. Catat pengamatan di jurnal sains.",
                subject = "SCIENCE",
                gradeLevel = 3,
                dueDate = "5 Oktober 2026, 15:00 WIB",
                isSubmitted = false,
                score = null,
                isSyncedWithClassroom = true
            )
        )
        db.classroomTaskDao().insertTasks(newTasks)
        newTasks
    }

    suspend fun postGroupDiscussion(
        authorName: String,
        authorAvatar: String,
        grade: Int,
        subjectTag: String,
        title: String,
        message: String
    ) = withContext(Dispatchers.IO) {
        val newDiscussion = GroupDiscussionEntity(
            authorName = authorName,
            authorAvatar = authorAvatar,
            gradeLevel = grade,
            subjectTag = subjectTag,
            questionTitle = title,
            messageText = message,
            replyCount = 0,
            likesCount = 1,
            timeAgo = "Baru saja",
            isSolved = false
        )
        db.groupDiscussionDao().insertDiscussion(newDiscussion)
        db.userProfileDao().addReward(15, 30) // Reward for active peer participation
    }

    suspend fun likeDiscussion(id: Int) = withContext(Dispatchers.IO) {
        db.groupDiscussionDao().likeDiscussion(id)
    }

    suspend fun addStudyReport(report: StudentReportEntity) = withContext(Dispatchers.IO) {
        db.studentReportDao().insertReport(report)
    }
}
