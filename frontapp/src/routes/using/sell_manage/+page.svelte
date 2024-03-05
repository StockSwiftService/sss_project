<script>    
    import {page} from "$app/stores";
    import {goto, replaceState} from "$app/navigation";
    import { onMount } from 'svelte';

    export let data;

    let isActive = false;
    let isActive2 = false;

    let isActiveAdd = false;
    let isActiveModifi = false;
    let isActiveAccountSearch = false;
    let isActiveStockSearch = false;

    function activateModalAdd() {
        isActive = true;
        isActiveAdd = true;
    }

    function activateModalModifi() {
        isActive = true;
        isActiveModifi = true;
    }

    function selectClient(client) {
        selectedClient = client;

        isActive2 = false;
        isActiveAccountSearch = false;
    }

    function deactivateModal() {
        isActive = false;
        isActiveAdd = false;
        isActiveModifi = false;
    }

    function deactivateAccountSearchModal() {
        isActive2 = false;
        isActiveAccountSearch = false;
        isActiveStockSearch = false;
    }

    function windowRefresh() {
        window.location.href = 'http://localhost:5173/using/sell_manage';
    }

    //오늘 날짜로 기본 데이터 생성
    function getTodayDate() {
        const now = new Date();
        const year = now.getFullYear();
        let month = (now.getMonth() + 1).toString().padStart(2, '0');
        let day = now.getDate().toString().padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

    let purchaseDate;
    let deliveryStatus = false;
    let significant = "";

    // 숫자 세자리 포멧팅
    function formatNumber(value) {
        return value.toLocaleString('ko-KR');
    }

    //거래처명 입력 후 검색
    let clients = [];
    let selectedClient = { clientName: '', phoneNumber: '', address: '' };
    let clientSerachInput ="";

    async function fetchClients(keyword = '') {
        const response = await fetch(`http://localhost:8080/api/v1/clients/search?clientName=${keyword}`);
        if (response.ok) {
            clients = await response.json();
        } else {
            console.error('거래처 목록을 불러오는데 실패했습니다.');
        }
    }

    async function searchClients() {
        isActive2 = true;
        isActiveAccountSearch = true;
        await fetchClients(clientSerachInput);
    }

    function searchClientNameEnter(event) {
        if (event.key === 'Enter') {
            fetchClients(clientSerachInput);
        }
    }

    function searchClientNameClick() {
        fetchClients(clientSerachInput);
    }

    //재고 영역 품목명 입력 후 검색
    let items = [
        {
            id: 1,
            itemName: "",
            inputQuantity: 0,
            salesPrice: 0,
            sumPrice: 0,
            selected: false
        }
    ];
    let selectItem;
    let stocks = [];
    let itemSerachInput = "";
    let itemAllSelected = false;
    let allPrice = 0;
    let formatAllPrice = 0;

    function addRow() {
        const newItem = {
            id: items.length + 1,
            itemName: "",
            inputQuantity: 0,
            salesPrice: 0,
            sumPrice: 0,
            selected: false,
        };
        items = [...items, newItem];
    }

    async function itemNameKeyUp(event, item) {
        if (event.key === 'Enter') {
            selectItem = item;
            const response = await fetch(`http://localhost:8080/api/v1/stocks/search?itemName=${item.itemName}`);
            if (response.ok) {
                const responseData = await response.json();
                stocks = responseData.data.stocks;
                if (stocks.length == 1) {
                    item.itemName = stocks[0].itemName;
                    item.inputQuantity = 1;
                    item.salesPrice = stocks[0].salesPrice;
                    item.sumPrice = item.inputQuantity * item.salesPrice;
                    items = items;
                    allPricecalc();
                }
                else {
                    isActive2 = true;
                    isActiveStockSearch = true;
                    itemSerachInput = item.itemName;
                }
            } else {
                console.error('서버로부터 데이터를 받아오는 데 실패했습니다.');
            }
        }
    }

    async function searchItemNameKeyUp(item) {
        const response = await fetch(`http://localhost:8080/api/v1/stocks/search?itemName=${itemSerachInput}`);
        if (response.ok) {
            const responseData = await response.json();
            stocks = responseData.data.stocks;
            if (stocks.length == 1) {
                item.itemName = stocks[0].itemName;
                item.inputQuantity = 1;
                item.salesPrice = stocks[0].salesPrice;
                item.sumPrice = item.inputQuantity * item.salesPrice;
                items = items;
                isActive2 = false;
                isActiveStockSearch = false;
                allPricecalc();
            }
        }
        else {
            console.error('서버로부터 데이터를 받아오는 데 실패했습니다.');
        }
    }

    function searchItemNameEnter(event, item) { 
        if (event.key === 'Enter') {
            searchItemNameKeyUp(item);
        }
    }

    function searchItemNameClick(item) {
        searchItemNameKeyUp(item);
    }

    function quantityChange(item) {
        if (item.inputQuantity <= 0) item.inputQuantity = 1;
        item.sumPrice = item.inputQuantity * item.salesPrice;
        items = items;
        allPricecalc();
    }

    function allPricecalc() {
        allPrice = 0;
        for (let i = 0; i < items.length; i++) {
            allPrice += items[i].sumPrice;
        }
        formatAllPrice = formatNumber(allPrice);
    }

    function selectStock(stock) {
        selectItem.itemName = stock.itemName;
        selectItem.inputQuantity = 1;
        selectItem.salesPrice = stock.salesPrice;
        selectItem.sumPrice = selectItem.inputQuantity * selectItem.salesPrice;
        items = items;
        isActive2 = false;
        isActiveStockSearch = false;
        allPricecalc();
    }

    function toggleAllSelection() {
        if (!itemAllSelected) {
            itemAllSelected = true;
            for(const item of items) {
                item.selected = true;
            }
        }
        else if (itemAllSelected) {
            itemAllSelected = false;
            for(const item of items) {
                item.selected = false;
            }
        }
        items = items;
    }

    function toggleSelection(item) {
        if (!item.selected) item.selected = true;
        else if (item.selected) item.selected = false;
        itemAllSelected = false;
    }

    function deleteSelectedItems() {
        items = items.filter(item => !item.selected);
        itemAllSelected = false;
        allPricecalc();
    }

    function purchaseCreate() {
        if (selectedClient.clientName == "") {
            alert("거래처를 선택해 주세요.");
            return true;
        }
        if (formatAllPrice == 0) {
            alert("최소 1개 이상 품목을 등록해 주세요.");
            return true;
        }
    }

    const submitSignupForm = async () => {
        if(purchaseCreate()) {
            return;
        }
        
        try {
            const filteredItems = items.map(item => ({
                itemName: item.itemName,
                inputQuantity: item.inputQuantity
            }));

            const data = {
                purchaseDate: purchaseDate,
                selectedClient: selectedClient,
                deliveryStatus: deliveryStatus,
                significant: significant,
                filteredItems: filteredItems,
                allPrice: allPrice
            };

            const response = await fetch('http://localhost:8080/api/v1/purchase/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                alert("판매 등록이 완료되었습니다.");
                windowRefresh();
            } else {
                console.log("전표생성 실패");
            }
        } catch (error) {
            console.error('Error submitting form:', error);
        }
    }  

    // 전체 선택
    let allChecked = false;

    function toggleAll() {

        data.data.purchases.content.map(purchase => {
            if (!purchase.checked) {
                purchase.checked = true;
            } else {
                purchase.checked = false;
            }
        })

        data = data;
    }

    // 승인 미승인
    let isUnapprovedActive = true;
    let isApprovedActive = false;

    function setActiveButtons(unapprovedActive) {
        isUnapprovedActive = unapprovedActive;
        isApprovedActive = !unapprovedActive;
    }

    const unApprovalPurchase = async (unapprovedActive) => {
        setActiveButtons(unapprovedActive);
        whetherVal = false;
        notApprovedAndApproved();
    }

    const approvalPurchase = async (unapprovedActive) => {
        setActiveButtons(unapprovedActive);
        whetherVal = true;
        notApprovedAndApproved();
    }

    // 승인 처리
    async function approvePurchases() {
        const selectedIds = data.data.purchases.content
            .filter(purchase => purchase.checked)
            .map(purchase => purchase.id);

        if (selectedIds.length === 0) {
            alert('승인 처리할 전표를 선택해주세요.');
            return;
        }

        const isConfirmed = confirm('해당 전표를 승인 처리 하시겠습니까?');

        if (!isConfirmed) {
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/v1/purchase/approvalRequest', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    ids: Array.from(selectedIds)
                }),
            });

            if (response.ok) {
                alert('승인 처리가 완료되었습니다.');
                windowRefresh();
            } else {
                alert('승인 처리 중 오류가 발생했습니다.');
            }
        } catch (error) {
            console.error('Error deleting clients:', error);
            alert('승인 처리 중 오류가 발생했습니다.');
        }
    }

    // 승인 취소 처리
    async function approvePurchasesCancel() {

        const selectedIds = data.data.purchases.content
            .filter(purchase => purchase.checked)
            .map(purchase => purchase.id);

        if (selectedIds.length === 0) {
            alert('승인 처리 취소할 전표를 선택해주세요.');
            return;
        }

        const isConfirmed = confirm('해당 전표를 승인 취소 처리 하시겠습니까?');

        if (!isConfirmed) {
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/v1/purchase/approvalCancelRequest', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    ids: Array.from(selectedIds)
                }),
            });

            if (response.ok) {
                alert('승인 처리 취소가 완료되었습니다.');
                windowRefresh();
            } else {
                alert('승인 처리 취소 중 오류가 발생했습니다.');
            }
        } catch (error) {
            console.error('Error deleting clients:', error);
            alert('승인 처리 취소 중 오류가 발생했습니다.');
        }
    }

    // 삭제 처리
    async function PurchasesDelete() {

        const selectedIds = data.data.purchases.content
            .filter(purchase => purchase.checked)
            .map(purchase => purchase.id);

        if (selectedIds.length === 0) {
            alert('삭제 할 전표를 선택해주세요.');
            return;
        }

        const isConfirmed = confirm('해당 전표를 삭제 하시겠습니까?');

        if (!isConfirmed) {
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/v1/purchase/delete', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    ids: Array.from(selectedIds)
                }),
            });

            if (response.ok) {
                alert('삭제가 완료되었습니다.');
                windowRefresh();
            } else {
                alert('삭제 중 오류가 발생했습니다.');
            }
        } catch (error) {
            console.error('Error deleting clients:', error);
            alert('삭제 중 오류가 발생했습니다.');
        }
    }

    //판매 게시글 출력
    
    onMount(async () => {

        //오늘 날짜로 기본 데이터 생성
        document.getElementById('searchDateInput1').value = getTodayDate();
        document.getElementById('searchDateInput2').value = getTodayDate();
        purchaseDate = getTodayDate();

        await dataLoad();
        const unsubscribe = page.subscribe(async ($page) => {
            // URL에서 검색어(kw) 쿼리 파라미터 값을 가져와 searchQuery에 할당
            searchQuery = $page.url.searchParams.get('kw') || '';
            await dataLoad();
        });

        // 컴포넌트가 언마운트될 때 구독 해제
        return () => {
            unsubscribe();
        };

    });

    //페이징
    function generatePageButtons(totalPages) {
        const buttons = [];
        for (let i = 0; i < totalPages; i++) {
            buttons.push(i + 1);
        }
        return buttons;
    }

    let searchQuery = '';
    let currentPage = 0;
    let whetherVal = false;

    async function changePage(searchQuery, currentPage) {
        try {

            $page.url.searchParams.get('kw', searchQuery);
            $page.url.searchParams.set('page', currentPage);
            $page.url.searchParams.get('whether', whetherVal);

            await goto(`?${$page.url.searchParams.toString()}`, {replaceState});
            await dataLoad();

        } catch (error) {
            console.error('Error fetching data:', error);
        }
    }

    //검색
    const performSearch = async () => {

        $page.url.searchParams.set('kw', searchQuery);
        $page.url.searchParams.set('page', currentPage);
        $page.url.searchParams.get('whether', whetherVal);

        await goto(`?${$page.url.searchParams.toString()}`, {replaceState});

        await dataLoad();

    }

    //미승인 승인
    const notApprovedAndApproved = async () => {

        $page.url.searchParams.get('kw', searchQuery);
        $page.url.searchParams.set('page', currentPage);
        $page.url.searchParams.set('whether', whetherVal);

        await goto(`?${$page.url.searchParams.toString()}`, {replaceState});

        await dataLoad();

    }

    function handleKeyPress(event) {
        if (event.key === 'Enter') {
            performSearch();
        }
    }

    async function dataLoad() {
        const queryString = window.location.search;

        const res = await fetch(`http://localhost:8080/api/v1/purchase${queryString}`, {
            credentials: 'include'
        })
        data = await res.json();

    }

