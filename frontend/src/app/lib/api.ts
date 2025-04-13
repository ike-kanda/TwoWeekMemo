export const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

// スラッシュが重なっても安全な連結関数
function joinUrl(base: string, path: string): string {
  return `${base.replace(/\/$/, '')}/${path.replace(/^\/+/, '')}`;
}

export async function fetchWithAuth(path: string, options: RequestInit = {}) {
  const token = typeof window !== 'undefined' ? localStorage.getItem('token') : null;

  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...options.headers,
  };

  const url = joinUrl(API_URL, path);

  console.log('✅ fetch URL:', url);
  console.log('✅ fetch TOKEN:', token);

  return fetch(url, { ...options, headers });
}
