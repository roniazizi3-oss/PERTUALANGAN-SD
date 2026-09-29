package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CoinGold
import com.example.ui.theme.ExpPurple
import com.example.ui.theme.FireStreak
import com.example.ui.theme.StarYellow

@Composable
fun CharacterAvatarView(
    character: String,
    hat: String = "none",
    outfit: String = "seragam_sd",
    accessory: String = "none",
    size: Dp = 80.dp,
    modifier: Modifier = Modifier
) {
    val charBgColor = when (character) {
        "kancil" -> Color(0xFFFDBA74) // warm amber
        "owl" -> Color(0xFF93C5FD)    // soft blue
        "lion" -> Color(0xFFFCD34D)   // bright golden
        "cat" -> Color(0xFFF472B6)    // cheerful pink
        else -> Color(0xFFA78BFA)     // cosmic lavender
    }

    val charEmoji = when (character) {
        "kancil" -> "🦌"
        "owl" -> "🦉"
        "lion" -> "🦁"
        "cat" -> "🐱"
        else -> "🧑‍🚀"
    }

    val hatEmoji = when (hat) {
        "wisuda" -> "🎓"
        "mahkota" -> "👑"
        "astronot" -> "🪖"
        "bando" -> "🎀"
        "koboi" -> "🤠"
        else -> ""
    }

    val outfitEmoji = when (outfit) {
        "seragam_sd" -> "🎒"
        "jas_peneliti" -> "🥼"
        "jubah_sihir" -> "🧙"
        "superhero" -> "🦸"
        "safari" -> "🏕️"
        else -> "👕"
    }

    val accessoryEmoji = when (accessory) {
        "kacamata_bintang" -> "⭐"
        "medali_emas" -> "🥇"
        "ransel_roket" -> "🚀"
        else -> ""
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size / 3))
            .background(
                Brush.linearGradient(
                    colors = listOf(charBgColor, charBgColor.copy(alpha = 0.7f))
                )
            )
            .border(2.5.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(size / 3)),
        contentAlignment = Alignment.Center
    ) {
        // Main mascot face
        Text(
            text = charEmoji,
            fontSize = (size.value * 0.45).sp
        )

        // Hat badge (top right)
        if (hatEmoji.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-2).dp, y = (-2).dp)
                    .size(size * 0.38f)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = hatEmoji, fontSize = (size.value * 0.22).sp)
            }
        }

        // Outfit badge (bottom left)
        if (outfitEmoji.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = 2.dp, y = 2.dp)
                    .size(size * 0.35f)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = outfitEmoji, fontSize = (size.value * 0.2).sp)
            }
        }

        // Accessory sparkle (bottom right)
        if (accessoryEmoji.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-2).dp, y = 2.dp)
                    .size(size * 0.32f)
                    .clip(CircleShape)
                    .background(Color(0xFFFEF3C7)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = accessoryEmoji, fontSize = (size.value * 0.18).sp)
            }
        }
    }
}

@Composable
fun StatChip(
    icon: ImageVector,
    iconColor: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(38.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun TopGameStatsBar(
    coins: Int,
    exp: Int,
    streak: Int,
    onStreakClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatChip(
            icon = Icons.Default.MonetizationOn,
            iconColor = CoinGold,
            value = "$coins",
            label = "Koin"
        )
        StatChip(
            icon = Icons.Default.Bolt,
            iconColor = ExpPurple,
            value = "$exp XP",
            label = "EXP"
        )
        Surface(
            modifier = Modifier
                .height(38.dp)
                .clickable { onStreakClick() }
                .testTag("streak_chip"),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Streak",
                    tint = FireStreak,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "$streak Hari",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun StarRatingRow(stars: Int, maxStars: Int = 3, starSize: Dp = 20.dp) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        for (i in 1..maxStars) {
            val isEarned = i <= stars
            Icon(
                imageVector = if (isEarned) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = if (isEarned) "Bintang dapat" else "Bintang kosong",
                tint = if (isEarned) StarYellow else Color.Gray.copy(alpha = 0.5f),
                modifier = Modifier.size(starSize)
            )
        }
    }
}
