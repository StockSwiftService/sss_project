
export async function load ({fetch, params}) {

    const backendUrl = import.meta.env.VITE_BACKEND_URL;
    const searchKeyword = params.kw || '';
    const currentPage = params.page || 0;
    const whetherVal = params.whether || false;

    let res = await fetch(`${backendUrl}/api/v1/purchase?kw=${searchKeyword}&page=${currentPage}&whether=${whetherVal}`, {
        credentials: 'include'
    })
    let result = await res.json();

    return result;
}