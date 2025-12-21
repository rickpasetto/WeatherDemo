//
//  ContentView.swift
//  Weather
//
//  Created by Rick Pasetto on 12/21/25.
//

import SwiftUI

struct ContentView: View {
    @StateObject private var viewModel = WeatherViewModel()
    @State private var searchText = ""
    
    var body: some View {
        NavigationView {
            Group {
                if viewModel.isLoading {
                    ProgressView("Loading weather...")
                } else {
                    List(viewModel.weatherData) { weather in
                        WeatherRowView(weather: weather)
                    }
                }
            }
            .navigationTitle("US Weather")
            .searchable(text: $searchText, prompt: "Search for a city")
            .onSubmit(of: .search) {
                Task {
                    await viewModel.addCity(name: searchText)
                    searchText = ""
                }
            }
            .overlay {
                if viewModel.isSearching {
                    ZStack {
                        Color.black.opacity(0.3)
                            .ignoresSafeArea()
                        
                        VStack(spacing: 16) {
                            ProgressView()
                                .scaleEffect(1.5)
                            Text("Searching for city...")
                                .font(.headline)
                                .foregroundColor(.primary)
                        }
                        .padding(24)
                        .background(Color(.systemBackground))
                        .cornerRadius(12)
                        .shadow(radius: 10)
                    }
                }
            }
            .task {
                await viewModel.loadWeather()
            }
        }
    }
}

struct WeatherRowView: View {
    let weather: WeatherInfo
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                Text(weather.city.name)
                    .font(.headline)
                if let state = weather.city.state {
                    Text(state)
                        .font(.headline)
                        .foregroundColor(.secondary)
                }
            }
            
            if let temp = weather.currentTemperature {
                let currentEmoji = WeatherEmoji.emoji(for: weather.currentCondition)
                Text("\(currentEmoji) Current: \(temp)°F \(weather.currentCondition ?? "")")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
            }
            
            if let firstForecast = weather.forecast.first {
                let forecastEmoji = WeatherEmoji.emoji(for: firstForecast.shortForecast)
                Text("\(forecastEmoji) \(firstForecast.name): \(firstForecast.temperature)°F - \(firstForecast.shortForecast)")
                    .font(.caption)
                    .foregroundColor(.secondary)
            }
        }
        .padding(.vertical, 4)
    }
}

#Preview {
    ContentView()
}
