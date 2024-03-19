<script>
    export let data;
    const backendUrl = import.meta.env.VITE_BACKEND_URL;
    async function downloadExcel() {
        try {
            const response = await fetch(`${backendUrl}/api/v1/stocks/excel`);
            const blob = await response.blob();
            const a = document.createElement('a');
            a.href = window.URL.createObjectURL(blob);
            a.download = 'stocks.xlsx';
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
        } catch (error) {
            console.error('Error downloading Excel file:', error);
        }
    }
</script>
<style>
    .t1 {
        border-collapse: collapse;
    }
    .t1 td,
    .t1 th {
        border: 1px solid red;
        padding: 10px;
    }
</style>
<table class="t1">
    <thead>
    <tr>
        <th>번호</th>
        <th>품목코드</th>
        <th>품목명</th>
        <th>거래일자</th>
        <th>거래처명</th>
        <th>수량</th>
        <th>단가</th>
        <th>금액합계</th>
    </tr>
    </thead>
    <tbody>
    {#each data.data.stocks as stock}
        <tr>
            <th>{stock.id}</th>
            <td>{stock.itemCode}</td>
            <td>{stock.itemName}</td>
            <td>{stock.transactionDate}</td>
            <td>{stock.clientName}</td>
            <td>{stock.quantity}</td>
            <td>{stock.unitPrice}</td>
            <td>{stock.totalAmount}</td>
        </tr>
    {/each}
    </tbody>
</table>
<button on:click={downloadExcel}>Excel</button>