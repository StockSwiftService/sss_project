export async function load({fetch}) {
    let res = await fetch('http://localhost:8080/api/v1/stocks')
    let result = await res.json();

    return result;
}