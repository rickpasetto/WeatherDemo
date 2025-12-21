//
//  City.swift
//  Weather
//
//  Created by Rick Pasetto on 12/21/25.
//

import Foundation

struct City: Identifiable, Codable {
    let id = UUID()
    let name: String
    let latitude: Double
    let longitude: Double
}

