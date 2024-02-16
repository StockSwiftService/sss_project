// src/hooks.js

export function fetchInterceptor(fetch) {
    return async function customFetch(input, init) {
      try {
        const response = await fetch(input, init);
        if (!response.ok && response.status === 401) {
            alert("로그인 후 이용해 주세요.");
            deleteAllCookies();
          window.location.href = 'http://localhost:5173/';
        }
        return response;
      } catch (error) {
        console.error('Fetch error:', error);
        throw error;
      }
    };
  }

export function useFetch() {
  const fetch = window.fetch;
  window.fetch = fetchInterceptor(fetch);
}


function deleteAllCookies() {
    const cookies = document.cookie.split(';');

    for (let i = 0; i < cookies.length; i++) {
      const cookie = cookies[i];
      const eqPos = cookie.indexOf('=');
      const name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
      document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/';
    }
  }