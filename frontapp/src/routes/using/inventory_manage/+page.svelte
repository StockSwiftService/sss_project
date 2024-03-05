<script>
    import {page} from "$app/stores";
    import {goto, replaceState} from "$app/navigation";
    import { onMount } from 'svelte';

    export let data;
    let isActive = false;
    let isActive2 = false;

    let isActiveAdd = false;
    let isActiveModify = false;
    let isActiveRecord = false;
    let isActiveAccountSearch = false;
    let currentClientId = null;

    function activateModalAdd() {
        isActive = true;
        isActiveAdd = true;
        resetForm();
    }

    function activateModalRecord() {
        isActive = true;
        isActiveRecord = true;
    }

    function activateModalAccountSearch() {
        isActive2 = true;
        isActiveAccountSearch = true;
    }

    function deactivateModal() {
        isActive = false;
        isActiveAdd = false;
        isActiveModify = false;
        isActiveRecord = false;
        confirmNameErrorMessage = '';
        confirmNameSuccessMessage = '';
    }

    function deactivateAccountSearchModal() {
        isActive2 = false;
        isActiveAccountSearch = false;
    }

    const resetForm = () => {
        const form = document.querySelector('form');
        if (form) {
            form.reset(); // 기본 폼 초기화
            const allErrorElements = document.querySelectorAll('.error-text-box .error-text');
            allErrorElements.forEach((errorElement) => {
                errorElement.textContent = '';
            });
        }
    };

    let formData = {
        clientName: '',
        itemName: '',
        quantity: '',
        purchasePrice: '',
        salesPrice: ''
    };
    let formDataModify = {
        clientNameModify: '',
        itemNameModify: '',
        purchasePriceModify: '',
        salesPriceModify: '',
    };
    onMount(async () => {
        await fetchClients();
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

    const submitClientForm = async (event) => {
        event.preventDefault();
        await checkDuplicate();

        if (confirmNameErrorMessage) {
            alert(confirmNameErrorMessage);
            return;
        }

        const clientName = formData.clientName;
        const itemName = formData.itemName;
        const quantity = formData.quantity;
        const purchasePrice = formData.purchasePrice;
        const salesPrice = formData.salesPrice;

        const errors = {};

        if (!clientName.trim()) {
            errors.clientName = '거래처명을 입력하세요.';
        }

        if (!itemName.trim()) {
            errors.itemName = '품목명을 입력하세요.';
        }

        if (!quantity) {
            errors.quantity = '수량을 입력하세요.';
        }

        if (!purchasePrice) {
            errors.purchasePrice = '구매단가를 입력하세요.';
        }

        if (!salesPrice) {
            errors.salesPrice = '판매단가를 입력하세요.';
        }

        if (Object.keys(errors).length > 0) {
            updateErrorMessages(errors);
            return;
        }

        // 서버로 데이터 전송
        try {
            const response = await fetch('http://localhost:8080/api/v1/stocks', {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData),
            });

            if (response.ok) {
                const responseData = await response.json();
                console.log(responseData);
                window.alert('저장되었습니다.');
                goto(`/using/inventory_manage`);
                setTimeout(() => {
                    location.reload();
                }, 100);
            } else {
                const responseData = await response.json();
                console.error(responseData);
                window.alert('저장에 실패했습니다.');
            }
        } catch (error) {
            console.error('Error submitting form:', error);
        }
    }

    const modifyClientForm = async (event) => {
        event.preventDefault();

        // 클라이언트 측 유효성 검사 수행
        const clientNameModify = formDataModify.clientNameModify;
        const itemNameModify = formDataModify.itemNameModify;
        const purchasePriceModify = formDataModify.purchasePriceModify;
        const salesPriceModify = formDataModify.salesPriceModify;

        const errors = {};

        if (!clientNameModify.trim()) {
            errors.clientNameModify = '거래처명을 입력하세요.';
        }

        if (!itemNameModify.trim()) {
            errors.itemNameModify = '품목명을 입력하세요.';
        }

        if (!purchasePriceModify) {
            errors.purchasePriceModify = '구매단가를 입력하세요.';
        }

        if (!salesPriceModify) {
            errors.salesPriceModify = '판매단가를 입력하세요.';
        }

        // 클라이언트 측에서 유효성 검사 실패 시 제출 중단
        if (Object.keys(errors).length > 0) {
            updateErrorMessages(errors);
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/v1/stocks/${currentClientId}`, {
                method: 'PATCH',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    clientNameModify: formDataModify.clientNameModify,
                    itemNameModify: formDataModify.itemNameModify,
                    purchasePriceModify: formDataModify.purchasePriceModify,
                    salesPriceModify: formDataModify.salesPriceModify
                }),
            });
            if (response.ok) {
                const responseData = await response.json();
                console.log(responseData);
                window.alert('내용이 수정되었습니다.');
                goto(`/using/inventory_manage`);
                setTimeout(() => {
                    location.reload();
                }, 100);
            } else {
                const responseData = await response.json();
                console.error(responseData);
                window.alert('수정에 실패했습니다.');
            }
        } catch (error) {
            console.error('Error submitting form:', error);
        }
    }

    async function activateModalModify(clientId) {
        // 모달 활성화 및 상태 초기화
        isActive = true;
        isActiveModify = true;
        currentClientId = clientId;
        resetForm();

        try {
            const response = await fetch(`http://localhost:8080/api/v1/stocks/${currentClientId}`, {
                credentials: 'include'
            });
            if (response.ok) {
                const stockData = await response.json();
                formDataModify.clientNameModify = stockData.data.stockDto.clientName;
                formDataModify.itemNameModify = stockData.data.stockDto.itemName;
                formDataModify.purchasePriceModify = stockData.data.stockDto.purchasePrice;
                formDataModify.salesPriceModify = stockData.data.stockDto.salesPrice;
                formDataModify = {...formDataModify};
                console.log(stockData)
            } else {

                console.error("Failed to fetch client details");
            }
        } catch (error) {
            console.error('Error fetching client details:', error);
        }
    }

    async function deleteSelectedClients() {
        const selectedIds = data.data.stocks.content
            .filter(stock => stock.checked)
            .map(client => client.id);


        if (selectedIds.length === 0) {
            alert('삭제할 재고를 선택해주세요.');
            return;
        }

        const isConfirmed = confirm('선택한 재고를 삭제하시겠습니까?');

        if (!isConfirmed) {
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/v1/stocks/deleteMultiple', {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(selectedIds),
            });

            if (response.ok) {
                alert('선택한 재고가 삭제되었습니다.');
                goto(`/using/inventory_manage`);
                setTimeout(() => {
                    location.reload();
                }, 100);
            } else {
                alert('재고 삭제에 실패했습니다.');
            }
        } catch (error) {
            console.error('Error deleting clients:', error);
            alert('재고 삭제 중 에러가 발생했습니다.');
        }
    }

    const updateErrorMessages = (errors) => {
        // 각 오류 메시지 업데이트
        for (const fieldName in errors) {
            const errorElement = document.querySelector(
                `.error-text-box[data-field="${fieldName}"] .error-text`
            );
            if (errorElement) {
                errorElement.textContent = errors[fieldName];
            }
        }
        const inputFields = document.querySelectorAll('.input-type-1 input');
        inputFields.forEach((inputField) => {
            inputField.addEventListener('input', () => {
                const fieldName = inputField.getAttribute('name');
                const errorElement = document.querySelector(
                    `.error-text-box[data-field="${fieldName}"] .error-text`
                );
                if (errorElement) {
                    errorElement.textContent = '';
                }
            });
        });
    };

    let confirmNameErrorMessage = '';
    let confirmNameSuccessMessage = '';
    const checkDuplicate = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/v1/stocks/check', {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({clientName: formData.clientName || formDataModify.clientNameModify, itemName: formData.itemName || formDataModify.itemNameModify }),
            });

            if (response.ok) {
                const responseData = await response.json();
                if (responseData.resultCode === 'S-6') {
                    confirmNameErrorMessage = '중복된 품목명입니다.';
                    confirmNameSuccessMessage = '';

                } else {
                    confirmNameSuccessMessage = '중복되지 않는 품목명입니다.';
                    confirmNameErrorMessage = '';
                }
            } else {
                const responseData = await response.json();
                console.error(responseData);
                window.alert('내용을 입력하세요.');
            }
        } catch (error) {
            console.error('Error checking duplicate:', error);
        }
    };


    let searchKeyword = '';
    let clients = [];

    // 거래처 목록을 불러오는 함수
    async function fetchClients(keyword = '') {
        const response = await fetch(`http://localhost:8080/api/v1/clients/search?clientName=${keyword}`);
        if (response.ok) {
            clients = await response.json();
        } else {
            console.error('거래처 목록을 불러오는데 실패했습니다.');
        }
    }
    // 검색 함수
    async function searchClients() {
        await fetchClients(searchKeyword);
    }
    function selectClient(client) {
        formData.clientName = client.clientName;
        formDataModify.clientNameModify = client.clientName;
        deactivateAccountSearchModal();
    }
    function handleKeyPress(event) {
        if (event.key === 'Enter') {
            searchClients();
            performSearch();
        }
    }

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

    async function changePage(searchQuery, currentPage) {
        try {

            $page.url.searchParams.get('kw', searchQuery);
            $page.url.searchParams.set('page', currentPage);

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

        await goto(`?${$page.url.searchParams.toString()}`, {replaceState});

        await dataLoad();

    }

    async function dataLoad() {
        const queryString = window.location.search;

        const res = await fetch(`http://localhost:8080/api/v1/stocks${queryString}`, {
            credentials: 'include'
        })
        data = await res.json();

    }

    let allChecked = false;

    function toggleAll() {

        data.data.stocks.content.map(client => {
            if (!client.checked) {
                client.checked = true;
            } else {
                client.checked = false;
            }
        })

        data = data;
    }

    async function downloadExcel() {
        try {
            const response = await fetch('http://localhost:8080/api/v1/stocks/excel');
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

    let phoneNumber = '';

    function formatPhoneNumber(phoneNumber) {
        if (phoneNumber.length === 10) {
            return phoneNumber.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3');
        } else if (phoneNumber.length === 11) {
            return phoneNumber.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
        }
        return phoneNumber;
    }

</script>

<div class="modal-area-1 modal-area wh100per fixed zi9" class:active="{isActive}">

    <!-- 재고 등록 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w480" class:active="{isActiveAdd}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">재고 등록</h3>
            <button class="x-btn img-box abs" on:click="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <form on:submit|preventDefault={submitClientForm}>
                <div class="flex fdc g36">
                    <div>
                        <h2 class="c333 f15 tm mb8">거래처명<span class="cr f16 tm inblock">*</span></h2>
                        <div class="flex g8">
                            <div class="input-type-1 f14 w100per">
                                <input bind:value={formData.clientName} type="text" name="clientName" placeholder="거래처명" readonly>
                            </div>
                            <button type="button" class="btn-type-1 w80 f14 bdr4 b333 cfff" on:click={activateModalAccountSearch}>찾기
                            </button>
                        </div>
                        <div class="error-text-box" data-field="clientName">
                            <span class="error-text f13 mt8 cr"></span>
                        </div>
                    </div>
                    <div>
                        <h2 class="c333 f15 tm mb8">품목명<span class="cr f16 tm inblock">*</span></h2>
                        <div class="flex g8">
                            <div class="input-type-1 f14 w100per">
                                <input bind:value={formData.itemName} type="text" name="itemName" placeholder="품목명">
                            </div>
                            <button type="button" class="btn-type-1 w80 f14 bdr4 b333 cfff" on:click={checkDuplicate}>확인</button>
                        </div>
                        <div class="error-text-box" data-field="itemName">
                            <span class="error-text f13 mt8 cr"></span>
                        </div>
                        {#if confirmNameErrorMessage}
                            <span class="f13 mt8 cr">{confirmNameErrorMessage}</span>
                        {/if}
                        {#if confirmNameSuccessMessage}
                            <span class="f13 mt8 cg">{confirmNameSuccessMessage}</span>
                        {/if}
                    </div>
                    <div>
                        <h2 class="c333 f15 tm mb8">수량<span class="cr f16 tm inblock">*</span></h2>
                        <div class="input-type-1 f14 w100per">
                            <input bind:value={formData.quantity} type="text" name="quantity" placeholder="수량">
                        </div>
                        <div class="error-text-box" data-field="quantity">
                            <span class="error-text f13 mt8 cr"></span>
                        </div>
                    </div>
                    <div>
                        <h2 class="c333 f15 tm mb8">구매 단가<span class="cr f16 tm inblock">*</span></h2>
                        <div class="input-type-1 f14 w100per">
                            <input bind:value={formData.purchasePrice} type="text" name="purchasePrice" placeholder="구매 단가">
                        </div>
                        <div class="error-text-box" data-field="purchasePrice">
                            <span class="error-text f13 mt8 cr"></span>
                        </div>
                    </div>
                    <div>
                        <h2 class="c333 f15 tm mb8">판매 단가<span class="cr f16 tm inblock">*</span></h2>
                        <div class="input-type-1 f14 w100per">
                            <input bind:value={formData.salesPrice} type="text" name="salesPrice" placeholder="판매 단가">
                        </div>
                        <div class="error-text-box" data-field="salesPrice">
                            <span class="error-text f13 mt8 cr"></span>
                        </div>
                    </div>
                </div>
                <div class="btn-area flex aic jcc g8 mt40">
                    <button type="submit" class="w120 h40 btn-type-2 bdr4 bm cfff tm f14">등록</button>
                    <button type="button" class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" on:click="{deactivateModal}">취소</button>
                </div>
            </form>
        </div>
    </div>

    <!-- 재고 수정 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w480" class:active="{isActiveModify}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">재고 수정</h3>
            <button type="button" class="x-btn img-box abs" on:click="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <form on:submit|preventDefault={modifyClientForm}>
            <div class="flex fdc g36">
                <div>
                    <h2 class="c333 f15 tm mb8">거래처명<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" bind:value={formDataModify.clientNameModify} name="clientNameModify" placeholder="거래처명">
                        </div>
                        <button type="button" class="btn-type-1 w80 f14 bdr4 b333 cfff" on:click={activateModalAccountSearch}>찾기
                        </button>
                    </div>
                    <div class="error-text-box" data-field="clientName">
                        <span class="error-text f13 mt8 cr"></span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">품목명<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" bind:value={formDataModify.itemNameModify} name="itemNameModify" placeholder="품목명">
                        </div>
                        <button type="button" class="btn-type-1 w80 f14 bdr4 b333 cfff" on:click={checkDuplicate}>확인</button>
                    </div>
                    <div class="error-text-box" data-field="itemNameModify">
                        <span class="error-text f13 mt8 cr"></span>
                    </div>
                    {#if confirmNameErrorMessage}
                        <span class="f13 mt8 cr">{confirmNameErrorMessage}</span>
                    {/if}
                    {#if confirmNameSuccessMessage}
                        <span class="f13 mt8 cg">{confirmNameSuccessMessage}</span>
                    {/if}
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">구매 단가<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" bind:value={formDataModify.purchasePriceModify} name="purchasePriceModify" placeholder="구매 단가">
                    </div>
                    <div class="error-text-box" data-field="purchasePriceModify">
                        <span class="error-text f13 mt8 cr"></span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">판매 단가<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" bind:value={formDataModify.salesPriceModify} name="salesPriceModify" placeholder="판매 단가">
                    </div>
                    <div class="error-text-box" data-field="salesPriceModify">
                        <span class="error-text f13 mt8 cr"></span>
                    </div>
                </div>
            </div>
            <div class="btn-area flex aic jcc g8 mt40">
                <button type="submit" class="w120 h40 btn-type-2 bdr4 bm cfff tm f14">수정</button>
                <button type="button" class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" on:click="{deactivateModal}">취소</button>
            </div>
            </form>
        </div>
    </div>

    <!-- 재고 이력 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w800" class:active="{isActiveRecord}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">재고 이력</h3>
            <button class="x-btn img-box abs" on:click="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <div class="flex aic jcsb">
                <div class="select-type-4 w100 f14 rel">
                    <select name="account">
                        <option value="">전체</option>
                        <option value="">판매</option>
                        <option value="">구매</option>
                    </select>
                    <span class="arrow img-box abs y-middle">
                        <img src="/img/arrow_bottom_A2A9B0.svg" alt=""/>
                    </span>
                </div>
                <div class="flex aic g8">
                    <div class="input-type-2 f14 w140">
                        <input type="date" placeholder="조회">
                    </div>
                    <span class="f14">~</span>
                    <div class="input-type-2 f14 w140">
                        <input type="date" placeholder="조회">
                    </div>
                    <button class="btn-type-1 w60 h36 f14 bdr4 b333 cfff">조회</button>
                </div>
            </div>
            <div class="line w100per h1 bf2f2f2 mt20 mb20"></div>
            <h1 class="f14 c777 tm">거래처명 : (주)네모컴퍼니 | 품목명 : 네모네모 스넥 100g</h1>
            <div class="table-type-2 mt12">
                <table>
                    <thead>
                    <tr>
                        <th colspan="4">2024-02-10</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td colspan="4">기존 재고 : 50개</td>
                    </tr>
                    </tbody>
                    <thead>
                    <tr>
                        <th colspan="4">2024-02-16</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td class="w120">2024-02-16</td>
                        <td class="w60">
                            <span class="inblock cb">판매</span>
                        </td>
                        <td>김네모</td>
                        <td>50개</td>
                    </tr>
                    <tr>
                        <td class="w120">2024-02-16</td>
                        <td>
                            <span class="inblock cr">구매</span>
                        </td>
                        <td>(주)네모컴퍼니</td>
                        <td>50개</td>
                    </tr>
                    </tbody>
                    <thead>
                    <tr>
                        <th colspan="4">2024-02-18</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td class="w120">2024-02-16</td>
                        <td class="w60">
                            <span class="inblock cb">판매</span>
                        </td>
                        <td>김철수</td>
                        <td>3개</td>
                    </tr>
                    <tr>
                        <td class="w120">2024-02-16</td>
                        <td>
                            <span class="inblock cb">판매</span>
                        </td>
                        <td>박지수</td>
                        <td>1개</td>
                    </tr>
                    </tbody>
                </table>
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
                    <input type="search" placeholder="거래처명" bind:value={searchKeyword} on:keypress={handleKeyPress}>
                </div>
                <button class="search-btn flex aic jcc" on:click={searchClients}>
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
                            <td class="wsn">{formatPhoneNumber(client.phoneNumber)}</td>
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
        <h1 class="tb c121619">재고 관리</h1>
    </div>
    <div class="cnt-box-1 cnt-box">
        <div class="top-area">
            <div class="space-area-2 flex aic jce">
                <div class="right-box flex aic">
                    <div class="search-type-1 flex aic">
                        <div class="search-box">
                            <input type="search" bind:value={searchQuery} placeholder="검색어 입력" autocomplete="off"
                                   on:keypress={handleKeyPress}>
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
            <div class="all-text c121619 f14">
<!--                전체 <span class="number inblock cm tm">{hasSearchQuery ? searchResultCount : totalClients}</span>개-->
                전체 <span class="number inblock cm tm">{data.data.stocks.totalElements}</span>개
            </div>
            <div class="table-box-1 table-type-1 scr-type-2 mt12">
                <table>
                    <thead>
                    <tr>
                        <th class="wsn" style="width: 44px;">
                            <div class="check-type-1">
                                <input type="checkbox" id="all" checked={allChecked} on:change={toggleAll}>
                                <label for="all"></label>
                            </div>
                        </th>
                        <th class="wsn">거래처명</th>
                        <th class="wsn">품목명</th>
                        <th class="wsn">수량</th>
                        <th class="wsn">구매 단가</th>
                        <th class="wsn">판매 단가</th>
                        <th class="wsn">이력</th>
                        <th class="wsn">수정</th>
                    </tr>
                    </thead>
                    <tbody>
                    {#each data.data.stocks.content as stock}
                    <tr>
                        <td class="wsn" style="width: 44px;">
                            <div class="check-type-1">
                                <input type="checkbox" bind:checked={stock.checked} id="{stock.id}">
                                <label for="{stock.id}"></label>
                            </div>
                        </td>
                        <td class="wsn">{stock.clientName}</td>
                        <td class="wsn tal">{stock.itemName}</td>
                        <td class="wsn">{stock.quantity.toLocaleString()}</td>
                        <td class="wsn">{stock.purchasePrice.toLocaleString()}</td>
                        <td class="wsn">{stock.salesPrice.toLocaleString()}</td>
                        <td class="wsn tac">
                            <button class="w40 h24 btn-type-2 bdr4 bdbbb cbbb f13" on:click={activateModalRecord}>이력
                            </button>
                        </td>
                        <td class="wsn tac">
                            <button class="w40 h24 btn-type-2 bdr4 bdbbb cbbb f13"
                                    on:click={() => activateModalModify(stock.id)}>
                                수정
                            </button>
                        </td>
                    </tr>
                        {/each}
                    </tbody>
                </table>
            </div>
            <div class="flex aic jcsb mt8">
                <div class="flex aic g4">
                    <button class="w50 h30  btn-type-1 bdA2A9B0 bdr4 f12 cA2A9B0" on:click="{deleteSelectedClients}">삭제</button>
                </div>
                <div class="flex aic g4">
                    <button class="w50 h30  btn-type-1 bdA2A9B0 bdr4 f12 cA2A9B0" on:click={downloadExcel}>Excel</button>
                    <button class="w50 h30 btn-type-1 bm bdr4 f12 cfff" on:click="{activateModalAdd}">등록</button>
                </div>
            </div>
            <div class="paging-box flex jcc mt40">
                <ul class="flex aic jcc">
                    {#if data.data.stocks.number > 0}
                        <!-- 현재 페이지가 첫 페이지가 아닐 때만 이전 버튼을 표시 -->
                        <!-- svelte-ignore a11y-click-events-have-key-events -->
                        <!-- svelte-ignore a11y-no-noninteractive-element-interactions -->
                        <li class="page-btn"
                            on:click={() => changePage(data.searchKeyword, data.data.stocks.number - 1)}>
                            <a href="">이전</a>
                        </li>
                    {/if}
                    {#each generatePageButtons(data.data.stocks.totalPages) as button}
                        <!-- svelte-ignore a11y-click-events-have-key-events -->
                        <!-- svelte-ignore a11y-no-noninteractive-element-interactions -->
                        <li
                                class="num"
                                on:click={() => data.data.stocks.number !== button - 1 && changePage(data.searchKeyword, button - 1)}
                        >
                            <a href="" class:active={data.data.stocks.number === button - 1}>{button}</a>
                        </li>
                    {/each}
                    {#if data.data.stocks.number < data.data.stocks.totalPages - 1}
                        <!-- 현재 페이지가 마지막 페이지가 아닐 때만 다음 버튼을 표시 -->
                        <!-- svelte-ignore a11y-click-events-have-key-events -->
                        <!-- svelte-ignore a11y-no-noninteractive-element-interactions -->
                        <li class="page-btn"
                            on:click={() => changePage(data.searchKeyword, data.data.stocks.number + 1)}>
                            <a href="">다음</a>
                        </li>
                    {/if}
                </ul>
            </div>
        </div>
    </div>
</div>