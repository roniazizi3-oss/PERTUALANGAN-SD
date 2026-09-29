package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET coins = coins + :earnedCoins, exp = exp + :earnedExp WHERE id = 1")
    suspend fun addReward(earnedCoins: Int, earnedExp: Int)

    @Query("UPDATE user_profile SET gradeLevel = :newGrade WHERE id = 1")
    suspend fun updateGradeLevel(newGrade: Int)

    @Query("UPDATE user_profile SET avatarCharacter = :character, avatarHat = :hat, avatarOutfit = :outfit, avatarAccessory = :accessory WHERE id = 1")
    suspend fun updateAvatar(character: String, hat: String, outfit: String, accessory: String)

    @Query("UPDATE user_profile SET unlockedItems = :items, coins = :remainingCoins WHERE id = 1")
    suspend fun updateUnlockedItems(items: String, remainingCoins: Int)

    @Query("UPDATE user_profile SET currentStreak = currentStreak + 1, lastActiveDate = :today WHERE id = 1")
    suspend fun incrementStreak(today: String)
}

@Dao
interface SubjectProgressDao {
    @Query("SELECT * FROM subject_progress WHERE gradeLevel = :grade AND subject = :subject ORDER BY levelNumber ASC")
    fun getProgressByGradeAndSubject(grade: Int, subject: String): Flow<List<SubjectProgressEntity>>

    @Query("SELECT * FROM subject_progress WHERE gradeLevel = :grade ORDER BY subject, levelNumber ASC")
    fun getAllProgressByGrade(grade: Int): Flow<List<SubjectProgressEntity>>

    @Query("SELECT * FROM subject_progress")
    fun getAllProgress(): Flow<List<SubjectProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<SubjectProgressEntity>)

    @Query("UPDATE subject_progress SET starsEarned = :stars, isCompleted = 1, highScore = :score WHERE gradeLevel = :grade AND subject = :subject AND levelNumber = :level")
    suspend fun completeLevel(grade: Int, subject: String, level: Int, stars: Int, score: Int)

    @Query("UPDATE subject_progress SET isUnlocked = 1 WHERE gradeLevel = :grade AND subject = :subject AND levelNumber = :nextLevel")
    suspend fun unlockNextLevel(grade: Int, subject: String, nextLevel: Int)
}

@Dao
interface QuizQuestionDao {
    @Query("SELECT * FROM quiz_questions WHERE gradeLevel = :grade AND subject = :subject AND levelNumber = :level")
    suspend fun getQuestionsForLevel(grade: Int, subject: String, level: Int): List<QuizQuestionEntity>

    @Query("SELECT COUNT(*) FROM quiz_questions")
    suspend fun getQuestionCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuizQuestionEntity>)
}

@Dao
interface AchievementBadgeDao {
    @Query("SELECT * FROM achievement_badges")
    fun getAllBadges(): Flow<List<AchievementBadgeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(badges: List<AchievementBadgeEntity>)

    @Query("UPDATE achievement_badges SET isUnlocked = 1, unlockedDate = :date, currentProgress = targetProgress WHERE badgeKey = :key")
    suspend fun unlockBadge(key: String, date: String)

    @Query("UPDATE achievement_badges SET currentProgress = :progress WHERE badgeKey = :key")
    suspend fun updateBadgeProgress(key: String, progress: Int)
}

@Dao
interface StudentReportDao {
    @Query("SELECT * FROM student_reports ORDER BY id DESC")
    fun getAllReports(): Flow<List<StudentReportEntity>>

    @Query("SELECT * FROM student_reports WHERE gradeLevel = :grade ORDER BY id DESC")
    fun getReportsForGrade(grade: Int): Flow<List<StudentReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: StudentReportEntity)
}

@Dao
interface ClassroomTaskDao {
    @Query("SELECT * FROM classroom_tasks WHERE gradeLevel = :grade ORDER BY dueDate ASC")
    fun getTasksForGrade(grade: Int): Flow<List<ClassroomTaskEntity>>

    @Query("SELECT * FROM classroom_tasks ORDER BY dueDate ASC")
    fun getAllTasks(): Flow<List<ClassroomTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<ClassroomTaskEntity>)

    @Query("UPDATE classroom_tasks SET isSubmitted = 1, score = :score WHERE id = :taskId")
    suspend fun submitTask(taskId: String, score: Int)
}

@Dao
interface GroupDiscussionDao {
    @Query("SELECT * FROM group_discussions WHERE gradeLevel = :grade ORDER BY id DESC")
    fun getDiscussionsForGrade(grade: Int): Flow<List<GroupDiscussionEntity>>

    @Query("SELECT * FROM group_discussions ORDER BY id DESC")
    fun getAllDiscussions(): Flow<List<GroupDiscussionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscussion(discussion: GroupDiscussionEntity)

    @Query("UPDATE group_discussions SET likesCount = likesCount + 1 WHERE id = :id")
    suspend fun likeDiscussion(id: Int)
}
