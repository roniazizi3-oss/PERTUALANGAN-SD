package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.*

@Database(
    entities = [
        UserProfileEntity::class,
        SubjectProgressEntity::class,
        QuizQuestionEntity::class,
        AchievementBadgeEntity::class,
        StudentReportEntity::class,
        ClassroomTaskEntity::class,
        GroupDiscussionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun subjectProgressDao(): SubjectProgressDao
    abstract fun quizQuestionDao(): QuizQuestionDao
    abstract fun achievementBadgeDao(): AchievementBadgeDao
    abstract fun studentReportDao(): StudentReportDao
    abstract fun classroomTaskDao(): ClassroomTaskDao
    abstract fun groupDiscussionDao(): GroupDiscussionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "edupetualang_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
