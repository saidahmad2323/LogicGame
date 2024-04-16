package com.example.operatorapp

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp


data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswer: String,
    val image: Int? = null
)

data class Player(
    val name: String,
    var score: Int = 0,
    var usedHint: Boolean = false
)

@AndroidEntryPoint
class QuizAppActivitys : ComponentActivity(){
    @Composable
    fun QuizApp() {
        var showGame by remember { mutableStateOf(false) }
        var playersInput by remember { mutableStateOf("") }
        var players by remember { mutableStateOf<List<Player>>(emptyList()) }
        val stateTake = remember { mutableStateOf(20) }
        val questions = listOf(
            Question("Как называется наиболее длинная река в мире?", listOf("Амазонка", "Нил", "Янцзы", "Миссисипи"), "Амазонка"),
            Question("Как называется самая высокая гора в мире?", listOf("Эльбрус", "Эверест", "Килиманджаро", "Дениали"), "Эверест"),
            Question("Как называется популярная головоломка, состоящая из 9 квадратных блоков с числами?", listOf("Кубик Рубика", "Лего", "Пазл", "Тетрис"), "Кубик Рубика"),
            Question("Как называется традиционная японская одежда?", listOf("Кимоно", "Сари", "Парео", "Ханбок"), "Кимоно"),
            Question("Как называется праздник, который отмечается 31 октября?", listOf("День святого Валентина", "Рождество", "Хэллоуин", "Пасха"), "Хэллоуин"),
            Question("Как называется игра, в которой нужно использовать буквы, чтобы составлять слова на игровом поле?", listOf("Скрэббл", "Монополия", "Мафия", "Шахматы"), "Скрэббл"),
            Question("Как называется известная французская карточная игра, в которой нужно собрать 21 очко?", listOf("Покер", "Рулетка", "Блэкджек", "Баккара"), "Блэкджек"),
            Question("Как называется культурное явление, включающее в себя театр, музыку, танец, пение и акробатику?", listOf("Кино", "Цирк", "Концерт", "Балет"), "Цирк"),
            Question("Как называется вид спорта, в котором соревнуются команды, состоящие из 11 игроков на каждой стороне?", listOf("Баскетбол", "Футбол", "Хоккей", "Теннис"), "Футбол"),
            Question("Как называется игра, в которой игроки пытаются угадать слова, используя показываемые картинки?", listOf("Монополия", "Пасьянс", "Что? Где? Когда?", "Диксит"), "Диксит")
        ).shuffled().take(30)
        val shuffledQuestions = remember { mutableStateOf(questions.shuffled()) }
        fun shuffleQuestions() {
            shuffledQuestions.value = questions.shuffled()
        }
        var showResults by remember { mutableStateOf(false) } // Состояния барои результати бозингарон

        // Функция барои аз нав кардани бози
        fun startNewGame() {
            showResults = false
            showGame = false
            players = emptyList()
            shuffleQuestions()
        }

        if (!showGame && !showResults) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = playersInput,
                    onValueChange = { playersInput = it },
                    label = { Text("Введите имена игроков через запятую") },
                    modifier = Modifier
                        .padding(17.dp)
                        .fillMaxWidth()

                )
                Button(
                    onClick = {
                        // Бозингаронро ба гурух таксим кардан
                        val playerNames = playersInput.split(",").map { it.trim() }
                        players = playerNames.map { Player(it) }
                        showGame = true
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Начать Играть!")
                }
            }
        } else {
            var currentQuestionIndex by remember { mutableStateOf(0) }
            var selectedAnswer by remember { mutableStateOf("") }
            var answered by remember { mutableStateOf(false) }
            val totalQuestions = shuffledQuestions.value.size
            var correctAnswers by remember { mutableStateOf(0) }
            var incorrectAnswers by remember { mutableStateOf(0) }

            var currentPlayerIndex by remember { mutableStateOf(0) }

            fun nextQuestion() {
                answered = false
                if (selectedAnswer == shuffledQuestions.value[currentQuestionIndex].correctAnswer) {
                    correctAnswers++
                    // Плюс бал барои игрок
                    players[currentPlayerIndex].score++
                } else {
                    incorrectAnswers++
                }
                currentQuestionIndex++
                selectedAnswer = ""

                // Бозингари навбати
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size

                if (currentQuestionIndex >= totalQuestions) {
                    showResults = true // Резултат агар саволхо ба охир расад
                }
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (currentQuestionIndex < totalQuestions) {
                    val currentQuestion = shuffledQuestions.value[currentQuestionIndex]
                    Text(
                        text = "${currentQuestionIndex + 1} от $totalQuestions",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally),
                        fontSize = 23.sp,
                        textAlign = TextAlign.Start
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth()
                        , horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (playersInput == "") "Одиночная игра" else "Вопрос игроку: ${players[currentPlayerIndex].name}",
                            modifier = Modifier
                                .wrapContentWidth(align = Alignment.CenterHorizontally),
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = currentQuestion.text,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally),
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn {
                        items(currentQuestion.options) { option ->
                            val isCorrect = option == currentQuestion.correctAnswer
                            val letters = listOf("A)", "B)", "C)", "D)")
                            val index = currentQuestion.options.indexOf(option)
                            ClickableCard(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        if (!answered) {
                                            selectedAnswer = option
                                            answered = true
                                        }
                                    },
                                backgroundColor = when {
                                    answered && isCorrect -> Color.Green
                                    answered && !isCorrect && selectedAnswer == option -> Color.Red
                                    else -> Color.White
                                }
                            ) {
                                Text(
                                    modifier = Modifier.padding(16.dp),
                                    text = "${letters[index]} $option",
                                    style = MaterialTheme.typography.body1,
                                    color = when {
                                        answered && isCorrect -> Color.White
                                        answered && !isCorrect && selectedAnswer == option -> Color.White
                                        else -> Color.Black
                                    }
                                )
                            }}
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    val stateAlert = remember{ mutableStateOf(false) }
                    // Добавляем кнопку подсказки
                    Button(
                        onClick = {
                            stateAlert.value = true
                        },
                        modifier = Modifier.padding(8.dp),
                        enabled = !players[currentPlayerIndex].usedHint
                    ) {
                        Text("Подсказка")
                    }
                    if (stateAlert.value){
                        AlertDialog(
                            onDismissRequest = {
                                stateAlert.value = false
                                players[currentPlayerIndex].usedHint = true
                            },
                            title = {
                                Text(text = "Подсказка")
                            },
                            text = {
                                Text(text = "${players[currentPlayerIndex].name} правильный ответ: ${currentQuestion.correctAnswer}")
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        players[currentPlayerIndex].usedHint = true
                                        stateAlert.value = false
                                    },
                                ) {
                                    Text("OK")
                                }
                            }
                        )
                    }

                    Button(
                        onClick = {
                            if (answered || selectedAnswer.isNotEmpty()) {
                                nextQuestion()
                            }
                        },
                        enabled = answered || selectedAnswer.isNotEmpty()
                    ) {
                        Text("Дальше")
                    }
                } else {
                    if (playersInput == "") {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Результаты игры", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Правильный ответ: $correctAnswers",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            )

                            Text(
                                text = "Неправильный ответ: $incorrectAnswers",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Button(
                                onClick = { startNewGame() },
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text("Повторить игру")
                            }
                        }
                    }
                    else Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Результаты игры", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        players.sortedByDescending { it.score }.forEachIndexed { index, player ->
                            Text(
                                text = "Игрок ${index + 1}: ${player.name}, Баллы: ${player.score}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        // Определение победителя или ничьи
                        val winner = players.maxByOrNull { it.score }
                        val draw = players.count { it.score == winner?.score } > 1
                        Text(
                            text = if (draw) "Ничья" else "Выиграл игру: ${winner?.name ?: "Нет победителя"}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )

                        // Кнопка "Повторить игру"
                        Button(
                            onClick = { startNewGame() },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Повторить игру")
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun ClickableCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        backgroundColor = backgroundColor,
        elevation = 4.dp
    ) {
        content()
    }
}
