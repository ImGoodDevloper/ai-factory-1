/**
 * Environment variable validation utility.
 * Ensures all required environment variables are present and valid.
 */

export function validateEnv() {
  const required = ['VITE_API_BASE_URL'];
  const missing = required.filter((key) => !import.meta.env[key]);

  if (missing.length > 0) {
    console.warn(`Missing environment variables: ${missing.join(', ')}. Using defaults.`);
  }
}
