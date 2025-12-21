//
//  ContentView.swift
//  Weather
//
//  Created by Rick Pasetto on 12/21/25.
//

import SwiftUI

struct ContentView: View {
    @StateObject private var viewModel = WeatherViewModel()
    
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
            Text(weather.city.name)
                .font(.headline)
            
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
