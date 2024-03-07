<script>
    import './MyCalendar.css';
    import {onMount} from 'svelte';
    import {Calendar} from '@fullcalendar/core';
    import interactionPlugin from '@fullcalendar/interaction'; // for selectable
    import dayGridPlugin from '@fullcalendar/daygrid'; // for dayGridMonth view

    let calendar;
    // 모달 비활성화
    let isActiveRecord = false;
    let isActive = false;

    // 해당 날짜
    let currentdate = '';

    let totalPrice = [];

    let formdata = [];

    async function getList(date) {
        try {
            const response = await fetch('http://localhost:8080/api/v1/purchase/list', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({date: date})
            });
            if (response.ok) {
                const data = await response.json();
                currentdate = date;
                formdata = data.data.purchase;

                console.log(totalPrice)
            }
        } catch (error) {
            console.error('또 오류루:', error);
        }
    }

    // 날짜의 매출을 클릭하면 해당 날짜가 포함된 구매 혹은 판매 리스트를 출력 시키게 구현 해야함
    function handleEventClick(info) {
        isActiveRecord = true;
        isActive = true;

        let startDate = info.event.start;
        // 날짜형식을 yyyy-mm-dd형식으로 바꾸는 구문
        let startDateStr = `${startDate.getFullYear()}-${String(startDate.getMonth() + 1).padStart(2, '0')}-${String(startDate.getDate()).padStart(2, '0')}`;

        console.log(startDateStr)
        // 여기에 fetch를 사용해서 해당 날짜가 포함된 구매 판매 리스트를 갖고올 예정 post형식으로
        getList(startDateStr)
    }

    function deactivateModal() {
        isActiveRecord = false;
        isActive = false;
    }

    function handleDateSelect(info) {
        const userInput = window.prompt('Enter your event:');
        if (userInput) {
            calendar.addEvent({title: userInput, start: info.startStr, end: info.endStr});
        }
    }

    onMount(async () => {
        // console.log(globalSalesData)
        const calendarEl = document.getElementById('calendar');

        const calendar = new Calendar(calendarEl, {
            plugins: [interactionPlugin, dayGridPlugin],
            initialView: 'dayGridMonth',
            selectable: false,
            select: handleDateSelect,
            events: [],
            eventContent: function (arg) {
                return {
                    html: '<span class="tar c777 f16 mt4">' + arg.event.title + '</span>' + '<span  class="tar c777 f16 mt4">' + arg.event.extendedProps.description + '</span>'
                };
            },
            eventClick: handleEventClick,
            datesSet: function (dateInfo) {
                // 시작 날짜와 끝 날짜 사이의 중간 지점 계산
                const middleTimestamp = (dateInfo.start.getTime() + dateInfo.end.getTime()) / 2;
                const middleDate = new Date(middleTimestamp);

                // 중간 지점의 연도와 월을 현재 보고 있는 연도와 월로 사용
                const currentYear = middleDate.getFullYear();
                const currentMonth = middleDate.getMonth() + 1; // getMonth()는 0부터 시작하므로, 1을 더합니다.
                // 기존 합계 헤더를 삭제합니다.
                removeTotalHeaders();
                loadDataAndUpdateCalendar(currentYear, currentMonth).then(() => {
                    console.log('성공공');
                }).catch(error => {
                    console.error('에러엉:', error);
                });
            }
        });
        calendar.render();

        function removeTotalHeaders() {
            const totalHeaders = calendarEl.querySelectorAll('.weekplus');
            totalHeaders.forEach(header => header.remove());
        }

        //th 합계 텍스트 추가
        const totalTh = document.createElement('th');
        const thInner = document.createElement('span');
        thInner.textContent = '합계';
        totalTh.appendChild(thInner);

        const targetThSelector = 'table thead tr th table thead tr';
        calendarEl.querySelector(targetThSelector).appendChild(totalTh);

        //각 열 td 합계
        function addTotalHeader(rowNumber, weekTotalSales, weekTotalNumber) {

            const totalTd = document.createElement('td');
            totalTd.classList.add('bfafafa', 'weekplus');

            const tdInner = document.createElement('div');
            tdInner.classList.add('w100per', 'h100per', 'flex', 'fdc', 'aic', 'jcc');

            const tdInner1 = document.createElement('span');
            if (weekTotalSales === undefined || weekTotalSales === 0) {
                tdInner1.textContent = 0 + "원";
                tdInner1.classList.add('f16', 'c333', 'tac');
            } else {
                tdInner1.textContent = weekTotalSales + "원";
                tdInner1.classList.add('f16', 'c333', 'tac');
            }
            const tdInner2 = document.createElement('span');
            tdInner2.textContent = weekTotalNumber + "건";
            tdInner2.classList.add('f16', 'c333', 'tac', 'mt4');

            totalTd.appendChild(tdInner).appendChild(tdInner1);
            totalTd.appendChild(tdInner).appendChild(tdInner2);

            const targetTdSelector = `table tbody tr td table tbody tr:nth-child(${rowNumber})`;
            calendarEl.querySelector(targetTdSelector).appendChild(totalTd);
        }

        //총 값
        function addMonthTotal(monthTotalSales, monthTotal) {
            const allTotalTr = document.createElement('tr');
            allTotalTr.setAttribute('id', 'monthTotal');

            const allTotalTd = document.createElement('td');
            allTotalTd.setAttribute('colspan', '8');
            allTotalTd.classList.add('h100', 'bfafafa');

            const allTdInner = document.createElement('div');
            allTdInner.classList.add('w100per', 'h100per', 'flex', 'fdc', 'aic', 'jcc');

            const AlltdInner1 = document.createElement('span');
            AlltdInner1.textContent = monthTotalSales + "원";
            AlltdInner1.classList.add('f16', 'cm', 'tac', 'pb');

            const AlltdInner2 = document.createElement('span');
            AlltdInner2.textContent = monthTotal + "건";
            AlltdInner2.classList.add('f16', 'cm', 'tac', 'mt4', 'pb');

            allTotalTr.appendChild(allTotalTd).appendChild(allTdInner).appendChild(AlltdInner1);
            allTotalTr.appendChild(allTotalTd).appendChild(allTdInner).appendChild(AlltdInner2);

            const targetAllTdSelector = 'table tbody tr td table tbody';

            // DOM 선택
            const monthTotalEl = document.getElementById('monthTotal')
            if (monthTotalEl) {
                // 기존 DOM 대체
                monthTotalEl.replaceWith(allTotalTr);
            } else {
                // 새로추가
                calendarEl.querySelector(targetAllTdSelector).appendChild(allTotalTr);
            }
        }

        function loadDataAndUpdateCalendar(year, month) {
            // console.log('연도 :' + year + '달 :' + month)
            return new Promise(async (resolve) => {
                try {
                    let purchaseId = 1;
                    const response = await fetch('http://localhost:8080/api/v1/sales', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/json'},
                        body: JSON.stringify({purchaseId: purchaseId})
                    });

                    if (response.ok) {
                        let salesData = await response.json();
                        // 캘린더에 기존 이벤트가 있다면 제거
                        calendar.removeAllEvents();

                        let monthDataKey = `${year}-${month.toString().padStart(2, '0')}`;
                        let maxMonthData = {[monthDataKey]: {monthTotalSales: 0, monthTotal: 0}};

                        let maxWeeklyData = {};
                        for (let i = 1; i <= 6; i++) {
                            maxWeeklyData[i] = {weekTotalSales: 0, weekTotal: 0};
                        }
                        salesData.data.salesManagement.forEach((sale) => {
                            // console.log('sale', sale)
                            let saleKey = `${sale.year}-${sale.month.toString().padStart(2, '0')}`;
                            // 해당 연도
                            let dateYear = sale.year;
                            // 해당 월
                            let dateMonth = sale.month;
                            // 월 매출 총합
                            let monthTotalSales = sale.monthTotalSales;
                            // 월 매출 건수
                            let monthTotal = sale.monthSalesNumber;
                            // 주간 매출 총합
                            let weekTotalSales = sale.weekTotalSales;
                            // 몇 주차인지 판단
                            let weekDate = sale.weekDate;
                            // 주간 매출 건수
                            let weekTotal = sale.weekSalesNumber;
                            // 이벤트 추가 로직
                            calendar.addEvent({
                                title: sale.dailyTotalSales + '원',
                                description: sale.dailySalesNumber + '건',
                                start: sale.salesDate,
                                color: 'white',
                                textColor: 'black',
                            });
                            if (!totalPrice[sale.salesDate]) {
                                totalPrice[sale.salesDate] = sale.dailyTotalSales;
                            } else {
                                totalPrice[sale.salesDate] += sale.dailyTotalSales;
                            }

                            console.log(sale.salesDate, totalPrice[sale.salesDate]);
                            // 주별 매출
                            if (!maxWeeklyData[sale.weekDate]) {
                                maxWeeklyData[sale.weekDate] = {
                                    weekTotalSales: sale.weekTotalSales,
                                    weekTotal: sale.weekSalesNumber
                                };
                                // console.log('maxWeeklyData', maxWeeklyData)
                            } else {

                                if (year == sale.year && month == sale.month) {
                                    // 이미 초기화된 주에 대해서는 최대값을 비교하여 업데이트
                                    // console.log('maxWeeklyData[sale.weekDate].weekTotalSales', maxWeeklyData[sale.weekDate].weekTotalSales)
                                    if (sale.weekTotalSales > maxWeeklyData[sale.weekDate].weekTotalSales) {
                                        maxWeeklyData[sale.weekDate].weekTotalSales = sale.weekTotalSales;
                                    }
                                    if (sale.weekSalesNumber > maxWeeklyData[sale.weekDate].weekTotal) {
                                        maxWeeklyData[sale.weekDate].weekTotal = sale.weekSalesNumber;
                                    }
                                    // console.log('maxWeeklyData', maxWeeklyData)
                                }
                            }
                            // 월 매출
                            if (saleKey === monthDataKey) {
                                if (!maxMonthData[saleKey]) {
                                    maxMonthData[saleKey] = {
                                        monthTotalSales: sale.monthTotalSales,
                                        monthTotal: sale.monthSalesNumber
                                    };
                                } else {
                                    maxMonthData[saleKey].monthTotalSales = Math.max(maxMonthData[saleKey].monthTotalSales, sale.monthTotalSales);
                                    maxMonthData[saleKey].monthTotal = Math.max(maxMonthData[saleKey].monthTotal, sale.monthSalesNumber);
                                }
                            }
                            resolve();
                        });
                        // 주간 총 매출
                        Object.keys(maxWeeklyData).forEach(weekDate => {
                            const weekData = maxWeeklyData[weekDate];
                            addTotalHeader(weekDate, weekData.weekTotalSales, weekData.weekTotal);
                        });
                        // 월별 최대 매출 데이터를 addMonthTotal에 전달
                        if (maxMonthData[monthDataKey]) {
                            addMonthTotal(maxMonthData[monthDataKey].monthTotalSales, maxMonthData[monthDataKey].monthTotal);
                        }
                        // console.log(globalSalesData);
                    }
                } catch (error) {
                    console.error('오류루:', error);
                }
            });
        }
    });
