<script>
    export let data;
    import { onMount } from 'svelte';
    import {goto, replaceState} from "$app/navigation";
    import {page} from "$app/stores";
    let isChecked = true;
    let questionId = null;
    let currentPage = 0;
    let createSubject = '';
    let createContent = '';
    let subject = '';
    let content = '';
    let allSelectCheckboxState = false;
    let questionCheckboxStates = Array(data.data.questions.content.length).fill(allSelectCheckboxState);

    onMount(() => {
        dataLoad();
	});

    async function fetchData(question) {
        try {
            if (question.isPublic) {
                isChecked = true;
            } else {
                isChecked = false;
            }
            questionId = question.id;

            const response = await fetch(`http://localhost:8080/api/v1/questions/admin/${questionId}`);
            const data = await response.json();
            subject = data.data.question.subject;
            content = data.data.question.content;

        } catch (error) {
            console.error("Error fetching data:", error);
        }
    }

    let isActive = false;
    let isActiveAdd = false;
    let isActiveModifi = false;

    const submitAddModal = async () => {
		const confirmed = window.confirm('등록하시겠습니까?');

		if (confirmed) {
            if (createSubject.trim() === "" || createContent.trim() === "") {
                window.alert('필수 입력 항목이 비어 있습니다.');
                console.error("필수 입력 항목이 비어 있습니다.");
                return;
            }
            const createData = {
                subject: createSubject,
                content: createContent,
                isPublic: isChecked
            };

            try {
                const response = await fetch('http://localhost:8080/api/v1/questions/admin', {
                    method: 'POST',
                    credentials: 'include',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(createData)
                });
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }

                const result = await response.json();
                console.log('Success:', result);

                data.data.questions.content.unshift(result.data.question);
                data.data.questions.content = data.data.questions.content.slice(0, 10);

                window.alert('등록이 완료되었습니다!');
                fetchLatestPosts();
                deactivateModal();
                // navigateToFirstPage();
            } catch (error) {
                console.error('Error:', error);
                window.alert('등록에 실패하였습니다');
            }
        }
    }

    function submitModifyModal() {
        if (subject.trim() === "" || content.trim() === "") {
            window.alert('필수 입력 항목이 비어 있습니다.');
            console.error("필수 입력 항목이 비어 있습니다.");
            return;
        }
        const updatedData = {
            subject: subject,
            content: content,
            isPublic: isChecked
        };

        fetch(`http://localhost:8080/api/v1/questions/admin/${questionId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(result => {
            console.log('Success:', result);

            const updatedQuestionIndex = data.data.questions.content.findIndex(q => q.id === questionId);
            if (updatedQuestionIndex !== -1) {
                data.data.questions.content[updatedQuestionIndex].subject = result.data.question.subject;
                data.data.questions.content[updatedQuestionIndex].content = result.data.question.content;
                data.data.questions.content[updatedQuestionIndex].isPublic = result.data.question.isPublic;
            }

            window.alert('수정이 완료되었습니다!');
            deactivateModal();
        })
        .catch(error => {
            console.error('Error:', error);
            window.alert('수정에 실패하였습니다');
        });
    }

    async function deleteChecked() {
        const selectedQuestionIds = data.data.questions.content
        .filter((question, i) => questionCheckboxStates[i])
        .map(question => question.id);
        console.log('Selected Question IDs:', selectedQuestionIds);

        const confirmed = window.confirm('삭제하시겠습니까?');

		if (confirmed) {
        try {
        const response = await fetch('http://localhost:8080/api/v1/questions/admin/delete', {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json',
            },
            body: JSON.stringify({ questionIds: selectedQuestionIds }),
        });

        const result = await response.json();
        const responseData = result.data;
        if (responseData) {
            data.data.questions.content = data.data.questions.content.filter((question, i) => !questionCheckboxStates[i]);
            questionCheckboxStates = Array(data.data.questions.content.length).fill(allSelectCheckboxState);

            await fetchLatestPosts();
            // if (data.data.questions.content.length === 0) {
            //     navigateToFirstPage();
            // }
            window.alert('삭제되었습니다');
        } else {
            window.alert('삭제에 실패하였습니다');
            console.error('Failed to delete questions');
        }
        } catch (error) {
        console.error('Error:', error);
        // 예상치 못한 오류 처리
        }
    }
}


    const navigateToFirstPage = () => {
        const location = 'http://localhost:5173/admin/using/qna?page=0'
        goto(location);
    }

    function activateModalAdd() {
        isChecked = true;
        createSubject = '';
        createContent = '';
        isActive = true;
        isActiveAdd = true;
    }

    function activateModalModifi(question) {
        fetchData(question);
        isActive = true;
        isActiveModifi = true;
    }

    function deactivateModal() {
        isActive = false;
        isActiveAdd = false;
        isActiveModifi = false;
    }
    function updateAllSelectCheckbox() {
        questionCheckboxStates = Array(data.data.questions.content.length).fill(allSelectCheckboxState);
    }
    function updateIndividualQuestionCheckbox(index) {
        allSelectCheckboxState = questionCheckboxStates.every(state => state);
    }
    async function fetchLatestPosts() {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/questions/admin?page=${currentPage}`); 
            const result = await response.json();

            data.data.questions.content = result.data.questions.content;
            data.data.questions.totalElements = result.data.questions.totalElements;
            data.data.questions.number = result.data.questions.number;
            data.data.questions.totalPages = result.data.questions.totalPages;
        } catch (error) {
            console.error('Error fetching latest posts:', error);
        }
    }
    function generatePageButtons(totalPages) {
            const buttons = [];
            for (let i = 0; i < totalPages; i++) {
                buttons.push(i + 1);
            }
            return buttons;
        }

        async function changePage(currentPage) {
        try {
            $page.url.searchParams.set('page', currentPage);

            await goto(`?${$page.url.searchParams.toString()}`, { replaceState });
            await dataLoad();
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    }

    async function dataLoad() {
        const queryString = window.location.search;

        const res = await fetch(`http://localhost:8080/api/v1/questions/admin${queryString}`, {
            credentials: 'include'
        });

        data = await res.json();
    }

</script>

<div class="modal-area-1 modal-area wh100per fixed zi9" class:active="{isActive}">

    <!-- 질문 등록 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w600" class:active="{isActiveAdd}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">질문 등록</h3>
            <button class="x-btn img-box abs" on:click="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <div class="flex fdc g36">
                <div>
                    <h2 class="c333 f15 tm mb8">제목<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="제목" bind:value="{createSubject}">
                    </div>
                    {#if createSubject.trim() === ""}
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    {/if}
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">내용<span class="cr f16 tm inblock">*</span></h2>
                    <div class="textarea-type-1 f14 w100per h200">
                        <textarea placeholder="내용" bind:value="{createContent}"></textarea>
                    </div>
                    {#if createContent.trim() === ""}
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    {/if}
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">공개 여부<span class="cr f16 tm inblock">*</span></h2>
                    <div class="check-type-1">
                        <input type="checkbox" id="w1" bind:checked="{isChecked}">
                        <label for="w1"></label>
                    </div>
                </div>
            </div>
            <div class="btn-area flex aic jcc g8 mt40">
                <button class="w120 h40 btn-type-2 bdr4 bm cfff tm f14" on:click="{submitAddModal}">등록</button>
                <button class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" on:click="{deactivateModal}">취소</button>
            </div>
        </div>
    </div>
    

    <!-- 질문 수정 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w600" class:active="{isActiveModifi}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">질문 수정</h3>
            <button class="x-btn img-box abs" on:click="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <div class="flex fdc g36">
                <div>
                    <h2 class="c333 f15 tm mb8">제목<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="제목" bind:value={subject}>
                    </div>
                    <div class="error-text-box">
                        {#if subject.trim() === ""}
                            <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        {/if}
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">내용<span class="cr f16 tm inblock">*</span></h2>
                    <div class="textarea-type-1 f14 w100per h200">
                        <textarea placeholder="내용" bind:value={content}></textarea>
                    </div>
                    <div class="error-text-box">
                        {#if content.trim() === ""}
                            <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        {/if}
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">공개 여부<span class="cr f16 tm inblock">*</span></h2>
                    <div class="check-type-1">
                        <input type="checkbox" id="w1" bind:checked={isChecked}>
                        <label for="w1"></label>
                    </div>
                </div>
            </div>
            <div class="btn-area flex aic jcc g8 mt40">
                <button class="w120 h40 btn-type-2 bdr4 bm cfff tm f14" on:click="{submitModifyModal}">수정</button>
                <button class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" on:click="{deactivateModal}">취소</button>
            </div>
        </div>
    </div>

</div>

<div class="store-management-area cnt-area w100per">
    <div class="title-box flex aic jcsb">
        <h1 class="tb c121619">질문 관리</h1>
    </div>
    <div class="cnt-box-1 cnt-box">
        <div class="middle-area">
            <div class="all-text c121619 f14">
                전체 <span class="number inblock cm tm">{data.data.questions.totalElements}</span>건
            </div>
            <div class="table-box-1 table-type-1 scr-type-2 mt12">
                <table>
                    <thead>
                        <tr>
                            <th class="wsn" style="width: 44px;">
                                <div class="check-type-1">
                                    <input type="checkbox" id="all" bind:checked={allSelectCheckboxState} on:change={updateAllSelectCheckbox}>
                                    <label for="all"></label>
                                </div> 
                            </th>
                            <th class="wsn">제목</th>
                            <th class="wsn">내용</th>
                            <th class="wsn">공개 여부</th>
                            <th class="wsn">수정</th>
                        </tr>
                    </thead>
                    {#each data.data.questions.content as question, i}
                    <tbody>
                        <tr>
                            <td class="wsn" style="width: 44px;">   
                                <div class="check-type-1">
                                <input type="checkbox" id={`v1-${question.id}`} bind:checked={questionCheckboxStates[i]} on:change={() => updateIndividualQuestionCheckbox(i)}>
                                <label for={`v1-${question.id}`}></label>
                                </div> 
                            </td>
                            <td class="wsn tal">{question.subject}</td>
                            <td class="tal lh140">
                                {question.content}
                            </td>
                            <td class="wsn">
                                {#if !question.isPublic}
                                <span class="cr">비공개</span>
                                {/if}
                                {#if question.isPublic}
                                <span class="cg">공개</span>
                                {/if}
                            </td>
                            <td class="wsn tac">
                                <button class="w40 h24 btn-type-2 bdr4 bdbbb cbbb f13" on:click={() => activateModalModifi(question)}>수정</button>
                            </td>
                        </tr>
                    </tbody>
                    {/each}
                </table>
            </div>
            <div class="flex aic jcsb mt8">
                <div class="flex aic g4">
                    <button class="w50 h30  btn-type-1 bdA2A9B0 bdr4 f12 cA2A9B0" on:click="{deleteChecked}">삭제</button>
                </div>
                <div class="flex aic g4">
                    <button class="w50 h30 btn-type-1 bm bdr4 f12 cfff" on:click="{activateModalAdd}">등록</button>
                </div>
            </div>
            <div class="paging-box flex jcc mt40">
                <ul class="flex aic jcc">
                    {#if data.data.questions.number > 0}
                        <!-- svelte-ignore a11y-click-events-have-key-events -->
                        <!-- svelte-ignore a11y-no-noninteractive-element-interactions -->
                        <li class="page-btn" on:click={() => changePage(data.data.questions.number - 1)}>
                            <a href="">이전</a>
                        </li>
                    {/if}
                    {#each generatePageButtons(data.data.questions.totalPages) as button}
                        <!-- svelte-ignore a11y-click-events-have-key-events -->
                        <!-- svelte-ignore a11y-no-noninteractive-element-interactions -->
                        <li
                            class="num"
                            on:click={() => data.data.questions.number !== button - 1 && changePage(button - 1)}
                        >
                            <a href="" class:active={data.data.questions.number === button - 1}>{button}</a>
                        </li>
                    {/each}
                    {#if data.data.questions.number < data.data.questions.totalPages - 1}
                        <!-- svelte-ignore a11y-click-events-have-key-events -->
                        <!-- svelte-ignore a11y-no-noninteractive-element-interactions -->
                        <li class="page-btn" on:click={() => changePage(data.data.questions.number + 1)}>
                            <a href="">다음</a>
                        </li>
                    {/if}
                </ul>
            </div>
        </div>
    </div>
</div>