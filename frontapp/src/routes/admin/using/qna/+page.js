export async function load ({fetch, params}) {
    const currentPage = params.page || 0;

    let res = await  fetch(`http://localhost:8080/api/v1/questions/admin?page=${currentPage}`, {
        credentials: 'include'
    })
    let result = await res.json();

    return result;
}