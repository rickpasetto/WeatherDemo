//
//  WeatherRowView.swift
//  Weather
//
//  Created by Rick Pasetto on 12/21/25.
//

import SwiftUI

struct WeatherRowView: View {
    let weather: WeatherInfo
    
    private func formatTemperature(_ celsius: Int) -> String {
        let temperature = Measurement(value: Double(celsius), unit: UnitTemperature.celsius)
        let formatter = MeasurementFormatter()
        formatter.numberFormatter.maximumFractionDigits = 0
        return formatter.string(from: temperature)
    }
    
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
                Text("\(currentEmoji) Current: \(formatTemperature(temp)) \(weather.currentCondition.map { $0.isEmpty ? "" : " - \($0)" } ?? "")")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
            }
            
            if let firstForecast = weather.forecast.first {
                let forecastEmoji = WeatherEmoji.emoji(for: firstForecast.shortForecast)
                Text("\(forecastEmoji) \(firstForecast.name): \(formatTemperature(firstForecast.temperature)) - \(firstForecast.shortForecast)")
                    .font(.caption)
                    .foregroundColor(.secondary)
            }
        }
        .padding(.vertical, 4)
    }
}

