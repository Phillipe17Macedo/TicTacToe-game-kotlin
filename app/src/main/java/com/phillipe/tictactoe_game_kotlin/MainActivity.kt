package com.phillipe.tictactoe_game_kotlin

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color

class MainActivity : AppCompatActivity() {

    private var activePlayer = 1 // Jogador 1 = X, Jogador 2 = O
    private var gameState = IntArray(9) { 0 } // 0: vazio, 1: X, 2: O
    private val winningPositions = arrayOf(
        intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8), // Linhas
        intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8), // Colunas
        intArrayOf(0, 4, 8), intArrayOf(2, 4, 6) // Diagonais
    )
    private var gameActive = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val statusTextView = findViewById<TextView>(R.id.tvStatus)
        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)

        // Configura os botões do tabuleiro
        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as Button
            button.setOnClickListener {
                makeMove(button, i)
                statusTextView.text = getStatusMessage()
            }
        }

        // Botão para reiniciar o jogo
        findViewById<Button>(R.id.btnReset).setOnClickListener {
            resetGame(gridLayout, statusTextView)
        }
    }

    // Faz uma jogada e atualiza o estado do jogo
    private fun makeMove(button: Button, position: Int) {
        if (gameState[position] == 0 && gameActive) {
            gameState[position] = activePlayer
            button.text = if (activePlayer == 1) "X" else "O"
            button.setTextColor(if (activePlayer == 1) Color.parseColor("#F27C38") else Color.parseColor("#F26938"))
            button.isEnabled = false
            if (checkWinner()) {
                gameActive = false
            } else {
                activePlayer = if (activePlayer == 1) 2 else 1 // Alterna jogadores
            }
        }
    }

    // Verifica se há um vencedor
    private fun checkWinner(): Boolean {
        for (winningPosition in winningPositions) {
            val (a, b, c) = winningPosition
            if (gameState[a] == gameState[b] && gameState[b] == gameState[c] && gameState[a] != 0) {
                return true // Temos um vencedor
            }
        }
        // Verifica se deu empate
        if (gameState.all { it != 0 }) {
            gameActive = false
        }
        return false
    }

    // Mensagem de status (Vencedor, empate ou próximo jogador)
    private fun getStatusMessage(): String {
        return when {
            checkWinner() -> "Jogador ${if (activePlayer == 1) "X" else "O"} venceu!"
            !gameActive -> "Empate!"
            else -> "Jogador ${if (activePlayer == 1) "X" else "O"}"
        }
    }

    // Reinicia o jogo
    private fun resetGame(gridLayout: GridLayout, statusTextView: TextView) {
        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as Button
            button.text = ""
            button.isEnabled = true
        }
        gameState.fill(0)
        activePlayer = 1
        gameActive = true
        statusTextView.text = "Jogador 1: X"
    }
}