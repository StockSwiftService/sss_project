
export async function load ({fetch, params}) {


    const searchKeyword = params.kw || '';
    const currentPage = params.page || 0;

    let res = await fetch(`http://localhost:8080/api/v1/clients?kw=${searchKeyword}&page=${currentPage}`, {
        credentials: 'include'
    })
    let result = await res.json();

    return result;
}