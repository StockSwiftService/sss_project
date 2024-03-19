export async function load ({fetch, params}) {
    const currentPage = params.page || 0;
    const backendUrl = import.meta.env.VITE_BACKEND_URL;

    let res = await  fetch(`${backendUrl}/api/v1/questions/admin?page=${currentPage}`, {
        credentials: 'include'
    })
    let result = await res.json();

    return result;
}