package com.example.games.sudoku

import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.games.sudoku.common.*
import com.example.games.ui.theme.GamesTheme
import kotlinx.coroutines.launch

class SudokuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF071026)
                ) {
                    SudokuScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SudokuScreen() {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var selectedDifficulty by remember { mutableStateOf(Difficulty.EASY) }
    var showWinDialog by remember { mutableStateOf(false) }
    var gameFinished by remember { mutableStateOf(false) }

    val accentCyan = Color(0xFF00E5FF)
    val accentPurple = Color(0xFF7C4DFF)
    val cardDark = Color(0xFF121826)
    val boardBorder = Color(0xFF00E5FF)

    // Motor del juego (no arrancamos el timer aquí; GameEngine.init() lo hará)
    val engine = remember {
        val timer = CountUpTimer()
        GameEngine(timer)
    }

    // Suscribir el estado del tablero y el timer
    val currentBoard by engine.currentBoard.collectAsState(initial = emptyList())
    val seconds by engine.second.collectAsState(initial = 0)

    // Selección y modo
    var selectedCell by remember { mutableStateOf<Cell?>(null) }
    var pencilMode by remember { mutableStateOf(false) }

    // Limpiar resources al salir
    DisposableEffect(Unit) {
        onDispose {
            engine.clear()
            engine.pause()
        }
    }

    // Mostrar diálogo de victoria según flow del engine
    LaunchedEffect(engine) {
        engine.win.collect { win ->
            if (win) {
                showWinDialog = true
                gameFinished = true
                engine.pause()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("🧩 Sudoku", color = accentCyan, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF0B1630))
            )
        },
        containerColor = Color(0xFF071026)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Timer y modo pencil
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tiempo: ${formatTime(seconds.toLong())}",
                    fontSize = 18.sp,
                    color = accentCyan
                )
                IconButton(onClick = { pencilMode = !pencilMode }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Pencil",
                        tint = if (pencilMode) accentPurple else Color(0xFFBFC8D8)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Selector de dificultad (en español)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Difficulty.values().forEach { difficulty ->
                    val isSelected = difficulty == selectedDifficulty
                    Button(
                        onClick = { selectedDifficulty = difficulty },
                        colors = ButtonDefaults.buttonColors(containerColor = if (isSelected) accentPurple else cardDark),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            when (difficulty) {
                                Difficulty.EASY -> "Fácil"
                                Difficulty.MEDIUM -> "Medio"
                                Difficulty.HARD -> "Difícil"
                            },
                            color = if (isSelected) Color.White else Color(0xFFBFC8D8)
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // Generar tablero
            Button(
                onClick = {
                    scope.launch {
                        engine.init(selectedDifficulty)
                        selectedCell = null
                        gameFinished = false
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = accentCyan),
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(vertical = 6.dp)
            ) {
                Text("Generar tablero", color = Color.Black, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }

            Spacer(Modifier.height(12.dp))

            // TABLERO: contenedor cuadrado y responsive
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(4.dp)
                    .background(Color(0xFF071026)),
                contentAlignment = Alignment.Center
            ) {
                SudokuGrid(
                    currentBoard = currentBoard,
                    selectedCell = selectedCell,
                    onCellClick = { cell ->
                        // solo permitir selección si la celda es editable (missingNum == true)
                        if (cell != null && cell.missingNum) {
                            selectedCell = cell
                        } else {
                            Toast.makeText(context, "No editable", Toast.LENGTH_SHORT).show()
                        }
                    },
                    boardBorder = boardBorder,
                    highlightColor = accentPurple
                )
            }

            Spacer(Modifier.height(14.dp))

            // Controles (Deshacer / Rehacer / Resolver)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { engine.undo() }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Deshacer", tint = Color.White)
                }
                IconButton(onClick = { engine.redo() }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Rehacer", tint = Color.White)
                }
                Button(
                    onClick = {
                        scope.launch {
                            engine.solve()      // llena todas las casillas
                            engine.pause()      // detiene el timer
                            gameFinished = true // bloquea botones
                            showWinDialog = true // muestra popup de victoria
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = accentPurple),
                    modifier = Modifier.weight(2f)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Resolver", tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Resolver", color = Color.White)
                }

            }

            Spacer(Modifier.height(8.dp))

            // Números 1..9 responsive
            NumberPad(
                onNumber = { n ->
                    if (selectedCell == null) {
                        Toast.makeText(context, "Selecciona una celda primero", Toast.LENGTH_SHORT).show()
                        return@NumberPad
                    }
                    // Llamar a updateBoard
                    scope.launch {
                        engine.updateBoard(selectedCell!!, n, if (pencilMode) SudokuGameAction.PENCIL else SudokuGameAction.NORMAL)
                        // para visual feedback, podríamos limpiar selección o mantenerla
                    }
                },
                enabled = !gameFinished
            )
        }
    }

    if (showWinDialog) {
        AlertDialog(
            onDismissRequest = { showWinDialog = false },
            confirmButton = { Button(onClick = { showWinDialog = false }) { Text("¡Excelente!") } },
            title = { Text("¡Felicidades!", color = accentCyan) },
            text = { Text("¡Completaste el Sudoku correctamente!\nTiempo: ${formatTime(seconds.toLong())}") },
            containerColor = Color(0xFF0F1A2A),
            titleContentColor = Color.White,
            textContentColor = Color.White
        )
    }
}

