package com.holovin.reboottracker.ui.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.holovin.reboottracker.data.repository.RebootEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val rebootEventRepository: RebootEventRepository
) : ViewModel() {
    private val _uiState = MutableLiveData<MainScreenUiState>(MainScreenUiState.Loading)
    val uiState: LiveData<MainScreenUiState> = _uiState

    init {
        viewModelScope.launch {
            val reboots = rebootEventRepository.getEvents(false)
            _uiState.value = MainScreenUiState.Content(reboots.map { it.date })
        }
    }
}

