<script>
    import './Calendar.css';
    import { onMount, afterUpdate } from 'svelte';
    import { Calendar } from '@fullcalendar/core';
    import interactionPlugin from '@fullcalendar/interaction';
    import dayGridPlugin from '@fullcalendar/daygrid';

    let calendar;
    let isDeleteEnabled = false;
    let isModifyEnabled = false;
    let events = [];

    async function fetchDataAndRenderCalendar() {
        try {
            const response = await fetch('http://localhost:8080/api/v1/schedules');
            const data = await response.json();
            events = data.data.schedules.map(event => ({
                eventId: event.id,
                title: event.subject,
                content: event.content,
                start: new Date(event.startDate),
                end: new Date(event.endDate)
            }));

            renderCalendar();
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    }

    function renderCalendar() {
        const calendarEl = document.getElementById('calendar');

        calendar = new Calendar(calendarEl, {
            plugins: [interactionPlugin, dayGridPlugin],
            initialView: 'dayGridMonth',
            selectable: false,
            events: events,
            dayMaxEventRows: true,
            views: {
                dayGrid: {
                dayMaxEventRows: 4
                }
            },
            eventContent: function(arg) {
            return {
                html: `<div><strong>${arg.event.title}</strong> - ${arg.event.extendedProps.content}</div>`,
                };
            },
            select: handleDateSelect,
            eventClick: handleEventClick,
            footerToolbar: {
                start: 'custom1',
                end: 'custom2,custom3'
            },
            customButtons: {
                custom1: {
                    text: '일정 등록',
                    click: handleEnableDateSelect
                },
                custom2: {
                    text: '일정 수정',
                    click: handleEnableModify,
                },
                custom3: {
                    text: '일정 삭제',
                    click: handleEnableDelete,
                }
            }
        });

        calendar.render();
    }

    function handleEnableDateSelect() {
        calendar.setOption('selectable', true);
        isDeleteEnabled = false;
        window.alert('등록할 일정의 기간을 드래그하여 선택하세요');
    }
    function handleEnableDelete() {
        calendar.setOption('selectable', false);
        isDeleteEnabled = true;
        window.alert('삭제할 일정을 클릭해주세요');
    }
    function handleEnableModify() {
        calendar.setOption('selectable', false);
        isModifyEnabled = true;
        window.alert('수정할 일정을 클릭해주세요');
    }
    function handleDateSelect(info) {
        if (!isDeleteEnabled) {
            const title = window.prompt('Enter the event title:');
            const content = window.prompt('Enter the event content:');

            if(title == '') {
                window.alert('제목을 기입해주세요');
                return;
            }
            if(content == '') {
                window.alert('내용을 기입해주세요');
                return;
            }

            if (title && content) {
                const event = { title, content, start: info.startStr, end: info.endStr };

                calendar.addEvent(event);

                const updateStartDate = new Date(info.startStr);
                const updateEndDate = new Date(info.endStr);

                updateStartDate.setDate(updateStartDate.getDate());
                updateEndDate.setDate(updateEndDate.getDate() - 1);


                fetch('http://localhost:8080/api/v1/schedules', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        subject: title,
                        content,
                        startDate: updateStartDate,
                        endDate: updateEndDate
                    }),
                })
                .then(response => response.json())
                .then(data => {
                    console.log('Event saved:', data);
                    window.alert('일정이 등록되었습니다');
                })
                .catch(error => {
                    console.error('Error saving event:', error);
                });

                calendar.setOption('selectable', false);
            } else {
                window.alert("일정 등록을 취소하였습니다");
                calendar.setOption('selectable', false);
            }
        }
    }

    onMount(() => {
        fetchDataAndRenderCalendar();
    });

    afterUpdate(() => {
        fetchDataAndRenderCalendar();
    });
    function handleEventClick(info) {
    if (isDeleteEnabled) {
        const eventTitle = info.event.title;
        const eventId = info.event.extendedProps.eventId;

        const confirmation = window.confirm(`이벤트 "${eventTitle}"를 삭제하시겠습니까?`);
        isDeleteEnabled = false;

        if (confirmation) {
            fetch(`http://localhost:8080/api/v1/schedules/${eventId}`, {
                method: 'DELETE',
            })
            .then(response => response.json())
            .then(data => {
                console.log('Event deleted:', data);
            })
            .catch(error => {
                console.error('Error deleting event:', error);
            });

            info.event.remove();
        }
    } else if (isModifyEnabled) {
        const eventTitle = info.event.title;
        const eventId = info.event.extendedProps.eventId;

        const confirmation = window.confirm(`이벤트 "${eventTitle}"를 수정하시겠습니까?`);
        isModifyEnabled = false;
        console.log(info);

        if (confirmation) {
            console.log(info.event.end);
            const formattedStartDate = formatDate(info.event.start);
            let formattedEndDate = null;

            if (info.event.end == null) {
                formattedEndDate = formatDate(info.event.start);
            } else {
                formattedEndDate = formatDate(info.event.end);
            }
            const newTitle = window.prompt('제목을 수정하세요:', eventTitle);
            const newContent = window.prompt('내용을 수정하세요:', info.event.extendedProps.content);
            const startDateString = window.prompt('시작날짜를 수정하세요 (yyyy-mm-dd):', formattedStartDate);
            const endDateString = window.prompt('종료날짜를 수정하세요 (yyyy-mm-dd):', formattedEndDate);

            if(newTitle == '') {
                window.alert('제목을 입력해주세요');
                return;
            }
            if(newContent == '') {
                window.alert('내용을 입력해주세요');
                return;
            }
            if(startDateString > endDateString) {
                window.alert('올바른 날짜를 입력하세요');
                return;
            }

            const dateRegex = /^\d{4}-\d{2}-\d{2}$/;
            if (!dateRegex.test(startDateString) || !dateRegex.test(endDateString)) {
                window.alert('올바른 날짜 형식을 입력하세요 (yyyy-mm-dd)');
                return;
            }
            const startDate = new Date(startDateString);
            const endDate = new Date(endDateString);

            // 각 날짜에 1일씩 추가
            startDate.setDate(startDate.getDate());
            endDate.setDate(endDate.getDate());

            if (newTitle !== null && newContent !== null) {
                fetch(`http://localhost:8080/api/v1/schedules/${eventId}`, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        subject: newTitle,
                        content: newContent,
                        startDate: startDateString,
                        endDate: endDateString,
                    }),
                })
                .then(response => response.json())
                .then(data => {
                    console.log('Event updated:', data);
                })
                .catch(error => {
                    console.error('Error updating event:', error);
                });

                info.event.setProp('title', newTitle);
                info.event.setExtendedProp('content', newContent);
                info.event.setStart(startDate);
                info.event.setEnd(endDate);
            }
        }
    }
}
    function formatDate(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');

        return `${year}-${month}-${day}`;
    }
