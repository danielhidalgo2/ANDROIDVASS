package com.chat.whatsvass.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.chat.whatsvass.R
import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.model.message.Message

import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtils {

    @SuppressLint("SimpleDateFormat")
    fun formatTimeFromApi(dateTimeString: String, context: Context): String {
        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_MONTH]
        val month = calendar[Calendar.MONTH] + 1
        val year = calendar[Calendar.YEAR]
        val today = "$day-$month-$year"
        val todayMonthAndYear = "$month-$year"

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputFormatDate = SimpleDateFormat("d-M-yyyy")
        val outputFormatDay = SimpleDateFormat("d")
        val outputFormatDayToShow = SimpleDateFormat("d-M-yy")
        val outputFormatMonthAndYear = SimpleDateFormat("M-yyyy")
        val date = inputFormat.parse(dateTimeString)
        val dateToCompare = date?.let { outputFormatDate.format(it).toString() }

        return if (today == dateToCompare) {
            outputFormat.format(date)

        } else if ((todayMonthAndYear == outputFormatMonthAndYear.format(date!!)) && ((day - outputFormatDay.format(
                date
            ).toString().toInt()) == 1)
        ) {
            outputFormat.format(date) + "\n${context.getString(R.string.yesterday)}"

        } else {
            outputFormat.format(date) + "\n${outputFormatDayToShow.format(date)}"
        }
    }

    fun formatTimeFromApiToOrderList(dateTimeString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateTimeString)
        return outputFormat.format(date!!)
    }

    fun formatTimeFromApiHourChatView(dateTimeString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateTimeString)
        return outputFormat.format(date!!)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatTimeToSeparateMessages(dateTimeString: String, context: Context): String {
        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_MONTH]
        val month = calendar[Calendar.MONTH] + 1
        val year = calendar[Calendar.YEAR]
        val today = "$day-$month-$year"
        val todayMonthAndYear = "$month-$year"

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormatDate = SimpleDateFormat("d-M-yyyy")
        val outputFormatDay = SimpleDateFormat("d")
        val outputFormatMonthAndYear = SimpleDateFormat("M-yyyy")
        val outputFormatDayToShow = SimpleDateFormat("M-d-yy")
        val date = inputFormat.parse(dateTimeString)
        val dateToCompare = outputFormatDate.format(date!!).toString()

        // Si las fechas son iguales devuelve "hoy"
        return if (today == dateToCompare) {
            outputFormatDayToShow.format(date) + "\n${context.getString(R.string.today)}"
            // Si el mes y año son iguales, se resta el dia de hoy con el del ultimo mensaje, si es 1, el mensaje es de ayer
        } else if ((todayMonthAndYear == outputFormatMonthAndYear.format(date)) && ((day - outputFormatDay.format(
                date
            ).toString().toInt()) == 1)
        ){
            outputFormatDayToShow.format(date) + "\n${context.getString(R.string.yesterday)}"
            // Para el resto se muestra la hora y fecha
        } else {
            outputFormatDayToShow.format(date)
        }

    }
    fun orderChatsByDate(chats: List<Chat>, messages: Map<String, List<Message>>): List<String> {

        val mapOfChatIDandDateLast = mutableMapOf<String, String>()
        for (i in chats) {
            if (!messages[i.chatId].isNullOrEmpty()) {
                if (messages[i.chatId]!!.lastOrNull()!!.date.isNotEmpty()) {
                    val formattedTime =
                        formatTimeFromApiToOrderList(messages[i.chatId]!!.lastOrNull()!!.date)
                    mapOfChatIDandDateLast[i.chatId] = formattedTime
                }
            } else {
                mapOfChatIDandDateLast[i.chatId] = "0"
            }
        }
        val mapResultOrdered =
            mapOfChatIDandDateLast.toList().sortedByDescending { (_, value) -> value }.toMap()
        val listRestultOrdered = mapResultOrdered.keys.toList()
        Log.d("Mensajes ordenados", listRestultOrdered.toString())

        return listRestultOrdered
    }
}