</script>
<style>
    #calendar > * {
        font-family: "Pretendard-Regular";
    }
</style>

<div class="store-management-area cnt-area w100per">
    <div class="title-box flex aic jcsb">
        <h1 class="tb c121619">매출 관리</h1>
    </div>
    <div class="cnt-box bsb pt20 pr28 pb20 pl28 bfff bdr12">
        <div class="calender-box">
            <div id="calendar" class="calendar-custom"></div>
        </div>
    </div>
</div>

<div class="modal-area wh100per fixed zi9" class:active="{isActive}">
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w800" class:active="{isActiveRecord}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">매출 관리</h3>
            <button class="x-btn img-box abs" on:click="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <div class="flex aic jcsb">
<!--                <div class="select-type-4 w100 f14 rel">-->
<!--                    <select name="account">-->
<!--                        <option value="">전체</option>-->
<!--                        <option value="">판매</option>-->
<!--                        <option value="">구매</option>-->
<!--                    </select>-->
<!--                    <span class="arrow img-box abs y-middle">-->
<!--                        <img src="/img/arrow_bottom_A2A9B0.svg" alt=""/>-->
<!--                    </span>-->
<!--                </div>-->
<!--                <div class="flex aic g8">-->
<!--                    <div class="input-type-2 f14 w200">-->
<!--                        <input type="date" placeholder="조회">-->
<!--                    </div>-->
<!--                    <span class="f14">~</span>-->
<!--                    <div class="input-type-2 f14 w200">-->
<!--                        <input type="date" placeholder="조회">-->
<!--                    </div>-->
<!--                    <button class="btn-type-1 w60 h36 f14 bdr4 b333 cfff">조회</button>-->
<!--                </div>-->
            </div>
            <div class="line w100per h1 bf2f2f2 mt20 mb20"></div>
            <h1 class="f14 c777 tm">{currentdate}</h1>
            <div class="table-type-2 mt12">
                <table>
                    <thead>
                    <tr>
                        <th colspan="4">판매 리스트</th>
                    </tr>
                    </thead>
                    <tbody>
                    {#each formdata as purchaseList}
                        <tr>
                            <td class="w120">{currentdate}</td>
                            <td class="w60">
                                <span class="inblock cb">판매</span>
                            </td>
                            <td class="wsn tal">
                                {purchaseList.purchaseStocks[0].itemName}
                                {#if purchaseList.purchaseStocks.length > 1}
                                    외 {purchaseList.purchaseStocks.length - 1}건
                                {/if}
                            </td>
                            <td>{purchaseList.allPrice}</td>
                        </tr>
                    {/each}
                    </tbody>
                    <tbody>
                    <tr>
                        <td colspan="4"><span class="tb">총 금액 {totalPrice[currentdate]}</span></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>