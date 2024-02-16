<script>
    import { onMount } from 'svelte';
    import { Calendar } from '@fullcalendar/core';
    import interactionPlugin from '@fullcalendar/interaction'; // for selectable
    import dayGridPlugin from '@fullcalendar/daygrid'; // for dayGridMonth view
  
    let calendar;
  
    onMount(() => {
      const calendarEl = document.getElementById('calendar');
  
      calendar = new Calendar(calendarEl, {
        plugins: [interactionPlugin, dayGridPlugin],
        initialView: 'dayGridMonth',
        selectable: true,
        select: handleDateSelect,
        events: [
          { title: 'Event 1', date: '2022-02-01' },
          { title: 'Event 2', date: '2022-02-15' },
        ],
      });
  
      calendar.render();
    });
  
    // Handle date select
    function handleDateSelect(info) {
      const userInput = window.prompt('Enter your event:');
      if (userInput) {
        // Use userInput as the event title
        calendar.addEvent({ title: userInput, start: info.startStr, end: info.endStr });
      }
    }
</script>
  
<style>
#calendar {
    width: 100%;
    margin-top: 20px;
}
</style>

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