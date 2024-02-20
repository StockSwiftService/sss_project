// src/hooks.js

export function fetchInterceptor(fetch) {
    return async function customFetch(input, init) {
        try {
            const response = await fetch(input,{...init,credentials: 'include'});
            if (!response.ok && response.status === 401) {
                alert("로그인 후 이용해 주세요.");
                // deleteAccessToken();
                // deleteRefreshToken();
                // window.location.href = 'http://localhost:5173/';
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


    function deleteCookie(name) {
    document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/';
     }

    function deleteAccessToken() {
    deleteCookie('access_token');
      }

     function deleteRefreshToken() {
    deleteCookie('refresh_token');
     }