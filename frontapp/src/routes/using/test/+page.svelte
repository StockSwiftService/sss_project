<script>
    let items = [
        {
            id: 1,
            ainput: "",
            binput: "",
        }
    ];

    function addRow() {
        const newItem = {
            id: items.length + 1,
            ainput: "",
            binput: "",
        };
        items = [...items, newItem];
    }

    function updateBInput(index, value) {
        items[index].binput = value;
    }
</script>

<table>
    <tbody>
        {#each items as item (item.id)}
        <tr>
            <td id="v{item.id}">
                {item.id}
            </td>
            <td>
                <div class="input-type-1">
                    <input type="text" placeholder="내용1" bind:value={item.ainput}>
                </div>
            </td>
            <td>
                <button class="w50 h30 btn-type-1 bdm bdr4 f12 cm" on:click={() => updateBInput(item.id - 1, item.ainput)}>입력</button>
            </td>
            <td>
                <div class="input-type-1">
                    <input type="text" placeholder="내용2" bind:value={item.binput}>
                </div>
            </td>
        </tr>
        {/each}
    </tbody>
</table>

<button class="w50 h30 btn-type-1 bm bdr4 f12 cfff mt40" on:click={addRow}>추가</button>
