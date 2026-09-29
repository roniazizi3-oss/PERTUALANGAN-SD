package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubjectProgressEntity
import com.example.data.model.UserProfileEntity
import com.example.ui.components.CharacterAvatarView
import com.example.ui.components.StarRatingRow
import com.example.ui.components.TopGameStatsBar
import com.example.ui.theme.CoinGold
import com.example.ui.theme.ExpPurple
import com.example.ui.theme.FireStreak
import com.example.ui.theme.StarYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdventureMapScreen(
    userProfile: UserProfileEntity?,
    selectedGrade: Int,
    selectedSubject: String,
    progressList: List<SubjectProgressEntity>,
    onSelectGrade: (Int) -> Unit,
    onSelectSubject: (String) -> Unit,
    onStartLevel: (Int) -> Unit,
    onOpenAvatarShop: () -> Unit,
    onOpenTeacherDashboard: () -> Unit,
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    onSendDailyReminder: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val filteredProgress = progressList.filter {
        it.gradeLevel == selectedGrade && it.subject == selectedSubject
    }.sortedBy { it.levelNumber }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CharacterAvatarView(
                            character = userProfile?.avatarCharacter ?: "kancil",
                            hat = userProfile?.avatarHat ?: "wisuda",
                            outfit = userProfile?.avatarOutfit ?: "seragam_sd",
                            size = 40.dp,
                            modifier = Modifier.clickable { onOpenAvatarShop() }
                        )
                        Column {
                            Text(
                                text = userProfile?.name ?: "Petualang SD",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Kelas $selectedGrade SD • ${userProfile?.schoolName ?: "Nusantara"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = onToggleDarkMode,
                        modifier = Modifier.testTag("toggle_dark_mode_btn")
                    ) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (isDarkMode) "Mode Terang" else "Mode Gelap",
                            tint = if (isDarkMode) Color(0xFFFBBF24) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onSendDailyReminder,
                        modifier = Modifier.testTag("daily_reminder_quick_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Pengingat Harian",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = onOpenTeacherDashboard,
                        modifier = Modifier.testTag("teacher_dashboard_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Assessment,
                            contentDescription = "Dashboard Guru",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats bar (Coins, Exp, Streak)
            item {
                TopGameStatsBar(
                    coins = userProfile?.coins ?: 0,
                    exp = userProfile?.exp ?: 0,
                    streak = userProfile?.currentStreak ?: 1
                )
            }

            // Grade Level Selector Pill Row (Kelas 1 to Kelas 6)
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Pilih Jenjang Kelas SD:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items((1..6).toList()) { grade ->
                            val isSelected = grade == selectedGrade
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSelectGrade(grade) },
                                label = {
                                    Text(
                                        text = "Kelas $grade",
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                leadingIcon = {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.School,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                },
                                modifier = Modifier.testTag("grade_chip_$grade")
                            )
                        }
                    }
                }
            }

            // Subject Selector Tabs (Matematika, Bahasa Indonesia, Sains)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SubjectSelectCard(
                        title = "Matematika",
                        icon = Icons.Default.Calculate,
                        color = Color(0xFF3B82F6),
                        isSelected = selectedSubject == "MATH",
                        modifier = Modifier.weight(1f),
                        onClick = { onSelectSubject("MATH") }
                    )
                    SubjectSelectCard(
                        title = "Bahasa",
                        icon = Icons.Default.MenuBook,
                        color = Color(0xFF10B981),
                        isSelected = selectedSubject == "BAHASA",
                        modifier = Modifier.weight(1f),
                        onClick = { onSelectSubject("BAHASA") }
                    )
                    SubjectSelectCard(
                        title = "Sains",
                        icon = Icons.Default.Science,
                        color = Color(0xFFF59E0B),
                        isSelected = selectedSubject == "SCIENCE",
                        modifier = Modifier.weight(1f),
                        onClick = { onSelectSubject("SCIENCE") }
                    )
                }
            }

            // Offline Mode & Daily Quest Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudDone,
                                contentDescription = "Offline Ready",
                                tint = Color.White
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "100% Mode Offline Aktif",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF10B981).copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "Tersimpan",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF065F46),
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Bisa dimainkan kapan saja tanpa kuota internet! Semua materi dan soal tersedia di perangkatmu.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }

            // Adventure World Title
            item {
                val subjName = when (selectedSubject) {
                    "MATH" -> "Pulau Angka Ajaib"
                    "BAHASA" -> "Lembah Kata Nusantara"
                    else -> "Galaksi Pengetahuan Sains"
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Peta Petualangan $subjName",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Selesaikan setiap pulau untuk membuka gerbang level berikutnya!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Adventure Map Nodes List (Level 1 to 5)
            items(filteredProgress) { levelItem ->
                AdventureNodeCard(
                    levelItem = levelItem,
                    onStart = { onStartLevel(levelItem.levelNumber) }
                )
            }
        }
    }
}

@Composable
fun SubjectSelectCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(76.dp)
            .clickable { onClick() }
            .testTag("subject_card_${title.lowercase()}"),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) color else MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = if (isSelected) 6.dp else 1.dp,
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) Color.White else color,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun AdventureNodeCard(
    levelItem: SubjectProgressEntity,
    onStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isUnlocked = levelItem.isUnlocked
    val isCompleted = levelItem.isCompleted

    val cardColor = when {
        isCompleted -> MaterialTheme.colorScheme.surface
        isUnlocked -> MaterialTheme.colorScheme.surface
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(enabled = isUnlocked) { onStart() }
            .testTag("adventure_level_${levelItem.levelNumber}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(if (isUnlocked) 3.dp else 0.dp),
        border = if (isUnlocked && !isCompleted) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Level badge with circle and status
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCompleted -> Brush.linearGradient(listOf(Color(0xFF10B981), Color(0xFF059669)))
                            isUnlocked -> Brush.linearGradient(listOf(Color(0xFF3B82F6), Color(0xFF2563EB)))
                            else -> Brush.linearGradient(listOf(Color.Gray.copy(alpha = 0.4f), Color.DarkGray.copy(alpha = 0.4f)))
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (!isUnlocked) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Terkunci",
                        tint = Color.White
                    )
                } else if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selesai",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                } else {
                    Text(
                        text = "${levelItem.levelNumber}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }

            // Info column
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Level ${levelItem.levelNumber}",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isUnlocked) MaterialTheme.colorScheme.primary else Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                    if (isCompleted) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFD1FAE5)
                        ) {
                            Text(
                                text = "Skor: ${levelItem.highScore}%",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF065F46),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Text(
                    text = levelItem.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else Color.Gray
                )

                Spacer(modifier = Modifier.height(6.dp))

                if (isCompleted) {
                    StarRatingRow(stars = levelItem.starsEarned, starSize = 18.dp)
                } else if (isUnlocked) {
                    Text(
                        text = "Tantangan Terbuka • Siap Dimainkan",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        text = "Selesaikan Level ${levelItem.levelNumber - 1} untuk membuka",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            // Action arrow or play button
            if (isUnlocked) {
                FilledTonalButton(
                    onClick = onStart,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("play_level_${levelItem.levelNumber}")
                ) {
                    Text(text = if (isCompleted) "Ulangi" else "Main")
                }
            }
        }
    }
}
