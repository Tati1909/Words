package com.example.wordsapp

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wordsapp.databinding.FragmentLetterListBinding

class LetterListFragment : Fragment() {

    private var _binding: FragmentLetterListBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    //флажок разметки кнопок: буквы в линейной компановке(по умолчанию)
    private var isLinearLayoutManager = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //отображение переметров меню
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLetterListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.recyclerView
        chooseLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Чтобы ваше приложение действительно могло использовать меню, вам нужно переопределить еще два метода:
    //onCreateOptionsMenu: где вы расширяете меню опций и выполняете любые дополнительные настройки
    //onOptionsItemSelected: когда вы  будете  вызывать chooseLayout(), если кнопка будет нажата.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)

        val layoutIcon = menu.findItem(R.id.menu_switch_icon)
        setIcon(layoutIcon)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //Если пользователь нажал на иконку переключателя меню(список-сетка), то ->
            R.id.menu_switch_icon -> {
                // Устанавливаем isLinearLayoutManager (логическое значение) на противоположное значение
                isLinearLayoutManager = !isLinearLayoutManager
                // Меняем макет и иконку
                chooseLayout()
                setIcon(item)

                return true
            }
            //  // В противном случае ничего не делать и использовать базовую обработку событий
            //
            // когда предложения требуют, чтобы все возможные пути были явно учтены,
            // например, как истинный, так и ложный случай, если значение является логическим,
            // или другое, чтобы поймать все необработанные случаи.
            else -> super.onOptionsItemSelected(item)
        }
    }

    //устанавливаем настройки списка или плитки в меню приложения
    private fun chooseLayout() {
        if (isLinearLayoutManager) {
            recyclerView.layoutManager = LinearLayoutManager(context)
        } else {
            recyclerView.layoutManager = GridLayoutManager(context, 4)
        }
        recyclerView.adapter = LetterAdapter()
    }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return

        // Установим возможность рисования для значка меню в зависимости от того,
        // какой LayoutManager используется в настоящее время
        // if (isLinearLayoutManager)
        //     menu.icon = ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_grid_layout)
        // else menu.icon = ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_linear_layout)
        menuItem.icon =
            if (isLinearLayoutManager)
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_grid_layout)
            else ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_linear_layout)
    }

}