</script>

<!--<style>-->
<!--    #calendar {-->
<!--        width: 100%;-->
<!--        margin-top: 20px;-->
<!--        font-size: 16px;-->
<!--        overflow: hidden;-->
<!--    }-->
<!--    /* :global(.fc) {-->
<!--        border: 1px solid #ffffff;-->
<!--        border-radius: 8px;-->
<!--        font-size: 16px;-->
<!--        background-color: #fff;-->
<!--    } */-->

<!--    :global(.fc-event) {-->
<!--        border-radius: 5px;-->
<!--        margin: 2px;-->
<!--        padding-left: 10px;-->
<!--        padding-top: 3px;-->
<!--        padding-bottom: 3px;-->
<!--        box-shadow: 0 2px 2px rgba(0, 0, 0, 0.1);-->
<!--    }-->
<!--    :global(.fc-day) {-->
<!--        background-color: #ffffff;-->
<!--    }-->
<!--    :global(.fc .fc-daygrid-day-frame) {-->
<!--        /*height: 16vh;*/-->
<!--    }-->
<!--    :global(.fc-day-today) {-->
<!--        background-color: #f5f8ff !important;-->
<!--        border-radius: 8px !important;-->
<!--    }-->
<!--    #calendar > * {-->
<!--        font-family: "Pretendard-Regular";-->
<!--    }-->
<!--</style>-->

<div class="store-management-area cnt-area w100per">
    <div class="title-box flex aic jcsb">
        <h1 class="tb c121619">캘린더</h1>
    </div>
    <div class="cnt-box bsb pt20 pr28 pb20 pl28 bfff bdr12">
        <div class="calender-box">
            <div id="calendar"></div>
        </div>
    </div>
</div>
