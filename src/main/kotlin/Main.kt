var count: Int = 15_000_000
var limitTransit = 0
fun main() {

    println("Добрый день, Вы используйте систему оплаты ShvetsPay. Просим Вас выбрать действие в меню ниже.")
    while (true) {
        println(
            """
            Введите следующее действие:
            1. Посмотреть баланс;
            2. Посмотреть условия перевода;
            3. Посмотреть сумму переводов за месяц
            4. Осуществить перевод;
            5. Завершить работу с приложением.
        """.trimIndent()
        )
        try {
            var chose: Int = readLine()?.toInt() ?: 0
            when (chose) {
                1 -> {
                    println("Ваш баланс = ${convertToRubles(count)} руб. (или $count копеек)")
                }
                2 -> println(
                    """
                    Условия перевода: 
                    Отправлять деньги можно получателю введя имя и фамилию. Номер карты указывать не надо.
                    За переводы с карт Mastercard и Maestro - до 75 т.р. не взимается. При превышении лимита, комиссия 0,6% + 20 рублей.
                    За переводы с карт Visa и Мир -  комиссия 0.75%, минимум 35 рублей.
                    Используя спобоб оплаты Vk Pay - не взимается.
                """.trimIndent()
                )
                3 -> println("За этот месяц вы перевели ${convertToRubles(limitTransit)} руб. (или $limitTransit копеек)")
                4 -> {
                    transitMoney()
                }
                5 -> break
                else -> println("Вы вели число, которого нет в меню. \n")
            }
        } catch (e: Exception) {
            println("Необходимо выбрать цифру")
        } finally {
            println("----------------------------------------------------------------------------------------------------")
        }

    }
    println(" Спасибо, что воспользовались системой ShvetsPay. Ваш баланс ${convertToRubles(count)} руб. (или $count копеек)")
}

fun transitMoney() {
    println("Введите имя и фамилию получателя через пробел для перевода:")
    var nameAndSurname: String? = readLine()
    println("Введите сумму (руб.) для перевода:")
    var amount: Int = (readLine()?.toInt() ?: 0) * 100
    println("Выберите карту, через которую будет производиться перевод:")
    println(
        """
        1. VK Pay
        2. Mastercard
        3. Maestro
        4. Visa
        5. Мир
    """.trimIndent()
    )
    var chose: Int = readLine()?.toInt() ?: 0
    val card: String = when (chose) {
        1 -> "VK Pay"
        2 -> "Mastercard"
        3 -> "Maestro"
        4 -> "Visa"
        5 -> "Мир"
        else -> {
            "Нет такой карты"
        }
    }

    if (card != "Нет такой карты") {
        val result: Int = countCommission(card, count, amount, limitTransit)
        count -= (result + amount)
        limitTransit += amount
        println(
            "Вы перевели пользователю $nameAndSurname ${convertToRubles(amount)} руб. (или $amount копеек), " +
                    "комиссия составляет ${convertToRubles(result)} руб. (или $result копеек), остаток на счете ${
                        convertToRubles(count)
                    } руб. (или $count копеек). Перевод осуществлен с помощью $card"
        )
    } else println("Нет такой карты")
}


fun countCommission(card: String = "VK Pay", count: Int, amount: Int, limitTransit: Int): Int {
    val commission = when (card) {
        "Mastercard", "Maestro" -> {
            if (limitTransit < 7500000) 0 else (amount * 60 / 10000)
        }
        "Visa", "Мир" -> {
            if ((amount * 75 / 10000) < 3500) 3500 else (amount * 75 / 10000)
        }
        else -> 0
    }
    return commission
}


fun convertToRubles(value: Int): Double {
    return value.toDouble() / 100
}