</script>

<style>
    .approval-btn-box > button {
        font-size: 12px;
        box-sizing: border-box;
        padding: 8px 12px;
        border-radius: 999px;
        background: #eee;
        color: #777;
    }
    .approval-btn-box > button.active {
        font-size: 12px;
        box-sizing: border-box;
        padding: 8px 12px;
        border-radius: 999px;
        background: #2656F6;
        color: #fff;
    }

    .situation-btn-box > button {
        display: none;
    }
    .situation-btn-box > button.active {
        display: flex;
    }
</style>

<div class="modal-area-1 modal-area wh100per fixed zi9" class:active="{isActive}">

    <!-- 판매 등록 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w800" class:active="{isActiveAdd}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">판매 등록</h3>
            <button class="x-btn img-box abs" on:click="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <form>
                <div class="chit-box flex fdc g12">
                    <ul class="w100per flex aic g20">
                        <li class="flex aic g12">
                            <span class="title-text f14 c333">일자</span>
                            <div class="input-box input-type-2 f14 w140">
                                <input type="date" placeholder="일자" bind:value={purchaseDate}>
                            </div>
                        </li>
                        <li class="flex aic g12">
                            <span class="title-text f14 c333">거래처</span>
                            <div class="input-box flex aic g8 w200">
                                <div class="input-type-2 f14 w100per">
                                    <input type="text" placeholder="거래처명" readonly value={selectedClient.clientName}>
                                </div>
                                <button class="btn-type-1 w60 h36 f14 bdr4 b333 cfff" type="button" style="min-width: 60px;" on:click={searchClients}>찾기</button>
                            </div>
                        </li>
                        <li class="flex aic g12">
                            <span class="title-text f14 c333">연락처</span>
                            <div class="input-box input-type-2 f14">
                                <input type="text" placeholder="연락처" readonly disabled value={selectedClient.phoneNumber}>
                            </div>
                        </li>
                    </ul>
                    <ul class="w100per flex aic g20">
                        <li class="flex aic g12" style="width:67%">
                            <span class="title-text f14 c333">주소</span>
                            <div class="input-box input-type-2 f14 w100per">
                                <input type="text" placeholder="주소" readonly disabled value={selectedClient.address}>
                            </div>
                        </li>
                        <li class="flex aic g12" style="width:calc(33% - 20px)">
                            <span class="title-text f14 c333">출고 여부</span>
                            <div class="check-type-1">
                                <input type="checkbox" id="w1" bind:checked={deliveryStatus}>
                                <label for="w1"></label>
                            </div>
                        </li>
                    </ul>
                    <div class="w100per">
                        <div class="flex aic g12">
                            <span class="title-text f14 c333">특이사항</span>
                            <div class="input-box textarea-type-1 f14 w100per h60">
                                <textarea placeholder="특이사항" bind:value={significant}></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="line w100per h1 bf2f2f2 mt20 mb20"></div>
                <div class="table-type-3 scr-type-2 rel">
                    <table>
                        <thead>
                            <tr>
                                <th class="wsn" style="width: 44px; min-width:44px;">
                                    <div class="check-type-1">
                                        <input type="checkbox" id="itemAll" bind:checked={itemAllSelected} on:click={toggleAllSelection}>
                                        <label for="itemAll"></label>
                                    </div> 
                                </th>
                                <th class="wsn">품목명</th>
                                <th class="wsn" style="width: 100px; min-width:100px;">수량</th>
                                <th class="wsn" style="width: 140px; min-width:140px;">단가</th>
                                <th class="wsn" style="width: 160px; min-width:160px;">금액</th>
                            </tr>
                        </thead>
                        <tbody>
                            {#each items as item, index (item.id)}
                            <tr>
                                <td class="wsn">
                                    <div class="check-type-1">
                                        <input type="checkbox" id={`v${index}`} bind:checked={item.selected} on:click={() => toggleSelection(item)}>
                                        <label for={`v${index}`}></label>
                                    </div> 
                                </td>
                                <td class="wsn">
                                    <div class="input-type-2 f14">
                                        <input type="text" placeholder="품목명" bind:value={item.itemName} on:keydown={(event) => itemNameKeyUp(event, item)}>
                                    </div>
                                </td>
                                <td class="wsn">
                                    <div class="input-type-2 f14">
                                        <input type="number" placeholder="수량" bind:value={item.inputQuantity} on:input={() => quantityChange(item)}>
                                    </div>
                                </td>
                                <td class="wsn">
                                    <div class="input-type-2 f14">
                                        <input type="number" placeholder="단가" readonly bind:value={item.salesPrice}>
                                    </div>
                                </td>
                                <td class="wsn">
                                    <div class="input-type-2 f14">
                                        <input type="number" placeholder="금액" readonly bind:value={item.sumPrice}>
                                    </div>
                                </td>
                            </tr>
                            {/each}
                        </tbody>
                        <tbody>
                            <tr class="last">
                                <td class="wsn"></td>
                                <td class="wsn"></td>
                                <td class="wsn"></td>
                                <td class="wsn"></td>
                                <td class="wsn">{formatAllPrice}원</td>
                            </tr>
                        </tbody>
                    </table>
                    <div class="flex aic g4 abs" style="left: 0; bottom: 0;">
                        <button class="w50 h30 btn-type-1 bdm bdr4 f12 cm" type="button" on:click={addRow}>추가</button>
                        <button class="w50 h30 btn-type-1 bdA2A9B0 bdr4 f12 cA2A9B0" type="button" on:click={deleteSelectedItems}>삭제</button>
                    </div>
                </div>
                <div class="btn-area flex aic jcc g8 mt40">
                    <button class="w120 h40 btn-type-2 bdr4 bm cfff tm f14" type="button" on:click={submitSignupForm}>등록</button>
                    <button class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" type="button" on:click="{deactivateModal}">취소</button>
                </div>
            </form>
        </div>
    </div>

    <!-- 판매 수정 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w800" class:active="{isActiveModifi}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">판매 수정</h3>
            <button class="x-btn img-box abs" on:click="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <form>
                <div class="chit-box flex fdc g12">
                    <ul class="w100per flex aic g20">
                        <li class="flex aic g12">
                            <span class="title-text f14 c333">일자</span>
                            <div class="input-box input-type-2 f14 w140">
                                <input type="date" placeholder="일자" bind:value={purchaseDate}>
                            </div>
                        </li>
                        <li class="flex aic g12">
                            <span class="title-text f14 c333">거래처</span>
                            <div class="input-box flex aic g8 w200">
                                <div class="input-type-2 f14 w100per">
                                    <input type="text" placeholder="거래처명" readonly value={selectedClient.clientName}>
                                </div>
                                <button class="btn-type-1 w60 h36 f14 bdr4 b333 cfff" type="button" style="min-width: 60px;" on:click={searchClients}>찾기</button>
                            </div>
                        </li>
                        <li class="flex aic g12">
                            <span class="title-text f14 c333">연락처</span>
                            <div class="input-box input-type-2 f14">
                                <input type="text" placeholder="연락처" readonly disabled value={selectedClient.phoneNumber}>
                            </div>
                        </li>
                    </ul>
                    <ul class="w100per flex aic g20">
                        <li class="flex aic g12" style="width:67%">
                            <span class="title-text f14 c333">주소</span>
                            <div class="input-box input-type-2 f14 w100per">
                                <input type="text" placeholder="주소" readonly disabled value={selectedClient.address}>
                            </div>
                        </li>
                        <li class="flex aic g12" style="width:calc(33% - 20px)">
                            <span class="title-text f14 c333">출고 여부</span>
                            <div class="check-type-1">
                                <input type="checkbox" id="w1" bind:checked={deliveryStatus}>
                                <label for="w1"></label>
                            </div>
                        </li>
                    </ul>
                    <div class="w100per">
                        <div class="flex aic g12">
                            <span class="title-text f14 c333">특이사항</span>
                            <div class="input-box textarea-type-1 f14 w100per h60">
                                <textarea placeholder="특이사항" bind:value={significant}></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="line w100per h1 bf2f2f2 mt20 mb20"></div>
                <div class="table-type-3 scr-type-2 rel">
                    <table>
                        <thead>
                            <tr>
                                <th class="wsn" style="width: 44px; min-width:44px;">
                                    <div class="check-type-1">
                                        <input type="checkbox" id="itemAll" bind:checked={itemAllSelected} on:click={toggleAllSelection}>
                                        <label for="itemAll"></label>
                                    </div> 
                                </th>
                                <th class="wsn">품목명</th>
                                <th class="wsn" style="width: 100px; min-width:100px;">수량</th>
                                <th class="wsn" style="width: 140px; min-width:140px;">단가</th>
                                <th class="wsn" style="width: 160px; min-width:160px;">금액</th>
                            </tr>
                        </thead>
                        <tbody>
                            {#each items as item, index (item.id)}
                            <tr>
                                <td class="wsn">
                                    <div class="check-type-1">
                                        <input type="checkbox" id={`v${index}`} bind:checked={item.selected} on:click={() => toggleSelection(item)}>
                                        <label for={`v${index}`}></label>
                                    </div> 
                                </td>
                                <td class="wsn">
                                    <div class="input-type-2 f14">
                                        <input type="text" placeholder="품목명" bind:value={item.itemName} on:keydown={(event) => itemNameKeyUp(event, item)}>
                                    </div>
                                </td>
                                <td class="wsn">
                                    <div class="input-type-2 f14">
                                        <input type="number" placeholder="수량" bind:value={item.inputQuantity} on:input={() => quantityChange(item)}>
                                    </div>
                                </td>
                                <td class="wsn">
                                    <div class="input-type-2 f14">
                                        <input type="number" placeholder="단가" readonly bind:value={item.salesPrice}>
                                    </div>
                                </td>
                                <td class="wsn">
                                    <div class="input-type-2 f14">
                                        <input type="number" placeholder="금액" readonly bind:value={item.sumPrice}>
                                    </div>
                                </td>
                            </tr>
                            {/each}
                        </tbody>
                        <tbody>
                            <tr class="last">
                                <td class="wsn"></td>
                                <td class="wsn"></td>
                                <td class="wsn"></td>
                                <td class="wsn"></td>
                                <td class="wsn">{formatAllPrice}원</td>
                            </tr>
                        </tbody>
                    </table>
                    <div class="flex aic g4 abs" style="left: 0; bottom: 0;">
                        <button class="w50 h30 btn-type-1 bdm bdr4 f12 cm" type="button" on:click={addRow}>추가</button>
                        <button class="w50 h30 btn-type-1 bdA2A9B0 bdr4 f12 cA2A9B0" type="button" on:click={deleteSelectedItems}>삭제</button>
                    </div>
                </div>
                <div class="btn-area flex aic jcc g8 mt40">
                    <button class="w120 h40 btn-type-2 bdr4 bm cfff tm f14" type="button" on:click={submitSignupForm}>등록</button>
                    <button class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" type="button" on:click="{deactivateModal}">취소</button>
                </div>
            </form>
        </div>
    </div>

</div>

<div class="modal-area-2 modal-area wh100per fixed zi10" class:active="{isActive2}">

    <!-- 거래처 찾기 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi10 w480" class:active="{isActiveAccountSearch}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">거래처 찾기</h3>
            <button class="x-btn img-box abs" on:click="{deactivateAccountSearchModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <div class="search-type-1 flex aic">
                <div class="search-box w100per">
                    <input type="search" placeholder="거래처명" bind:value={clientSerachInput} on:keydown={(event) => searchClientNameEnter(event)}>
                </div>
                <button class="search-btn flex aic jcc" on:click={searchClientNameClick}>
                    <span class="ico-box img-box w16">
                        <img src="/img/ico_search.svg" alt="검색 아이콘">
                    </span>
                </button>
            </div>
            <div class="table-type-3 scr-type-2 mt20">
                <table>
                    <thead>
                        <tr>
                            <th class="wsn">거래처명</th>
                            <th class="wsn">대표자명</th>
                            <th class="wsn">연락처</th>
                        </tr>
                    </thead>
                    <tbody>
                        {#each clients as client}
                        <tr>
                            <td class="wsn">
                                <button class="inblock tdu c162b60" on:click={() => selectClient(client)}>{client.clientName}</button>
                            </td>
                            <td class="wsn">{client.repName}</td>
                            <td class="wsn">{client.phoneNumber}</td>
                        </tr>
                        {/each}
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!--  품목 찾기 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi10 w480" class:active="{isActiveStockSearch}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">품목 찾기</h3>
            <button class="x-btn img-box abs" on:click="{deactivateAccountSearchModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <div class="search-type-1 flex aic">
                <div class="search-box w100per">
                    <input type="search" placeholder="품목명" bind:value={itemSerachInput} on:keydown={(event) => searchItemNameEnter(event, selectItem)}>
                </div>
                <button class="search-btn flex aic jcc" on:click={() => searchItemNameClick(selectItem)}>
                    <span class="ico-box img-box w16">
                        <img src="/img/ico_search.svg" alt="검색 아이콘">
                    </span>
                </button>
            </div>
            <div class="table-type-3 scr-type-2 mt20">
                <table>
                    <thead>
                        <tr>
                            <th class="wsn">품목명</th>
                            <th class="wsn">거래처명</th>
                            <th class="wsn">재고</th>
                            <th class="wsn">가격</th>
                        </tr>
                    </thead>
                    <tbody>
                        {#each stocks as stock}
                        <tr>
                            <td class="wsn">
                                <button class="inblock tdu c162b60" on:click={() => selectStock(stock)}>{stock.itemName}</button>
                            </td>
                            <td class="wsn">{stock.clientName}</td>
                            <td class="wsn">{stock.quantity}</td>
                            <td class="wsn">{stock.salesPrice}</td>
                        </tr>
                        {/each}
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</div>

<div class="store-management-area cnt-area w100per">
    <div class="title-box flex aic jcsb">
        <h1 class="tb c121619">판매 관리</h1>
    </div>
    <div class="cnt-box-1 cnt-box">
        <div class="top-area">
            <div class="space-area-2 flex aic jcsb">
                <div class="left-box flex aic">
                    <div class="flex aic g8">
                        <div class="input-type-2 f14 w140">
                            <input type="date" placeholder="조회" id="searchDateInput1">
                        </div>
                        <span class="f14">~</span>
                        <div class="input-type-2 f14 w140">
                            <input type="date" placeholder="조회" id="searchDateInput2">
                        </div>
                        <button class="btn-type-1 w60 h36 f14 bdr4 b333 cfff">조회</button>
                    </div>
                </div>
                <div class="right-box flex aic">
                    <div class="search-type-1 flex aic">
                        <div class="search-box">
                            <input type="search" bind:value={searchQuery} placeholder="검색어 입력" autocomplete="off" on:keypress={handleKeyPress}>
                        </div>
                        <button class="search-btn flex aic jcc" on:click={performSearch}>
                            <span class="ico-box img-box w16">
                                <img src="/img/ico_search.svg" alt="검색 아이콘">
                            </span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="line"></div>
        <div class="middle-area">
            <div class="approval-btn-box flex aic g8">
                <button class:active={isUnapprovedActive} on:click={() => unApprovalPurchase(true)}>미승인</button>
                <button class:active={isApprovedActive} on:click={() => approvalPurchase(false)}>승인</button>
            </div>
            <div class="all-text c121619 f14 mt16">
                전체 <span class="number inblock cm tm">{data.data.purchases.totalElements}</span>개
            </div>
            <div class="table-box-1 table-type-1 scr-type-2 mt12">
                <table>
                    <thead>
                        <tr>
                            <th class="wsn" style="width: 44px;">
                                <div class="check-type-1">
                                    <input type="checkbox" id="purchaseAll" checked={allChecked} on:change={toggleAll}>
                                    <label for="purchaseAll"></label>
                                </div> 
                            </th>
                            <th class="wsn">일자</th>
                            <th class="wsn">거래처</th>
                            <th class="wsn">품목명</th>
                            <th class="wsn">금액</th>
                            <th class="wsn">출고 여부</th>
                        </tr>
                    </thead>
                    <tbody>
                        {#each data.data.purchases.content as purchase}
                        <tr>
                            <td class="wsn" style="width: 44px;">   
                                <div class="check-type-1">
                                    <input type="checkbox" id={purchase.id} bind:checked={purchase.checked}>
                                    <label for={purchase.id}></label>
                                </div> 
                            </td>
                            <td class="wsn">
                                <button class="c162b60 tdu inblock" on:click="{activateModalModifi}">{purchase.purchaseDate}</button>
                            </td>
                            <td class="wsn">{purchase.client.clientName}</td>
                            <td class="wsn tal">
                                {purchase.purchaseStocks[0].itemName}
                                {#if purchase.purchaseStocks.length > 1}
                                외 {purchase.purchaseStocks.length - 1}건
                                {/if}
                            </td>
                            <td class="wsn tal">{purchase.allPrice}원</td>
                            <td class="wsn">
                                {#if purchase.deliveryStatus}완료
                                {:else}
                                {/if}
                            </td>
                        </tr>
                        {/each}
                    </tbody>
                </table>
            </div>
            <div class="flex aic jcsb mt8">
                <div class="situation-btn-box flex aic g4">
                    <button class:active={isUnapprovedActive} class="w50 h30 btn-type-1 bdm bdr4 f12 cm" on:click={approvePurchases}>승인</button>
                    <button class:active={isApprovedActive} class="w70 h30 btn-type-1 bdm bdr4 f12 cm" on:click={approvePurchasesCancel}>승인 취소</button>
                    <button class:active={isUnapprovedActive} class="w50 h30  btn-type-1 bdA2A9B0 bdr4 f12 cA2A9B0" on:click={PurchasesDelete}>삭제</button>
                </div>
                <div class="flex aic g4">
                    <button class="w50 h30 btn-type-1 bm bdr4 f12 cfff" on:click="{activateModalAdd}">등록</button>
                </div>
            </div>
            <div class="paging-box flex jcc mt40">
                <ul class="flex aic jcc">
                    {#if data.data.purchases.number > 0}
                        <!-- 현재 페이지가 첫 페이지가 아닐 때만 이전 버튼을 표시 -->
                        <!-- svelte-ignore a11y-click-events-have-key-events -->
                        <!-- svelte-ignore a11y-no-noninteractive-element-interactions -->
                        <li class="page-btn"
                            on:click={() => changePage(data.searchKeyword, data.data.purchases.number - 1)}>
                            <a href="">이전</a>
                        </li>
                    {/if}
                    {#each generatePageButtons(data.data.purchases.totalPages) as button}
                        <!-- svelte-ignore a11y-click-events-have-key-events -->
                        <!-- svelte-ignore a11y-no-noninteractive-element-interactions -->
                        <li
                                class="num"
                                on:click={() => data.data.purchases.number !== button - 1 && changePage(data.searchKeyword, button - 1)}
                        >
                            <a href="" class:active={data.data.purchases.number === button - 1}>{button}</a>
                        </li>
                    {/each}
                    {#if data.data.purchases.number < data.data.purchases.totalPages - 1}
                        <!-- 현재 페이지가 마지막 페이지가 아닐 때만 다음 버튼을 표시 -->
                        <!-- svelte-ignore a11y-click-events-have-key-events -->
                        <!-- svelte-ignore a11y-no-noninteractive-element-interactions -->
                        <li class="page-btn"
                            on:click={() => changePage(data.searchKeyword, data.data.purchases.number + 1)}>
                            <a href="">다음</a>
                        </li>
                    {/if}
                </ul>
            </div>
        </div>
    </div>
</div>