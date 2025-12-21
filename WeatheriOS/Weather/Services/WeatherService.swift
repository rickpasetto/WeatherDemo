//
//  WeatherService.swift
//  Weather
//
//  Created by Rick Pasetto on 12/21/25.
//

import Foundation

class WeatherService {
    private let baseURL = "https://api.weather.gov"
    
    func fetchWeather(for city: City) async throws -> WeatherInfo {
        let points = try await fetchGridPoints(latitude: city.latitude, longitude: city.longitude)
        let forecast = try await fetchForecast(url: points.properties.forecast)
        let currentWeather = try? await fetchCurrentWeather(
            gridId: points.properties.gridId,
            gridX: points.properties.gridX,
            gridY: points.properties.gridY
        )
        
        return WeatherInfo(
            city: city,
            currentTemperature: currentWeather?.temperature,
            currentCondition: currentWeather?.condition,
            forecast: forecast.properties.forecastPeriods
        )
    }
    
    private func makeRequest(url: URL) async throws -> (Data, URLResponse) {
        var request = URLRequest(url: url)
        request.setValue("WeatherApp (iOS)", forHTTPHeaderField: "User-Agent")
        return try await URLSession.shared.data(for: request)
    }
    
    private func fetchGridPoints(latitude: Double, longitude: Double) async throws -> PointsResponse {
        let url = URL(string: "\(baseURL)/points/\(latitude),\(longitude)")!
        let (data, _) = try await makeRequest(url: url)
        return try JSONDecoder().decode(PointsResponse.self, from: data)
    }
    
    private func fetchForecast(url: URL) async throws -> ForecastResponse {
        let (data, _) = try await makeRequest(url: url)
        return try JSONDecoder().decode(ForecastResponse.self, from: data)
    }
    
    private func fetchCurrentWeather(gridId: String, gridX: Int, gridY: Int) async throws -> (temperature: Int?, condition: String?)? {
        let url = URL(string: "\(baseURL)/gridpoints/\(gridId)/\(gridX),\(gridY)/stations")!
        let (data, _) = try await makeRequest(url: url)
        let stationsResponse = try JSONDecoder().decode(StationsResponse.self, from: data)
        
        guard let stationId = stationsResponse.features.first?.properties.stationIdentifier else {
            return nil
        }
        
        let observationURL = URL(string: "\(baseURL)/stations/\(stationId)/observations/latest")!
        let (obsData, _) = try await makeRequest(url: observationURL)
        let observation = try JSONDecoder().decode(ObservationResponse.self, from: obsData)
        
        let temperature = observation.properties?.temperature?.value.map { Int($0) }
        let condition = observation.properties?.textDescription
        
        return (temperature: temperature, condition: condition)
    }
}

struct StationsResponse: Codable {
    let features: [StationFeature]
}

struct StationFeature: Codable {
    let properties: StationProperties
}

struct StationProperties: Codable {
    let stationIdentifier: String
}

