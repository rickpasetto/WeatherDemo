//
//  SearchLoadingOverlay.swift
//  Weather
//
//  Created by Rick Pasetto on 12/21/25.
//

import SwiftUI

struct SearchLoadingOverlay: View {
    let isSearching: Bool
    
    var body: some View {
        if isSearching {
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
}

