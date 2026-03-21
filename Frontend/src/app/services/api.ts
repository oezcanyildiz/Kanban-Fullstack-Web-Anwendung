export const API_BASE_URL = ''; // Wir nutzen den Vite Proxy, daher ist die BaseURL leer (relative Pfade)

/**
 * Hilfsfunktion, um das gespeicherte JWT-Token abzurufen.
 */
export function getAuthToken(): string | null {
  return localStorage.getItem('jwtToken');
}

/**
 * Hilfsfunktion, um asynchrone Requests mit fetch auszuführen.
 * Fügt automatisch den Authorization-Header (Bearer Token) hinzu, falls vorhanden.
 */
export async function fetchApi(endpoint: string, options: RequestInit = {}) {
  const token = getAuthToken();
  
  const headers = new Headers(options.headers || {});
  headers.set('Content-Type', 'application/json');
  
  if (token) {
    headers.set('Authorization', `Bearer ${token}`);
  }

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });

  if (!response.ok) {
    const errorText = await response.text();
    let errorMessage = `HTTP error! status: ${response.status}`;
    try {
      const errorData = JSON.parse(errorText);
      if (errorData?.message) errorMessage = errorData.message;
    } catch (e) {}
    throw new Error(errorMessage);
  }

  const text = await response.text();
  return text ? JSON.parse(text) : null;
}
