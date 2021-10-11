/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.wordsapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

/**
 * Адаптер для [RecyclerView] в [DetailActivity].
 */
class WordAdapter(private val letterId: String, context: Context) :
    RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    private val filteredWords: List<String>

    init {
        //Получаем список слов из res / values / arrays.xml
        val words = context.resources.getStringArray(R.array.words).toList()

        filteredWords = words
            // Возвращает элементы в коллекции, если условное предложение истинно,
            // в этом случае, если элемент начинается с данной буквы,
            // игнорирование ВЕРХНИХ или строчных букв
            .filter { it.startsWith(letterId, ignoreCase = true) }
            // Returns a collection that it has shuffled in place
            .shuffled()
            // Returns the first n items as a [List]
            .take(5)
            // Returns a sorted version of that [List]
            .sorted()
    }

    class WordViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val button = view.findViewById<Button>(R.id.list_item)
    }

    override fun getItemCount(): Int = filteredWords.size

    /**
     * Создает новые view с R.layout.item_view в качестве шаблона
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_view, parent, false)

        // Setup custom accessibility delegate to set the text read
        layout.accessibilityDelegate = Accessibility

        return WordViewHolder(layout)
    }

    /**
     * Заменяет содержимое существующего view новыми данными
     */
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {

        val item = filteredWords[position]
        // context нужен для запуска интента в startActivity
        val context = holder.view.context

        // Устанавливаем текст WordViewHolder
        holder.button.text = item
        //устанавливаем слушатель и интент(выходим в интернет)
        //ACTION_VIEW- это общее намерение, которое принимает URI, в вашем случае веб-адрес.
        //Затем система знает, как обработать это намерение, открыв URI в веб-браузере пользователя.
        // Некоторые другие типы намерений включают:
        //CATEGORY_APP_MAPS - запуск приложения карт
        //CATEGORY_APP_EMAIL - запуск почтового приложения
        //CATEGORY_APP_GALLERY - запуск приложения "Галерея (фото)"
        //ACTION_SET_ALARM - установка будильника в фоновом режиме
        //ACTION_DIAL - совершение телефонного звонка
        holder.button.setOnClickListener {
            val queryUrl: Uri = Uri.parse("${WordListFragment.SEARCH_PREFIX}${item}")
            val intent = Intent(Intent.ACTION_VIEW, queryUrl)
            context.startActivity(intent)
        }
    }

    // Настройте AccessibilityDelegate(), чтобы установить текст,
    // читаемый с помощью службы Accessibility (доступности)
    companion object Accessibility : View.AccessibilityDelegate() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onInitializeAccessibilityNodeInfo(
            host: View?,
            info: AccessibilityNodeInfo?
        ) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            // Если вторым аргументом [AccessibilityAction] будет `null`,
            // служба доступности объявляет «двойное касание для активации».
            // Если указана настраиваемая строка,
            // он объявляет «двойное нажатие на <настраиваемую строку>».
            val customString = host?.context?.getString(R.string.look_up_word)
            val customClick =
                AccessibilityNodeInfo.AccessibilityAction(
                    AccessibilityNodeInfo.ACTION_CLICK,
                    customString
                )
            info?.addAction(customClick)
        }
    }
}