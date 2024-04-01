package com.chat.whatsvass.utils

import android.content.Context
import android.util.Log
import com.chat.whatsvass.R
import com.chat.whatsvass.data.domain.model.chat.Chat
import com.chat.whatsvass.data.domain.model.message.Message

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    fun formatTimeFromApi(dateTimeString: String, context: Context): String {
        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_MONTH]
        val month = calendar[Calendar.MONTH] + 1
        val year = calendar[Calendar.YEAR]
        val today = "$day-$month-$year"
        val todayMonthAndYear = "$month-$year"
        Log.d("FECHAACTUAL", todayMonthAndYear)

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputFormatDate = SimpleDateFormat("d-M-yyyy")
        val outputFormatDay = SimpleDateFormat("d")
        val outputFormatDayToShow = SimpleDateFormat("d-M-yy")
        val outputFormatMonthAndYear = SimpleDateFormat("M-yyyy")
        val date = inputFormat.parse(dateTimeString)
        val dateToCompare = outputFormatDate.format(date).toString()
        Log.d("FECHAACTUAL", outputFormatMonthAndYear.format(date!!))


        if (today == dateToCompare) {
            return outputFormat.format(date)

        } else if ((todayMonthAndYear == outputFormatMonthAndYear.format(date!!)) && ((day - outputFormatDay.format(
                date
            ).toString().toInt()) == 1)
        ) {
            return outputFormat.format(date) + "\n${context.getString(R.string.yesterday)}"

        } else {
            return outputFormat.format(date) + "\n${outputFormatDayToShow.format(date)}"
        }
    }

    fun formatTimeFromApiToOrderList(dateTimeString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateTimeString)
        return outputFormat.format(date!!)
    }

    fun orderChatsByDate(chats: List<Chat>, messages: Map<String, List<Message>>): List<String> {

        val mapOfChatIDandDateLast = mutableMapOf<String, String>()
        for (i in chats) {
            if (!messages[i.chatId].isNullOrEmpty()) {
                if (!messages[i.chatId]!!.lastOrNull()!!.date.isNullOrEmpty()) {
                    val formattedTime =
                        messages[i.chatId]!!.lastOrNull()!!.date.let { formatTimeFromApiToOrderList(it) }
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
