export function getWeatherEmoji(condition) {
  if (!condition) return '🌤️';
  
  const lower = condition.toLowerCase();
  
  if (lower.includes('sunny') || lower.includes('clear')) return '☀️';
  if (lower.includes('partly cloudy')) return '⛅';
  if (lower.includes('cloudy') || lower.includes('overcast')) return '☁️';
  if (lower.includes('rain') || lower.includes('shower')) return '🌧️';
  if (lower.includes('thunderstorm') || lower.includes('storm')) return '⛈️';
  if (lower.includes('snow') || lower.includes('sleet')) return '❄️';
  if (lower.includes('fog') || lower.includes('mist') || lower.includes('haze')) return '🌫️';
  if (lower.includes('wind')) return '💨';
  
  return '🌤️';
}