/** Construye y dibuja la grid 9x9 a partir de la estructura "parent blocks" que devuelve GameEngine */
@Composable
fun SudokuGrid(
    currentBoard: List<Cell>,
    selectedCell: Cell?,
    onCellClick: (Cell?) -> Unit,
    boardBorder: Color,
    highlightColor: Color
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .drawBehind {
                // dibujar líneas del grid (fina y gruesa cada 3)
                val w = size.width
                val h = size.height
                val cellW = w / 9f
                val cellH = h / 9f
                val thin = 1.dp.toPx()
                val thick = 3.dp.toPx()
                for (i in 0..9) {
                    val x = i * cellW
                    drawLine(
                        color = boardBorder,
                        start = Offset(x, 0f),
                        end = Offset(x, h),
                        strokeWidth = if (i % 3 == 0) thick else thin
                    )
                }
                for (j in 0..9) {
                    val y = j * cellH
                    drawLine(
                        color = boardBorder,
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = if (j % 3 == 0) thick else thin
                    )
                }
            }
    ) {
        val maxW = maxWidth
        val cellSize = maxW / 9f

        // convertir currentBoard (9 blocks) a grid 9x9
        val grid: List<List<Cell?>> = remember(currentBoard) {
            val g = List(9) { MutableList<Cell?>(9) { null } }
            currentBoard.forEachIndexed { blockIndex, parent ->
                val blockRow = blockIndex / 3
                val blockCol = blockIndex % 3
                parent.subCells.forEachIndexed { subIndex, cell ->
                    val sr = subIndex / 3
                    val sc = subIndex % 3
                    val r = blockRow * 3 + sr
                    val c = blockCol * 3 + sc
                    g[r][c] = cell
                }
            }
            g.map { it.toList() }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            for (r in 0 until 9) {
                Row(modifier = Modifier.height(cellSize), horizontalArrangement = Arrangement.Start) {
                    for (c in 0 until 9) {
                        val cell = grid[r][c]
                        val isSelected = selectedCell != null && cell != null && selectedCell.id == cell.id
                        Box(
                            modifier = Modifier
                                .size(cellSize)
                                .clickable { onCellClick(cell) }
                                .background(if (isSelected) highlightColor.copy(alpha = 0.12f) else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            val text = cell?.n?.takeIf { it in 1..9 }?.toString() ?: ""
                            Text(text = text, color = if (cell?.missingNum == true) Color(0xFFECEFF6) else Color(0xFF9FB3C8), fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NumberPad(onNumber: (Int) -> Unit, enabled: Boolean) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val rows = listOf(listOf(1,2,3,4,5), listOf(6,7,8,9))
        rows.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { n ->
                    Button(
                        onClick = { if (enabled) onNumber(n) },
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2632)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("$n", color = Color(0xFFECEFF6), fontSize = 18.sp)
                    }
                }
            }
        }
    }
}

fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", minutes, secs)
}
