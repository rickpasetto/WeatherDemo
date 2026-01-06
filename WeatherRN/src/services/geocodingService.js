// Using Nominatim (OpenStreetMap) for geocoding - free, no API key required
const NOMINATIM_URL = 'https://nominatim.openstreetmap.org/search';

export async function geocodeCity(cityName) {
  const params = new URLSearchParams({
    q: `${cityName}, USA`,
    format: 'json',
    addressdetails: '1',
    limit: '1',
  });

  const response = await fetch(`${NOMINATIM_URL}?${params}`, {
    headers: { 'User-Agent': 'WeatherApp (ReactNative)' },
  });

  if (!response.ok) {
    throw new Error('Geocoding request failed');
  }

  const results = await response.json();
  
  if (!results.length) {
    throw new Error('City not found');
  }

  const result = results[0];
  const address = result.address || {};
  
  return {
    id: `city-${Date.now()}`,
    name: address.city || address.town || address.village || cityName,
    state: address.state ? getStateAbbreviation(address.state) : null,
    latitude: parseFloat(result.lat),
    longitude: parseFloat(result.lon),
  };
}

const STATE_MAP = {
  'Alabama': 'AL', 'Alaska': 'AK', 'Arizona': 'AZ', 'Arkansas': 'AR',
  'California': 'CA', 'Colorado': 'CO', 'Connecticut': 'CT', 'Delaware': 'DE',
  'Florida': 'FL', 'Georgia': 'GA', 'Hawaii': 'HI', 'Idaho': 'ID',
  'Illinois': 'IL', 'Indiana': 'IN', 'Iowa': 'IA', 'Kansas': 'KS',
  'Kentucky': 'KY', 'Louisiana': 'LA', 'Maine': 'ME', 'Maryland': 'MD',
  'Massachusetts': 'MA', 'Michigan': 'MI', 'Minnesota': 'MN', 'Mississippi': 'MS',
  'Missouri': 'MO', 'Montana': 'MT', 'Nebraska': 'NE', 'Nevada': 'NV',
  'New Hampshire': 'NH', 'New Jersey': 'NJ', 'New Mexico': 'NM', 'New York': 'NY',
  'North Carolina': 'NC', 'North Dakota': 'ND', 'Ohio': 'OH', 'Oklahoma': 'OK',
  'Oregon': 'OR', 'Pennsylvania': 'PA', 'Rhode Island': 'RI', 'South Carolina': 'SC',
  'South Dakota': 'SD', 'Tennessee': 'TN', 'Texas': 'TX', 'Utah': 'UT',
  'Vermont': 'VT', 'Virginia': 'VA', 'Washington': 'WA', 'West Virginia': 'WV',
  'Wisconsin': 'WI', 'Wyoming': 'WY', 'District of Columbia': 'DC',
};

function getStateAbbreviation(stateName) {
  return STATE_MAP[stateName] || stateName;
}


