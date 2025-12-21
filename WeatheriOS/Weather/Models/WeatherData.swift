//
//  WeatherData.swift
//  Weather
//
//  Created by Rick Pasetto on 12/21/25.
//

import Foundation

struct WeatherInfo: Identifiable {
    let id = UUID()
    let city: City
    let currentTemperature: Int?
    let currentCondition: String?
    let forecast: [ForecastPeriod]
}

struct ForecastPeriod: Identifiable {
    let id = UUID()
    let name: String
    let temperature: Int
    let shortForecast: String
}

struct ForecastPeriodDTO: Codable {
    let name: String
    let temperature: Int
    let shortForecast: String
    
    func toForecastPeriod() -> ForecastPeriod {
        ForecastPeriod(name: name, temperature: temperature, shortForecast: shortForecast)
    }
}

struct PointsResponse: Codable {
    let properties: PointsProperties
}

struct PointsProperties: Codable {
    let gridId: String
    let gridX: Int
    let gridY: Int
    let forecast: URL
}

struct ForecastResponse: Codable {
    let properties: ForecastProperties
}

struct ForecastProperties: Codable {
    let periods: [ForecastPeriodDTO]
    
    var forecastPeriods: [ForecastPeriod] {
        periods.map { $0.toForecastPeriod() }
    }
}

struct ObservationResponse: Codable {
    let properties: ObservationProperties?
}

struct ObservationProperties: Codable {
    let temperature: TemperatureValue?
    let textDescription: String?
}

struct TemperatureValue: Codable {
    let value: Double?
}

