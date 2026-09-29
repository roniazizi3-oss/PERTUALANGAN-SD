package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "Budi Pratama",
    val gradeLevel: Int = 3, // Kelas 1 - 6
    val schoolName: String = "SD Negeri Nusantara 01",
    val coins: Int = 350,
    val exp: Int = 1250,
    val currentStreak: Int = 5,
    val lastActiveDate: String = "2026-09-29",
    val avatarCharacter: String = "kancil", // kancil, owl, lion, cat, astronaut
    val avatarHat: String = "wisuda", // none, wisuda, mahkota, astronot, bando
    val avatarOutfit: String = "seragam_sd", // seragam_sd, jubah_sihir, jas_peneliti, superhero, safari
    val avatarAccessory: String = "kacamata_bintang", // none, kacamata_bintang, medali_emas, ransel_roket
    val unlockedItems: String = "kancil,wisuda,seragam_sd,kacamata_bintang" // CSV
)

@Entity(tableName = "subject_progress")
data class SubjectProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gradeLevel: Int, // 1 to 6
    val subject: String, // MATH, BAHASA, SCIENCE
    val levelNumber: Int, // 1 to 10
    val title: String,
    val starsEarned: Int = 0, // 0 to 3
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val highScore: Int = 0,
    val totalQuestions: Int = 5
)

@Entity(tableName = "quiz_questions")
data class QuizQuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gradeLevel: Int, // 1 to 6
    val subject: String, // MATH, BAHASA, SCIENCE
    val levelNumber: Int,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctIndex: Int, // 0..3
    val explanation: String,
    val hint: String
)

@Entity(tableName = "achievement_badges")
data class AchievementBadgeEntity(
    @PrimaryKey val badgeKey: String,
    val title: String,
    val description: String,
    val category: String, // "BELAJAR", "STREAK", "AVATAR", "SKOR"
    val iconName: String,
    val currentProgress: Int,
    val targetProgress: Int,
    val isUnlocked: Boolean,
    val unlockedDate: String = ""
)

@Entity(tableName = "student_reports")
data class StudentReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentName: String,
    val gradeLevel: Int,
    val dateString: String,
    val subject: String,
    val scorePercentage: Int,
    val correctCount: Int,
    val totalCount: Int,
    val durationSeconds: Int,
    val strongTopics: String,
    val weakTopics: String
)

@Entity(tableName = "classroom_tasks")
data class ClassroomTaskEntity(
    @PrimaryKey val id: String,
    val courseName: String,
    val teacherName: String,
    val title: String,
    val description: String,
    val subject: String,
    val gradeLevel: Int,
    val dueDate: String,
    val isSubmitted: Boolean = false,
    val score: Int? = null,
    val isSyncedWithClassroom: Boolean = true
)

@Entity(tableName = "group_discussions")
data class GroupDiscussionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val authorName: String,
    val authorAvatar: String,
    val gradeLevel: Int,
    val subjectTag: String,
    val questionTitle: String,
    val messageText: String,
    val replyCount: Int = 0,
    val likesCount: Int = 0,
    val timeAgo: String = "Baru saja",
    val isSolved: Boolean = false
)

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val avatarCharacter: String,
    val school: String,
    val gradeLevel: Int,
    val exp: Int,
    val stars: Int,
    val badgesCount: Int,
    val isCurrentUser: Boolean = false
)
