<script>    
    import { onMount } from 'svelte';

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

    async function searchAccountNameAll() {
        isActive2 = true;
        isActiveAccountSearch = true;

        const response = await fetch('http://localhost:8080/api/v1/clients');
        if (response.ok) {
            const responseData = await response.json();
            clients = responseData.data.clients.content;
            console.log(clients);
        } else {
            console.error('서버로부터 데이터를 받아오는 데 실패했습니다.');
        }
    }

    async function searchClientNameKeyUp() {
        const response = await fetch(`http://localhost:8080/api/v1/clients/search?clientName=${clientSerachInput}`);
        if (response.ok) {
            const responseData = await response.json();
            clients = responseData.data.clients.content;
        }
        else {
            console.error('서버로부터 데이터를 받아오는 데 실패했습니다.');
        }
    }

    function searchClientNameEnter(event) {
        if (event.key === 'Enter') {
            searchClientNameKeyUp();
        }
    }

    function searchClientNameClick() {
        searchClientNameKeyUp();
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
                window.location.reload();
                console.log(data);
            } else {
                console.log("전표생성 실패");
                console.log(data);
            }
        } catch (error) {
            console.error('Error submitting form:', error);
        }
    }

    //판매 게시글 출력
    let purchases = [];

    onMount(async () => {

        //판매 게시글 출력
        const response = await fetch('http://localhost:8080/api/v1/purchase');
        if (response.ok) {
            const responseData = await response.json();
            purchases = responseData.data.purchases;
        } else {
            console.error('서버로부터 데이터를 받아오는 데 실패했습니다.');
        }

        //오늘 날짜로 기본 데이터 생성
        document.getElementById('searchDateInput1').value = getTodayDate();
        document.getElementById('searchDateInput2').value = getTodayDate();
        purchaseDate = getTodayDate();

    });
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
                                <button class="btn-type-1 w60 h36 f14 bdr4 b333 cfff" type="button" style="min-width: 60px;" on:click={searchAccountNameAll}>찾기</button>
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

    <!-- 재고 수정 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w480" class:active="{isActiveModifi}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">재고 수정</h3>
            <button class="x-btn img-box abs" on:click="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <div class="flex fdc g36">
                <div>
                    <h2 class="c333 f15 tm mb8">거래처명<span class="cr f16 tm inblock">*</span></h2>
                    <div class="select-type-3 w100per f14 rel">
                        <select name="account">
                            <option value="">모든 거래처</option>
                            <option value="">거래처1</option>
                            <option value="">거래처2</option>
                            <option value="">거래처3</option>
                        </select>
                        <span class="arrow img-box abs y-middle">
                            <img src="/img/arrow_bottom_A2A9B0.svg" alt="" />
                        </span>
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 선택 항목입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">품목명<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" placeholder="품목명">
                        </div>
                        <button class="btn-type-1 w80 f14 bdr4 b333 cfff">확인</button>
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">중복된 품목명입니다.</span>
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        <span class="f13 mt8 cg">사용할 수 있는 품목명입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">구매 단가<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="구매 단가">
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">판매 단가<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="판매 단가">
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    </div>
                </div>
            </div>
            <div class="btn-area flex aic jcc g8 mt40">
                <button class="w120 h40 btn-type-2 bdr4 bm cfff tm f14">수정</button>
                <button class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" on:click="{deactivateModal}">취소</button>
            </div>
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
                        <div class="search-box w200">
                            <input type="search" placeholder="검색어 입력">
                        </div>
                        <button class="search-btn flex aic jcc">
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
                <button class="active">미승인</button>
                <button>승인</button>
            </div>
            <div class="all-text c121619 f14 mt16">
                전체 <span class="number inblock cm tm">0</span>개
            </div>
            <div class="table-box-1 table-type-1 scr-type-2 mt12">
                <table>
                    <thead>
                        <tr>
                            <th class="wsn" style="width: 44px;">
                                <div class="check-type-1">
                                    <!-- <input type="checkbox" id="purchaseAll" bind:checked={itemAllSelected} on:click={toggleAllSelection}> -->
                                    <input type="checkbox" id="purchaseAll">
                                    <label for="purchaseAll"></label>
                                </div> 
                            </th>
                            <th class="wsn">일자</th>
                            <th class="wsn">거래처</th>
                            <th class="wsn">품목명</th>
                            <!-- <th class="wsn">수량</th> -->
                            <th class="wsn">금액</th>
                            <th class="wsn">출고 여부</th>
                        </tr>
                    </thead>
                    <tbody>
                        {#each purchases as purchase, index}
                        <tr>
                            <td class="wsn" style="width: 44px;">   
                                <div class="check-type-1">
                                    <input type="checkbox" id={`vv${index}`}>
                                    <label for={`vv${index}`}></label>
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
                            <!-- <td class="wsn">
                                {#each purchase.purchaseStocks as stock}
                                inputQuantity
                                재고간 총 수량 로직 작성해야함
                                {/each}
                            </td> -->
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
                <div class="flex aic g4">
                    <button class="w50 h30 btn-type-1 bdm bdr4 f12 cm">승인</button>
                    <button class="w50 h30  btn-type-1 bdA2A9B0 bdr4 f12 cA2A9B0">삭제</button>
                </div>
                <div class="flex aic g4">
                    <button class="w50 h30 btn-type-1 bm bdr4 f12 cfff" on:click="{activateModalAdd}">등록</button>
                </div>
            </div>
            <div class="paging-box flex jcc mt40">
                <ul class="flex aic jcc">
                    <li class="page-btn">
                        <a href="">이전</a>
                    </li>
                    <li class="num">
                        <a href="" class="active">1</a>
                    </li>
                    <li class="num">
                        <a href="">2</a>
                    </li>
                    <li class="num">
                        <a href="">3</a>
                    </li>
                    <li class="num">
                        <a href="">4</a>
                    </li>
                    <li class="num">
                        <a href="">5</a>
                    </li>
                    <li class="page-btn">
                        <a href="">다음</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>