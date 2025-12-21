//
//  WeatherViewModel.swift
//  Weather
//
//  Created by Rick Pasetto on 12/21/25.
//

import Foundation
import Combine

@MainActor
class WeatherViewModel: ObservableObject {
    @Published var weatherData: [WeatherInfo] = []
    @Published var isLoading = false
    @Published var errorMessage: String?
    
    private let weatherService = WeatherService()
    private let cities: [City] = [
        City(name: "New York", latitude: 40.7128, longitude: -74.0060),
        City(name: "Los Angeles", latitude: 34.0522, longitude: -118.2437),
        City(name: "Chicago", latitude: 41.8781, longitude: -87.6298),
        City(name: "Houston", latitude: 29.7604, longitude: -95.3698),
        City(name: "Phoenix", latitude: 33.4484, longitude: -112.0740),
        City(name: "Philadelphia", latitude: 39.9526, longitude: -75.1652),
        City(name: "San Antonio", latitude: 29.4241, longitude: -98.4936),
        City(name: "San Diego", latitude: 32.7157, longitude: -117.1611),
        City(name: "Dallas", latitude: 32.7767, longitude: -96.7970),
        City(name: "San Jose", latitude: 37.3382, longitude: -121.8863)
    ]
    
    func loadWeather() async {
        isLoading = true
        errorMessage = nil
        
        var results: [WeatherInfo] = []
        
        for city in cities {
            do {
                let weather = try await weatherService.fetchWeather(for: city)
                results.append(weather)
            } catch {
                errorMessage = "Failed to load weather: \(error.localizedDescription)"
            }
        }
        
        weatherData = results
        isLoading = false
    }
}

