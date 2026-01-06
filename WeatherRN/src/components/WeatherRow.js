import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { getWeatherEmoji } from '../utils/weatherEmoji';

export function WeatherRow({ weather }) {
  const { city, currentTemperature, currentCondition, currentError } = weather;
  const emoji = getWeatherEmoji(currentCondition);
  const tempDisplay = currentTemperature != null ? `${currentTemperature}°F` : '--';
  const cityDisplay = city.state ? `${city.name}, ${city.state}` : city.name;
  const conditionDisplay = currentCondition || currentError || 'Loading...';
  const hasError = !currentCondition && currentError;

  return (
    <View style={styles.container}>
      <Text style={styles.emoji}>{emoji}</Text>
      <View style={styles.info}>
        <Text style={styles.city}>{cityDisplay}</Text>
        <Text style={[styles.condition, hasError && styles.errorCondition]}>
          {conditionDisplay}
        </Text>
      </View>
      <Text style={styles.temp}>{tempDisplay}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 14,
    paddingHorizontal: 16,
    backgroundColor: '#1e1e2e',
    borderRadius: 12,
    marginHorizontal: 16,
    marginVertical: 6,
  },
  emoji: {
    fontSize: 32,
    marginRight: 14,
  },
  info: {
    flex: 1,
  },
  city: {
    fontSize: 17,
    fontWeight: '600',
    color: '#f0f0f5',
  },
  condition: {
    fontSize: 14,
    color: '#a0a0b0',
    marginTop: 2,
  },
  errorCondition: {
    color: '#f87171',
    fontSize: 12,
  },
  temp: {
    fontSize: 24,
    fontWeight: '700',
    color: '#60a5fa',
  },
});


