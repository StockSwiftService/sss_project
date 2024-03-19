export async function load({fetch}) {
    const backendUrl = import.meta.env.VITE_BACKEND_URL;

    let res = await fetch(`${backendUrl}/api/v1/stocks`)
    let result = await res.json();

    return result;
}