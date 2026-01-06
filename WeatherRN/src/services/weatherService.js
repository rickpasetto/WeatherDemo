const BASE_URL = 'https://api.weather.gov';

const headers = {
  'User-Agent': 'WeatherApp (ReactNative)',
};

async function fetchGridPoints(latitude, longitude) {
  const response = await fetch(
    `${BASE_URL}/points/${latitude},${longitude}`,
    { headers }
  );
  if (!response.ok) throw new Error('Failed to fetch grid points');
  return response.json();
}

async function fetchForecast(forecastUrl) {
  const response = await fetch(forecastUrl, { headers });
  if (!response.ok) throw new Error('Failed to fetch forecast');
  return response.json();
}

async function fetchCurrentObservation(gridId, gridX, gridY) {
  try {
    const stationsUrl = `${BASE_URL}/gridpoints/${gridId}/${gridX},${gridY}/stations`;
    console.log(`[DEBUG] Fetching stations: ${stationsUrl}`);
    
    const stationsResponse = await fetch(stationsUrl, { headers });
    if (!stationsResponse.ok) {
      console.warn(`[DEBUG] Stations request failed: ${stationsResponse.status}`);
      return { error: `Stations API error: ${stationsResponse.status}` };
    }
    
    const stations = await stationsResponse.json();
    const stationId = stations.features?.[0]?.properties?.stationIdentifier;
    if (!stationId) {
      console.warn(`[DEBUG] No station found for ${gridId}/${gridX},${gridY}`);
      return { error: 'No weather station found' };
    }
    console.log(`[DEBUG] Found station: ${stationId}`);

    const obsUrl = `${BASE_URL}/stations/${stationId}/observations/latest`;
    const obsResponse = await fetch(obsUrl, { headers });
    if (!obsResponse.ok) {
      console.warn(`[DEBUG] Observation request failed: ${obsResponse.status}`);
      return { error: `Observation API error: ${obsResponse.status}` };
    }

    const observation = await obsResponse.json();
    const tempCelsius = observation.properties?.temperature?.value;
    const condition = observation.properties?.textDescription || null;
    
    console.log(`[DEBUG] Station ${stationId}: temp=${tempCelsius}, condition=${condition}`);
    
    // If station returns null data, it's not reporting
    if (tempCelsius == null && !condition) {
      return { error: `Station ${stationId} not reporting` };
    }
    
    return {
      temperature: tempCelsius != null ? Math.round(tempCelsius * 9/5 + 32) : null,
      condition,
    };
  } catch (e) {
    console.error(`[DEBUG] fetchCurrentObservation error:`, e.message);
    return { error: e.message };
  }
}

export async function fetchWeather(city) {
  const points = await fetchGridPoints(city.latitude, city.longitude);
  const { gridId, gridX, gridY, forecast: forecastUrl } = points.properties;
  
  const [forecastData, current] = await Promise.all([
    fetchForecast(forecastUrl),
    fetchCurrentObservation(gridId, gridX, gridY),
  ]);

  return {
    id: city.id,
    city,
    currentTemperature: current?.temperature ?? null,
    currentCondition: current?.condition ?? null,
    currentError: current?.error ?? null,
    forecast: forecastData.properties.periods.slice(0, 5).map((p, i) => ({
      id: `${city.id}-${i}`,
      name: p.name,
      temperature: p.temperature,
      temperatureUnit: p.temperatureUnit,
      shortForecast: p.shortForecast,
    })),
  };
}


