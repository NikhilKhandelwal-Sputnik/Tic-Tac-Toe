package com.example.tic_tac_toe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tic_tac_toe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                TicTacToePreview()
            }
        }
    }
}


enum class Player {
    X, O
}

enum class GameState {
    IN_PROGRESS, X_WINS, O_WINS, DRAW
}

@Composable
fun TicTacToe() {
    var board by remember { mutableStateOf(List(9) { "" }) }
    var currentPlayer by remember { mutableStateOf(Player.X) }
    var gameState by remember { mutableStateOf(GameState.IN_PROGRESS) }

    fun handleCellClick(index: Int) {
        if (board[index].isEmpty() && gameState == GameState.IN_PROGRESS) {
            board = board.toMutableList().also {
                it[index] = currentPlayer.name
            }
            currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X
            gameState = checkGameState(board)
        }
    }

    fun resetGame() {
        board = List(9) { "" }
        currentPlayer = Player.X
        gameState = GameState.IN_PROGRESS
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = when (gameState) {
                GameState.IN_PROGRESS -> "Player ${currentPlayer.name}'s Turn"
                GameState.X_WINS -> "Player X Wins!"
                GameState.O_WINS -> "Player O Wins!"
                GameState.DRAW -> "It's a Draw!"
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.size(300.dp)
        ) {
            items(board.indices.toList()) { index ->
                Cell(
                    value = board[index],
                    onClick = { handleCellClick(index) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (gameState != GameState.IN_PROGRESS) {
            Button(onClick = { resetGame() }) {
                Text("Play Again")
            }
        }
    }
}

@Composable
fun Cell(value: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.Yellow)
            .clickable { onClick() }
            .border(1.dp, Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = when (value) {
                "X" -> Color.Blue
                "O" -> Color.Red
                else -> Color.Black
            }
        )
    }
}

fun checkGameState(board: List<String>): GameState {
    val winningCombinations = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // Rows
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // Columns
        listOf(0, 4, 8), listOf(2, 4, 6) // Diagonals
    )

    for (combination in winningCombinations) {
        val (a, b, c) = combination
        if (board[a].isNotEmpty() && board[a] == board[b] && board[a] == board[c]) {
            return if (board[a] == "X") GameState.X_WINS else GameState.O_WINS
        }
    }

    if (board.all { it.isNotEmpty() }) {
        return GameState.DRAW
    }

    return GameState.IN_PROGRESS
}




@Preview(showBackground = true)
@Composable
fun TicTacToePreview() {
    TicTacToeTheme {
        TicTacToe()
    }
}