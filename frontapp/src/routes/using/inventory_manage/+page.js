export async function load ({fetch, params}) {

    const backendUrl = import.meta.env.VITE_BACKEND_URL;
    const searchKeyword = params.kw || '';
    const currentPage = params.page || 0;

    let res = await fetch(`${backendUrl}/api/v1/stocks?kw=${searchKeyword}&page=${currentPage}`, {
        credentials: 'include'
    })
    let result = await res.json();

    return result;
}