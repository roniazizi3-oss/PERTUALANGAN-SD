package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfileEntity
import com.example.ui.components.CharacterAvatarView
import com.example.ui.components.StatChip
import com.example.ui.theme.CoinGold

data class ShopItem(
    val id: String,
    val name: String,
    val category: String, // "CHARACTER", "HAT", "OUTFIT", "ACCESSORY"
    val iconEmoji: String,
    val price: Int,
    val description: String
)

val AVATAR_SHOP_ITEMS = listOf(
    // Characters
    ShopItem("kancil", "Kancil Cerdik", "CHARACTER", "🦌", 0, "Cerdik dan pandai menyelesaikan teka-teki."),
    ShopItem("owl", "Burung Hantu Bijak", "CHARACTER", "🦉", 100, "Membaca banyak buku dan berwawasan luas."),
    ShopItem("lion", "Singa Pemberani", "CHARACTER", "🦁", 200, "Berani mencoba tantangan sains yang sulit."),
    ShopItem("cat", "Kucing Pintar", "CHARACTER", "🐱", 150, "Cepat dan tangkas dalam berhitung."),
    ShopItem("astronaut", "Penjelajah Galaksi", "CHARACTER", "🧑‍🚀", 300, "Menjelajah misteri luar angkasa dan teknologi."),

    // Hats
    ShopItem("wisuda", "Topi Wisuda", "HAT", "🎓", 0, "Lambang kelulusan dan prestasi belajar."),
    ShopItem("mahkota", "Mahkota Emas", "HAT", "👑", 250, "Bagi juara kelas yang gigih."),
    ShopItem("astronot", "Helm Pelindung", "HAT", "🪖", 180, "Melindungi dari radiasi kosmik."),
    ShopItem("bando", "Pita Ceria", "HAT", "🎀", 80, "Pita manis penyemangat belajar."),
    ShopItem("koboi", "Topi Petualang", "HAT", "🤠", 120, "Siap berburu ilmu di alam bebas."),

    // Outfits
    ShopItem("seragam_sd", "Seragam SD", "OUTFIT", "🎒", 0, "Seragam kebanggaan anak sekolah dasar."),
    ShopItem("jas_peneliti", "Jas Peneliti", "OUTFIT", "🥼", 120, "Laboratorium sains siap dieksplorasi."),
    ShopItem("jubah_sihir", "Jubah Penyihir", "OUTFIT", "🧙", 220, "Membuat matematika tampak seperti keajaiban!"),
    ShopItem("superhero", "Baju Superhero", "OUTFIT", "🦸", 280, "Pahlawan pembela ilmu pengetahuan."),
    ShopItem("safari", "Baju Safari", "OUTFIT", "🏕️", 160, "Cocok untuk ekspedisi sains di hutan."),

    // Accessories
    ShopItem("kacamata_bintang", "Kacamata Bintang", "ACCESSORY", "⭐", 0, "Melihat masa depan dengan ceria."),
    ShopItem("medali_emas", "Medali Juara", "ACCESSORY", "🥇", 200, "Tanda kemenangan di papan peringkat."),
    ShopItem("ransel_roket", "Ransel Roket", "ACCESSORY", "🚀", 260, "Melesat cepat menuju cita-cita!")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarCustomizerScreen(
    userProfile: UserProfileEntity?,
    onEquipAvatar: (String, String, String, String, Context) -> Unit,
    onBuyItem: (String, Int, Context) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("CHARACTER") }

    val unlockedList = remember(userProfile?.unlockedItems) {
        userProfile?.unlockedItems?.split(",")?.map { it.trim() } ?: listOf("kancil", "wisuda", "seragam_sd", "kacamata_bintang")
    }

    var previewChar by remember(userProfile?.avatarCharacter) {
        mutableStateOf(userProfile?.avatarCharacter ?: "kancil")
    }
    var previewHat by remember(userProfile?.avatarHat) {
        mutableStateOf(userProfile?.avatarHat ?: "wisuda")
    }
    var previewOutfit by remember(userProfile?.avatarOutfit) {
        mutableStateOf(userProfile?.avatarOutfit ?: "seragam_sd")
    }
    var previewAccessory by remember(userProfile?.avatarAccessory) {
        mutableStateOf(userProfile?.avatarAccessory ?: "kacamata_bintang")
    }

    val currentItems = AVATAR_SHOP_ITEMS.filter { it.category == selectedCategory }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lemari Avatar Petualang",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    StatChip(
                        icon = Icons.Default.MonetizationOn,
                        iconColor = CoinGold,
                        value = "${userProfile?.coins ?: 0}",
                        label = "Koin"
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Live Avatar Display Stage Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.colorScheme.surfaceVariant
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        CharacterAvatarView(
                            character = previewChar,
                            hat = previewHat,
                            outfit = previewOutfit,
                            accessory = previewAccessory,
                            size = 96.dp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = userProfile?.name ?: "Petualang SD",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Kustomisasi karakter sesukamu dengan koin belajar!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Save Equipped button
                    Button(
                        onClick = {
                            onEquipAvatar(previewChar, previewHat, previewOutfit, previewAccessory, context)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("save_avatar_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pasang ke Profil")
                    }
                }
            }

            // Category Tab Selector
            ScrollableTabRow(
                selectedTabIndex = when (selectedCategory) {
                    "CHARACTER" -> 0
                    "HAT" -> 1
                    "OUTFIT" -> 2
                    else -> 3
                },
                edgePadding = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = selectedCategory == "CHARACTER",
                    onClick = { selectedCategory = "CHARACTER" },
                    text = { Text("Karakter") },
                    icon = { Icon(Icons.Default.Face, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.testTag("tab_character")
                )
                Tab(
                    selected = selectedCategory == "HAT",
                    onClick = { selectedCategory = "HAT" },
                    text = { Text("Topi & Mahkota") },
                    icon = { Icon(Icons.Default.EmojiEvents, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.testTag("tab_hat")
                )
                Tab(
                    selected = selectedCategory == "OUTFIT",
                    onClick = { selectedCategory = "OUTFIT" },
                    text = { Text("Pakaian") },
                    icon = { Icon(Icons.Default.Checkroom, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.testTag("tab_outfit")
                )
                Tab(
                    selected = selectedCategory == "ACCESSORY",
                    onClick = { selectedCategory = "ACCESSORY" },
                    text = { Text("Aksesoris") },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier.testTag("tab_accessory")
                )
            }

            // Grid of Items for selected category
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(currentItems.size) { index ->
                    val item = currentItems[index]
                    val isUnlocked = unlockedList.contains(item.id)
                    val isEquipped = when (item.category) {
                        "CHARACTER" -> previewChar == item.id
                        "HAT" -> previewHat == item.id
                        "OUTFIT" -> previewOutfit == item.id
                        else -> previewAccessory == item.id
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isUnlocked) {
                                    when (item.category) {
                                        "CHARACTER" -> previewChar = item.id
                                        "HAT" -> previewHat = item.id
                                        "OUTFIT" -> previewOutfit = item.id
                                        "ACCESSORY" -> previewAccessory = item.id
                                    }
                                }
                            }
                            .testTag("shop_item_${item.id}"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isEquipped) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                        ),
                        border = if (isEquipped) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
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
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = item.iconEmoji, fontSize = 28.sp)
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = item.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (isUnlocked) {
                                if (isEquipped) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.primary
                                    ) {
                                        Text(
                                            text = "Dipakai",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                } else {
                                    FilledTonalButton(
                                        onClick = {
                                            when (item.category) {
                                                "CHARACTER" -> previewChar = item.id
                                                "HAT" -> previewHat = item.id
                                                "OUTFIT" -> previewOutfit = item.id
                                                "ACCESSORY" -> previewAccessory = item.id
                                            }
                                        },
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text("Pilih")
                                    }
                                }
                            } else {
                                Button(
                                    onClick = { onBuyItem(item.id, item.price, context) },
                                    colors = ButtonDefaults.buttonColors(containerColor = CoinGold),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("buy_item_${item.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MonetizationOn,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${item.price}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
