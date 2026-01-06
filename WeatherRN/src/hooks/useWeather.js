import { useState, useEffect, useCallback } from 'react';
import { fetchWeather } from '../services/weatherService';
import { geocodeCity } from '../services/geocodingService';

const DEFAULT_CITIES = [
  { id: '1', name: 'New York', state: 'NY', latitude: 40.7128, longitude: -74.0060 },
  { id: '2', name: 'Los Angeles', state: 'CA', latitude: 34.0522, longitude: -118.2437 },
  { id: '3', name: 'Chicago', state: 'IL', latitude: 41.8781, longitude: -87.6298 },
  { id: '4', name: 'Houston', state: 'TX', latitude: 29.7604, longitude: -95.3698 },
  { id: '5', name: 'Phoenix', state: 'AZ', latitude: 33.4484, longitude: -112.0740 },
  { id: '6', name: 'Philadelphia', state: 'PA', latitude: 39.9526, longitude: -75.1652 },
  { id: '7', name: 'San Antonio', state: 'TX', latitude: 29.4241, longitude: -98.4936 },
  { id: '8', name: 'San Diego', state: 'CA', latitude: 32.7157, longitude: -117.1611 },
  { id: '9', name: 'Dallas', state: 'TX', latitude: 32.7767, longitude: -96.7970 },
  { id: '10', name: 'San Jose', state: 'CA', latitude: 37.3382, longitude: -121.8863 },
];

export function useWeather() {
  const [weatherData, setWeatherData] = useState([]);
  const [cities, setCities] = useState(DEFAULT_CITIES);
  const [isLoading, setIsLoading] = useState(true);
  const [isSearching, setIsSearching] = useState(false);
  const [error, setError] = useState(null);

  const loadWeather = useCallback(async () => {
    setIsLoading(true);
    setError(null);

    const results = await Promise.all(
      cities.map(async (city) => {
        try {
          return await fetchWeather(city);
        } catch (e) {
          console.warn(`Failed to load weather for ${city.name}:`, e);
          return null;
        }
      })
    );

    setWeatherData(results.filter(Boolean));
    setIsLoading(false);
  }, [cities]);

  useEffect(() => {
    loadWeather();
  }, []);

  const addCity = useCallback(async (cityName) => {
    setIsSearching(true);
    setError(null);

    try {
      const city = await geocodeCity(cityName);
      
      if (cities.some(c => c.name.toLowerCase() === city.name.toLowerCase())) {
        setError('City already in list');
        setIsSearching(false);
        return;
      }

      setCities(prev => [...prev, city]);
      
      const weather = await fetchWeather(city);
      setWeatherData(prev => [...prev, weather]);
    } catch (e) {
      setError(e.message || 'Failed to add city');
    } finally {
      setIsSearching(false);
    }
  }, [cities]);

  return { weatherData, isLoading, isSearching, error, addCity, refresh: loadWeather };
}


