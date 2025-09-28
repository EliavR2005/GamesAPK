package com.example.games.memorama

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games.ui.theme.GamesTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
class MemoramaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamesTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF0A0A0A),
                                    Color(0xFF1A0033),
                                    Color(0xFF0D0033)
                                )
                            )
                        )
                ) {
                    MemoramaScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoramaScreen() {
    val game = remember { MemoramaGame() }
    var cards by remember { mutableStateOf(game.createCards()) }
    var selectedCards by remember { mutableStateOf(listOf<CardModel>()) }
    var moves by remember { mutableStateOf(0) }
    var showWinDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(cards) {
        if (game.checkWinCondition(cards)) {
            showWinDialog = true
        }
    }

    fun resetGame() {
        cards = game.createCards()
        selectedCards = emptyList()
        moves = 0
        showWinDialog = false
    }

    fun onCardClick(card: CardModel) {
        if (card.isFaceUp || card.isMatched || selectedCards.size >= 2 || showWinDialog) return

        cards = cards.map { if (it.id == card.id) it.copy(isFaceUp = true) else it }
        selectedCards = selectedCards + card

        if (selectedCards.size == 2) {
            moves++
            scope.launch {
                delay(1000)
                cards = game.processTurn(selectedCards, cards)
                selectedCards = emptyList()
            }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "🎴 MEMORAMA NEON",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 22.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0x66000000))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(20.dp))
            Text(
                text = "MOVIMIENTOS: $moves",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00FFEE),
                modifier = Modifier
                    .background(Color(0x2200FFEE), RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(Modifier.height(12.dp))
            val matchedPairs = cards.count { it.isMatched } / 2
            val totalPairs = cards.size / 2
            Text(
                text = "PARES: $matchedPairs/$totalPairs",
                fontSize = 16.sp,
                color = Color(0xFFFF00FF),
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                ResponsiveCardGrid(cards = cards, onCardClick = ::onCardClick)
            }

            Spacer(Modifier.height(16.dp))
            NeonButton(onClick = { resetGame() }, text = "REINICIAR JUEGO")
        }
    }

    if (showWinDialog) {
        NeonWinDialog(moves = moves, onPlayAgain = { resetGame() }, onClose = { showWinDialog = false })
    }
}

@Composable
fun ResponsiveCardGrid(cards: List<CardModel>, onCardClick: (CardModel) -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val availableWidth = screenWidth - 32.dp
    val cardSpacing = 8.dp
    val cardSize = (availableWidth - cardSpacing * 3) / 4

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (row in 0 until 4) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                for (col in 0 until 4) {
                    val index = row * 4 + col
                    if (index < cards.size) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            ResponsiveNeonCardView(cards[index], onClick = { onCardClick(cards[index]) }, cardSize)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResponsiveNeonCardView(card: CardModel, onClick: () -> Unit, cardSize: Dp) {
    val (cardColor, glowColor, textColor) = when {
        card.isMatched -> Triple(Color(0x334CAF50), Color(0xFF00FF00), Color(0xFF00FF00))
        card.isFaceUp -> Triple(Color(0x332196F3), Color(0xFF0080FF), Color(0xFF00CCFF))
        else -> Triple(Color(0x33424242), Color(0xFF666666), Color(0xFFAAAAAA))
    }

    val finalCardSize = minOf(cardSize, 90.dp)


    Box(
        modifier = Modifier
            .size(finalCardSize)
            .background(cardColor, RoundedCornerShape(12.dp))
            .background(
                brush = Brush.radialGradient(listOf(glowColor.copy(alpha = 0.3f), Color.Transparent)),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = !card.isMatched, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (card.isFaceUp || card.isMatched) card.content else "?",
            fontSize = when {
                finalCardSize < 60.dp -> 18.sp
                finalCardSize < 70.dp -> 22.sp
                finalCardSize < 80.dp -> 26.sp
                else -> 30.sp
            },
            color = textColor,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun NeonButton(onClick: () -> Unit, text: String) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = Modifier
            .height(50.dp)
            .padding(bottom = 16.dp)
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun NeonWinDialog(moves: Int, onPlayAgain: () -> Unit, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onClose() },
        containerColor = Color(0xFF1A0033),
        titleContentColor = Color(0xFF00FFEE),
        textContentColor = Color.White,
        title = { Text("¡FELICIDADES! 🎉", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
        text = {
            Column {
                Text("¡Has ganado el juego!", fontSize = 18.sp)
                Spacer(Modifier.height(12.dp))
                Text("Movimientos totales: $moves", fontSize = 16.sp, color = Color(0xFFFF00FF))
            }
        },
        confirmButton = {
            Button(onClick = { onPlayAgain() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00FFEE))) {
                Text("JUGAR OTRA VEZ", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = { onClose() }) {
                Text("CERRAR", color = Color(0xFFFF00FF))
            }
        }
    )
}
