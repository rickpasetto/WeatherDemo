//
//  WeatherEmoji.swift
//  Weather
//
//  Created by Rick Pasetto on 12/21/25.
//

import Foundation

struct WeatherEmoji {
    static func emoji(for condition: String?) -> String {
        guard let condition = condition?.lowercased() else {
            return "🌤️"
        }
        
        if condition.contains("sunny") || condition.contains("clear") {
            return "☀️"
        } else if condition.contains("partly cloudy") {
            return "⛅"
        } else if condition.contains("cloudy") || condition.contains("overcast") {
            return "☁️"
        } else if condition.contains("rain") || condition.contains("shower") {
            return "🌧️"
        } else if condition.contains("thunderstorm") || condition.contains("storm") {
            return "⛈️"
        } else if condition.contains("snow") || condition.contains("sleet") {
            return "❄️"
        } else if condition.contains("fog") || condition.contains("mist") {
            return "🌫️"
        } else if condition.contains("wind") {
            return "💨"
        } else if condition.contains("haze") {
            return "🌫️"
        } else {
            return "🌤️"
        }
    }
}

