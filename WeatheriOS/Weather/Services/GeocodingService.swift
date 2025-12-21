//
//  GeocodingService.swift
//  Weather
//
//  Created by Rick Pasetto on 12/21/25.
//

import Foundation
import CoreLocation
import Contacts

class GeocodingService {
    private let geocoder = CLGeocoder()
    
    func geocode(cityName: String) async throws -> City {
        let placemarks = try await geocoder.geocodeAddressString(cityName)
        
        guard let placemark = placemarks.first,
              let location = placemark.location else {
            throw GeocodingError.locationNotFound
        }
        
        let name = placemark.locality ?? placemark.administrativeArea ?? cityName
        let state = extractStateAbbreviation(from: placemark)
        
        return City(
            name: name,
            state: state,
            latitude: location.coordinate.latitude,
            longitude: location.coordinate.longitude
        )
    }
    
    private func extractStateAbbreviation(from placemark: CLPlacemark) -> String? {
        if let postalAddress = placemark.postalAddress {
            return postalAddress.state
        }
        
        if let administrativeArea = placemark.administrativeArea {
            return StateAbbreviationMapper.abbreviation(for: administrativeArea)
        }
        
        return nil
    }
}

struct StateAbbreviationMapper {
    private static let stateMap: [String: String] = [
        "Alabama": "AL", "Alaska": "AK", "Arizona": "AZ", "Arkansas": "AR",
        "California": "CA", "Colorado": "CO", "Connecticut": "CT", "Delaware": "DE",
        "Florida": "FL", "Georgia": "GA", "Hawaii": "HI", "Idaho": "ID",
        "Illinois": "IL", "Indiana": "IN", "Iowa": "IA", "Kansas": "KS",
        "Kentucky": "KY", "Louisiana": "LA", "Maine": "ME", "Maryland": "MD",
        "Massachusetts": "MA", "Michigan": "MI", "Minnesota": "MN", "Mississippi": "MS",
        "Missouri": "MO", "Montana": "MT", "Nebraska": "NE", "Nevada": "NV",
        "New Hampshire": "NH", "New Jersey": "NJ", "New Mexico": "NM", "New York": "NY",
        "North Carolina": "NC", "North Dakota": "ND", "Ohio": "OH", "Oklahoma": "OK",
        "Oregon": "OR", "Pennsylvania": "PA", "Rhode Island": "RI", "South Carolina": "SC",
        "South Dakota": "SD", "Tennessee": "TN", "Texas": "TX", "Utah": "UT",
        "Vermont": "VT", "Virginia": "VA", "Washington": "WA", "West Virginia": "WV",
        "Wisconsin": "WI", "Wyoming": "WY", "District of Columbia": "DC"
    ]
    
    static func abbreviation(for stateName: String) -> String? {
        return stateMap[stateName] ?? stateName
    }
}

enum GeocodingError: LocalizedError {
    case locationNotFound
    
    var errorDescription: String? {
        switch self {
        case .locationNotFound:
            return "Could not find location for the city name"
        }
    }
}

