// export async function load() {
//     if (typeof window === 'undefined') return;
//
//     const script = document.createElement('script');
//     script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
//     script.async = true;
//     document.head.appendChild(script);
//
//     await new Promise((resolve, reject) => {
//         script.onload = resolve;
//         script.onerror = reject;
//     });
//
//     return {
//         props: {}
//     };
// }