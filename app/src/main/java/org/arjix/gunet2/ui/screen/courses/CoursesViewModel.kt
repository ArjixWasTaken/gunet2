package org.arjix.gunet2.ui.screen.home

import androidx.lifecycle.ViewModel
import org.arjix.gunet2.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor() : ViewModel() {
    private val _greetingsRes = MutableStateFlow(R.string.label_hello_world)
    val greetingsRes: StateFlow<Int> = _greetingsRes

    fun onClickMeClicked() {
        // Toggling message
        val newGreetingsRes = if (greetingsRes.value == R.string.label_hello_world) {
            R.string.label_hello_compose
        } else {
            R.string.label_hello_world
        }
        _greetingsRes.value = newGreetingsRes
    }
}