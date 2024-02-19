
export async function load ({fetch, params}) {
    const searchKeyword = params.searchKeyword || '';
    const currentPage = params.currentPage || 0;

    // const query = req.querystring;

    // console.log("fetch: ", fetch);
    // console.log("params: ", params);
    // console.log("searchParams: ", params);
    //querystring으로 어떻게 해야함?
    let res = await  fetch(`http://localhost:8080/api/v1/clients?kw=${searchKeyword}&page=${currentPage}`, {
        credentials: 'include'
    })
    let result = await res.json();
    let data;
    data = {
        result,
        searchKeyword,
        currentPage,
    };
    return data;
}