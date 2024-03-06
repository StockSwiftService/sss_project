<script>
    import './Calendar.css';
    import { onMount, afterUpdate } from 'svelte';
    import { Calendar } from '@fullcalendar/core';
    import interactionPlugin from '@fullcalendar/interaction';
    import dayGridPlugin from '@fullcalendar/daygrid';
    import tippy from 'tippy.js';
    import 'tippy.js/dist/tippy.css';

    let calendar;
    let isDeleteEnabled = false;
    let isModifyEnabled = false;
    let events = [];
    let loggedInUserId = null;
    
    onMount(async() => {
		try {
			const response = await fetch('http://localhost:8080/api/v1/member/loginUser', {
				method: 'GET',
				headers: {
					'Content-Type': 'application/json'
				},
				credentials: 'include'
			});
			if (response.ok) {
				const data = await response.json();
                loggedInUserId = data.data.member.id;
			} else {
				console.error('서버 응답 오류:', response.statusText);
				if (!response.ok && response.status != 401) {
					alert('다시 시도 해주세요.');
				}
			}
		} catch (error) {
			console.error('오류 발생:', error);
			alert('다시 시도 해주세요.');
		}
        fetchDataAndRenderCalendar(loggedInUserId);
	});

    async function fetchDataAndRenderCalendar(loggedInUserId) {
        try {
            const response = await fetch('http://localhost:8080/api/v1/schedules', {
                credentials: 'include'
            });
            const data = await response.json();
            events = data.data.schedules.map(event => ({
                eventId: event.id,
                title: event.subject,
                content: event.content,
                start: new Date(event.startDate),
                end: new Date(event.endDate),
                color: event.member.id === loggedInUserId ? '#8fdf82' : '#42cef5',
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
            eventDidMount: handleEventDidMount,
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
    
    async function handleEventDidMount(info) {
        const startDate = new Date(info.event.start);
        const endDate = new Date(info.event.end);

        const formattedStartDate = new Intl.DateTimeFormat('en-US', {
        month: '2-digit',
        day: '2-digit',
        }).format(startDate);

        const formattedEndDate = new Intl.DateTimeFormat('en-US', {
        month: '2-digit',
        day: '2-digit',
        }).format(endDate);

        if(formattedEndDate == '01/01') {
            tippy(info.el, {
            content: '[' + formattedStartDate + ' ~ ' + formattedStartDate + '] ' + info.event.title + ' - ' + info.event.extendedProps.content,
            theme: 'light',
            });
        } else {
            tippy(info.el, {
            content: '[' + formattedStartDate + ' ~ ' + formattedEndDate + '] ' + info.event.title + ' - ' + info.event.extendedProps.content,
            theme: 'light',
            });
        }
    }


    function handleEnableDateSelect() {
        calendar.setOption('selectable', true);
        isDeleteEnabled = false;
        window.alert('등록할 일정의 기간을 드래그하여 선택하세요');
    }
    function handleEnableDelete() {
        fetchDataAndRenderCalendar(loggedInUserId);
        calendar.setOption('selectable', false);
        isDeleteEnabled = true;
        window.alert('삭제할 일정을 클릭해주세요');
    }
    function handleEnableModify() {
        fetchDataAndRenderCalendar(loggedInUserId);
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
                let event = [];

                const updateStartDate = new Date(info.startStr);
                const updateEndDate = new Date(info.endStr);

                updateStartDate.setDate(updateStartDate.getDate());
                updateEndDate.setDate(updateEndDate.getDate() - 1);

                if(updateStartDate.getDate() === updateEndDate.getDate()){
                    event = { title, content, start: info.startStr, end: info.endStr, memberId: loggedInUserId, 
                        backgroundColor: 'inherit', textColor: 'black' , borderColor: 'white' };
                } else {
                    event = { title, content, start: info.startStr, end: info.endStr, memberId: loggedInUserId, backgroundColor: '#8fdf82', borderColor: '#8fdf82' };
                }
                calendar.addEvent(event);

                fetch('http://localhost:8080/api/v1/schedules', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    credentials: 'include',
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

    async function handleEventClick(info) {
        console.log('아이디:' + info.event.extendedProps.eventId);

    if (isDeleteEnabled) {
        const eventTitle = info.event.title;
        const eventId = info.event.extendedProps.eventId;

        const confirmation = window.confirm(`이벤트 "${eventTitle}"를 삭제하시겠습니까?`);

        try {
            console.log('로그인아이디:' + loggedInUserId);
            const authorization = await checkMemberAuthorization(info);

            console.log("정보:" + authorization);
            if (!authorization) {
                window.alert('회원 정보가 일치하지 않아 삭제할 수 없습니다.');
                return;
            }
            if (confirmation) {
            fetch(`http://localhost:8080/api/v1/schedules/${eventId}`, {
                credentials: 'include',
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
        } else {
            isDeleteEnabled = false;
            window.alert('일정 삭제를 취소하였습니다');
        }
        
        isDeleteEnabled = false;
    } catch (error) {
            console.error('Error handling event click:', error);
        }
    } else if (isModifyEnabled) {
        const eventTitle = info.event.title;
        const eventId = info.event.extendedProps.eventId;

        const confirmation = window.confirm(`이벤트 "${eventTitle}"를 수정하시겠습니까?`);

        try {
            const authorization = await checkMemberAuthorization(info);

            console.log("정보:" + authorization);
            if (!authorization) {
                window.alert('회원 정보가 일치하지 않아 수정할 수 없습니다.');
                return;
            }

            if (confirmation) {
                console.log('event:' + info.event); 
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

            startDate.setDate(startDate.getDate());
            endDate.setDate(endDate.getDate());

            if (newTitle !== null && newContent !== null) {
                fetch(`http://localhost:8080/api/v1/schedules/${eventId}`, {
                    method: 'PATCH',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    credentials: 'include',
                    body: JSON.stringify({
                        subject: newTitle,
                        content: newContent,
                        startDate: startDateString,
                        endDate: endDateString,
                    }),
                })
                .then(response => response.json())
                .then(data => {
                    fetchDataAndRenderCalendar(loggedInUserId);
                    console.log('Event updated:', data);
                })
                .catch(error => {
                    console.error('Error updating event:', error);
                });
                
                info.event.setProp('title', newTitle);
                info.event.setExtendedProp('content', newContent);
                info.event.setStart(startDate);
                info.event.setEnd(endDate);

            } else {
                window.alert('수정을 취소하였습니다');
                isModifyEnabled = false;
            }
            isModifyEnabled = false;
        }
            } catch (error) {
                console.error('Error handling event click:', error);
            }
    }
}
    function formatDate(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');

        return `${year}-${month}-${day}`;
    }
    function checkMemberAuthorization(info) {
    const eventId = info.event.extendedProps.eventId;

    return fetch(`http://localhost:8080/api/v1/schedules/check/${eventId}`, {
        credentials: 'include'
        })
        .then(response => response.json())
        .then(data => {
            console.log(data.data.isConfirm);
            if (data.data.isConfirm) {
                return true;
            } else {
                return false;
            }
        })
        .catch(error => {
            console.error('Error checking member authorization:', error);
        });
    }
</script>

<!-- <style>
    #calendar {
        width: 100%;
        margin-top: 20px;
        font-size: 16px;
    }

    :global(.fc-event) {
        border-radius: 5px;
        margin: 2px;
        padding-left: 10px;
        padding-top: 3px;
        padding-bottom: 3px;
        box-shadow: 0 2px 2px rgba(0, 0, 0, 0.1);
        overflow: hidden;
        white-space: nowrap !important;
        text-overflow: ellipsis !important;
    }


    :global(.fc-day, .fc-today) {
        background-color: #fff;
    }

</style> -->



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
