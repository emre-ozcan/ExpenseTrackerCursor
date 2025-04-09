package com.emreozcan.expensetracker.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emreozcan.expensetracker.BuildConfig
import com.emreozcan.expensetracker.ui.theme.ExpenseTrackerTheme
import kotlinx.coroutines.delay

@Composable
fun SettingsScreen() {
    val scrollState = rememberScrollState()
    val appVersion = BuildConfig.VERSION_NAME // build.gradle'den versionName'i dinamik alıyoruz
    val userName = "Emre Özcan" // Burada kullanıcı adını sabit girdim, gerçek uygulamada veri tabanından alınabilir
    
    // Animasyon için state'ler
    val headerVisible = remember { MutableTransitionState(false) }
    val userCardVisible = remember { MutableTransitionState(false) }
    val helpSectionVisible = remember { MutableTransitionState(false) }
    val versionVisible = remember { MutableTransitionState(false) }
    
    // Animasyonları başlat
    LaunchedEffect(key1 = true) {
        headerVisible.targetState = true
        delay(150)
        userCardVisible.targetState = true
        delay(150)
        helpSectionVisible.targetState = true
        delay(150)
        versionVisible.targetState = true
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Header - Fade in animasyonu
        AnimatedVisibility(
            visibleState = headerVisible,
            enter = fadeIn(animationSpec = tween(500)) + 
                   slideInVertically(
                       animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                       initialOffsetY = { -40 }
                   )
        ) {
            Text(
                text = "Ayarlar",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B1D36)
                ),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        
        // User Card - Scale In + Fade In animasyonu
        AnimatedVisibility(
            visibleState = userCardVisible,
            enter = fadeIn(animationSpec = tween(500)) + 
                   scaleIn(
                       animationSpec = spring(
                           dampingRatio = Spring.DampingRatioMediumBouncy,
                           stiffness = Spring.StiffnessMedium
                       ),
                       initialScale = 0.8f
                   )
        ) {
            UserInfoCard(userName = userName)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Help Section - Slide In animasyonu
        AnimatedVisibility(
            visibleState = helpSectionVisible,
            enter = fadeIn(animationSpec = tween(500)) + 
                   slideInVertically(
                       animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                       initialOffsetY = { 100 }
                   )
        ) {
            HelpSection()
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // App Version - Slide In + Fade In animasyonu
        AnimatedVisibility(
            visibleState = versionVisible,
            enter = fadeIn(animationSpec = tween(500)) + 
                   slideInVertically(
                       animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                       initialOffsetY = { 100 }
                   )
        ) {
            SettingsItem(
                icon = Icons.Default.Info,
                title = "Uygulama Versiyonu",
                value = appVersion,
                showArrow = false
            )
        }
    }
}

@Composable
fun UserInfoCard(userName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar Circle
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1B1D36)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Avatar",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B1D36)
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Kişisel Profil",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray
                    )
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Profile Details",
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun HelpSection() {
    var expanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300)
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header (Clickable Section)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1B1D36).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Help",
                            tint = Color(0xFF1B1D36),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Text(
                        text = "Nasıl Kullanılır?",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Kapat" else "Aç",
                    modifier = Modifier.rotate(rotationState),
                    tint = Color.Gray
                )
            }
            
            // Expandable Content
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Divider(color = Color.LightGray.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Expense Tracker Uygulaması Kullanım Kılavuzu:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1B1D36)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "1. Ana Ekran: Günlük harcamalarınızı takip edin ve yeni harcama ekleyin.",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = Color.DarkGray
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "2. İstatistikler: Harcamalarınızın kategorilere göre dağılımını ve haftalık harcama grafiğini görüntüleyin.",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = Color.DarkGray
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "3. Raporlar: Aylık ve yıllık harcama raporlarınızı inceleyin.",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = Color.DarkGray
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "4. Ayarlar: Uygulama ayarlarını yönetin ve kişisel bilgilerinizi güncelleyin.",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    showArrow: Boolean = true
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1B1D36).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color(0xFF1B1D36),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                if (value.isNotEmpty()) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray
                        )
                    )
                }
            }
            
            if (showArrow) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Navigate",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    ExpenseTrackerTheme {
        SettingsScreen()
    }
} 