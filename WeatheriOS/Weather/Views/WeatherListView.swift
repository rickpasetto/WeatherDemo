//
//  WeatherListView.swift
//  Weather
//
//  Created by Rick Pasetto on 12/21/25.
//

import SwiftUI

struct WeatherListView: View {
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
                SearchLoadingOverlay(isSearching: viewModel.isSearching)
            }
            .task {
                await viewModel.loadWeather()
            }
        }
    }
}

#Preview {
    WeatherListView()
}

