package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.model.AchievementBadgeEntity
import com.example.data.model.LeaderboardEntry
import com.example.ui.components.CharacterAvatarView
import com.example.ui.theme.CoinGold
import com.example.ui.theme.ExpPurple
import com.example.ui.theme.StarYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    leaderboardEntries: List<LeaderboardEntry>,
    badges: List<AchievementBadgeEntity>,
    selectedGrade: Int,
    filter: String,
    onFilterChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var mainTab by remember { mutableStateOf(0) } // 0 = Papan Peringkat, 1 = Lencana Prestasi

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Prestasi & Peringkat SD",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = mainTab,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = mainTab == 0,
                    onClick = { mainTab = 0 },
                    text = { Text("Papan Juara 🏆") },
                    modifier = Modifier.testTag("tab_leaderboard")
                )
                Tab(
                    selected = mainTab == 1,
                    onClick = { mainTab = 1 },
                    text = { Text("Lencana Prestasi 🎖️") },
                    modifier = Modifier.testTag("tab_badges")
                )
            }

            if (mainTab == 0) {
                // Leaderboard Tab Content
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Filter Chips (Harian, Mingguan, Semua)
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            listOf("HARIAN", "MINGGUAN", "SEMUA").forEach { option ->
                                val isSelected = filter == option
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { onFilterChange(option) },
                                    label = {
                                        Text(
                                            text = when (option) {
                                                "HARIAN" -> "Harian"
                                                "MINGGUAN" -> "Mingguan"
                                                else -> "Sepanjang Masa"
                                            }
                                        )
                                    },
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp)
                                        .testTag("filter_$option")
                                )
                            }
                        }
                    }

                    // Podium Top 3 (1st, 2nd, 3rd)
                    item {
                        val top3 = leaderboardEntries.take(3)
                        if (top3.size >= 3) {
                            PodiumView(
                                first = top3[0],
                                second = top3[1],
                                third = top3[2]
                            )
                        }
                    }

                    // Header for other rankers
                    item {
                        Text(
                            text = "Daftar Peringkat Siswa Kelas $selectedGrade SD:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // List of entries
                    items(leaderboardEntries) { entry ->
                        LeaderboardRowCard(entry = entry)
                    }
                }
            } else {
                // Badges Tab Content
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = StarYellow,
                                    modifier = Modifier.size(32.dp)
                                )
                                Column {
                                    val unlockedCount = badges.count { it.isUnlocked }
                                    Text(
                                        text = "$unlockedCount dari ${badges.size} Lencana Terbuka!",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Terus selesaikan tantangan belajar untuk melengkapi semua koleksi lencana.",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }

                    items(badges) { badge ->
                        BadgeItemCard(badge = badge)
                    }
                }
            }
        }
    }
}

@Composable
fun PodiumView(
    first: LeaderboardEntry,
    second: LeaderboardEntry,
    third: LeaderboardEntry
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🏆 Peringkat Teratas Minggu Ini",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                // 2nd Place (Silver)
                PodiumColumn(
                    entry = second,
                    rankNumber = "2",
                    pillarHeight = 70.dp,
                    color = Color(0xFF94A3B8),
                    medalEmoji = "🥈"
                )

                // 1st Place (Gold)
                PodiumColumn(
                    entry = first,
                    rankNumber = "1",
                    pillarHeight = 100.dp,
                    color = Color(0xFFF59E0B),
                    medalEmoji = "👑"
                )

                // 3rd Place (Bronze)
                PodiumColumn(
                    entry = third,
                    rankNumber = "3",
                    pillarHeight = 55.dp,
                    color = Color(0xFFD97706),
                    medalEmoji = "🥉"
                )
            }
        }
    }
}

@Composable
fun PodiumColumn(
    entry: LeaderboardEntry,
    rankNumber: String,
    pillarHeight: androidx.compose.ui.unit.Dp,
    color: Color,
    medalEmoji: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(text = medalEmoji, fontSize = 24.sp)
        CharacterAvatarView(
            character = entry.avatarCharacter,
            size = if (rankNumber == "1") 56.dp else 48.dp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = entry.name.split(" ").first(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Text(
            text = "${entry.exp} XP",
            style = MaterialTheme.typography.labelSmall,
            color = ExpPurple,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))

        // Pillar block
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(pillarHeight)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rankNumber,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
fun LeaderboardRowCard(entry: LeaderboardEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("leaderboard_row_${entry.rank}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (entry.isCurrentUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        border = if (entry.isCurrentUser) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Rank Number
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        when (entry.rank) {
                            1 -> Color(0xFFF59E0B)
                            2 -> Color(0xFF94A3B8)
                            3 -> Color(0xFFD97706)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${entry.rank}",
                    fontWeight = FontWeight.Bold,
                    color = if (entry.rank <= 3) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            CharacterAvatarView(character = entry.avatarCharacter, size = 42.dp)

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = entry.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    if (entry.isCurrentUser) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = "Kamu",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Text(
                    text = entry.school,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${entry.exp} XP",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = ExpPurple
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = StarYellow,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${entry.stars}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun BadgeItemCard(badge: AchievementBadgeEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("badge_${badge.badgeKey}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (badge.isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        if (badge.isUnlocked) Brush.linearGradient(listOf(StarYellow, Color(0xFFF59E0B)))
                        else Brush.linearGradient(listOf(Color.LightGray, Color.Gray))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (badge.iconName) {
                        "LocalFireDepartment" -> Icons.Default.LocalFireDepartment
                        "Calculate" -> Icons.Default.Calculate
                        "Science" -> Icons.Default.Science
                        "MenuBook" -> Icons.Default.MenuBook
                        "Checkroom" -> Icons.Default.Checkroom
                        else -> Icons.Default.Groups
                    },
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = badge.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (badge.isUnlocked) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFD1FAE5)
                        ) {
                            Text(
                                text = "Terbuka",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF065F46),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                val prog = badge.currentProgress.toFloat() / badge.targetProgress
                LinearProgressIndicator(
                    progress = { prog.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = if (badge.isUnlocked) Color(0xFF10B981) else MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${badge.currentProgress}/${badge.targetProgress}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
