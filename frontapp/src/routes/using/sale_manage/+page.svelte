<script>
    import './MyCalendar.css';
    import { onMount } from 'svelte';
    import { Calendar } from '@fullcalendar/core';
    import interactionPlugin from '@fullcalendar/interaction'; // for selectable
    import dayGridPlugin from '@fullcalendar/daygrid'; // for dayGridMonth view

    let calendar;
// 모달 비활성화
    let isActiveRecord = false;
    let isActive = false;

    function handleEventClick() {
        isActiveRecord = true;
        isActive = true;
    }

    function deactivateModal() {
        isActiveRecord = false;
        isActive = false;
    }

    function handleDateSelect(info) {
        const userInput = window.prompt('Enter your event:');
        if (userInput) {
            calendar.addEvent({ title: userInput, start: info.startStr, end: info.endStr });
        }
    }

    onMount(async () => {
        const calendarEl = document.getElementById('calendar');

        const calendar = new Calendar(calendarEl, {
            plugins: [interactionPlugin, dayGridPlugin],
            initialView: 'dayGridMonth',
            selectable: false,
            select: handleDateSelect,
            events: [],
            eventContent: function(arg) {
                return {
                    html: '<b>' + arg.event.title + '</b><br>' + arg.event.extendedProps.description,
                };
            },
            eventClick: handleEventClick
        });
        calendar.render();

        try {
            let purchaseId = 1;  // 필요한 purchaseId 값을 지정하세요.

            const response = await fetch('http://localhost:8080/api/v1/sales', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ purchaseId: purchaseId })
            });

            if (response.ok) {
                const salesData = await response.json();
                console.log(salesData.data.salesManagement)

                localStorage.setItem('salesData', JSON.stringify(salesData));

                // 판매 관리 목록을 순회하면서 각 항목을 이벤트로 추가합니다.
                salesData.data.salesManagement.forEach(sale => {
                    console.log(sale.dailyTotalSales)
                    console.log(sale.dailySalesNumber)
                    console.log(sale.salesDate)
                    calendar.addEvent({
                        title: sale.dailyTotalSales + '원',
                        description: sale.dailySalesNumber + '건',
                        start: sale.salesDate,
                        color: 'white',
                        textColor: 'black',
                    });
                });
            }
        } catch (error) {
            console.error('오류 발생:', error);
        }
    });
</script>

<div class="store-management-area cnt-area w100per">
    <div class="title-box flex aic jcsb">
        <h1 class="tb c121619">매출 관리</h1>
    </div>
    <div class="cnt-box bsb pt20 pr28 pb20 pl28 bfff bdr12">
        <div class="calender-box">
            <div id="calendar"></div>
        </div>
    </div>
</div>

<div class="modal-area wh100per fixed zi9" class:active="{isActive}">
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
                        <img src="/img/arrow_bottom_A2A9B0.svg" alt="" />
                    </span>
            </div>
            <div class="flex aic g8">
                <div class="input-type-2 f14 w200">
                    <input type="date" placeholder="조회">
                </div>
                <span class="f14">~</span>
                <div class="input-type-2 f14 w200">
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