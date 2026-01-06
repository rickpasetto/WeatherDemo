package com.weatherdemo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weatherdemo.viewmodels.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherListScreen(
    viewModel: WeatherViewModel = viewModel()
) {
    val weatherData by viewModel.weatherData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        viewModel.loadWeather()
    }
    
    Scaffold(
        topBar = {
            if (isSearchActive) {
                SearchBar(
                    query = searchText,
                    onQueryChange = { searchText = it },
                    onSearch = {
                        if (searchText.isNotEmpty()) {
                            viewModel.addCity(searchText)
                            searchText = ""
                            isSearchActive = false
                        }
                    },
                    active = isSearchActive,
                    onActiveChange = { isSearchActive = it },
                    leadingIcon = {
                        IconButton(onClick = { isSearchActive = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close search"
                            )
                        }
                    },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = { searchText = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear search"
                                )
                            }
                        }
                    },
                    placeholder = { Text("Search for a city") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (searchText.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                TextButton(
                                    onClick = {
                                        viewModel.addCity(searchText)
                                        searchText = ""
                                        isSearchActive = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Search for \"$searchText\"")
                                }
                            }
                        }
                    }
                }
            } else {
                TopAppBar(
                    title = { Text("US Weather") },
                    actions = {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator()
                        Text(text = "Loading weather…")
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(weatherData) { weather ->
                            WeatherRow(weather = weather)
                        }
                    }
                }
            }
            
            SearchLoadingOverlay(isSearching = isSearching)
        }
    }
}

