<script>
    export let data;
    import {goto, replaceState} from "$app/navigation";
    import {page} from "$app/stores";
    import {onMount} from "svelte";

    let email = '';
    let subject = '';
    let content = '';
    let searchQuery = '';
    let currentPage = 0;

    let isActive = false;
    let isActiveAdd = false;

    function activateModalAdd() {
        email = '';
        subject = '';
        content = '';
        isActive = true;
        isActiveAdd = true;
    }

    function deactivateModal() {
        isActive = false;
        isActiveAdd = false;
    }

    let activeStates = [false, false];

    function toggle(index) {
        activeStates[index] = !activeStates[index];
    }

    const performSearch = async () => {

    $page.url.searchParams.set('kw', searchQuery);
    $page.url.searchParams.set('page', currentPage);

    await goto(`?${$page.url.searchParams.toString()}`, {replaceState});

    await dataLoad();

    }

    function handleKeyPress(event) {
    if (event.key === 'Enter') {
        performSearch();
    }
    }

    onMount(async () => {
    await dataLoad();
    const unsubscribe = page.subscribe(async ($page) => {
        searchQuery = $page.url.searchParams.get('kw') || '';
            await dataLoad();
        });
        // 컴포넌트가 언마운트될 때 구독 해제
        return () => {
            unsubscribe();
        };
    })

    async function dataLoad() {
    const queryString = window.location.search;

    const res = await fetch(`http://localhost:8080/api/v1/questions${queryString}`, {
        credentials: 'include'
    })
    data = await res.json();
    }

    function generatePageButtons(totalPages) {
        const buttons = [];
        for (let i = 0; i < totalPages; i++) {
            buttons.push(i + 1);
        }
        return buttons;
    }

    async function changePage(searchQuery,currentPage) {
        try {

            $page.url.searchParams.get('kw', searchQuery);
            $page.url.searchParams.set('page', currentPage);

            await goto(`?${$page.url.searchParams.toString()}`, {replaceState});
            await dataLoad();

        } catch (error) {
            console.error('Error fetching data:', error);
        }
    }
    function submitContact() {
        if (email.trim() === "" || subject.trim() === "" || content.trim() === "") {
            window.alert('필수 입력 항목이 비어 있습니다.');
            console.error("필수 입력 항목이 비어 있습니다.");
            return;
        }
        if (!isValidEmail(email)) {
            window.alert('이메일 형식이 유효하지 않습니다');
            return;
        }
        const createData = {
            email: email,
            subject: subject,
            content: content
        };

        fetch('http://localhost:8080/api/v1/questions/contact', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(createData)
        })
        .then(result => {
            console.log('Success:', result);

            window.alert('문의가 완료되었습니다!');
            deactivateModal();
        })
        .catch(error => {
            console.error('Error:', error);
            window.alert('문의에 실패하였습니다');
        });
    }
    function isValidEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

</script>

<style>
    .qna-box > li .q {
        box-sizing: border-box;
        padding: 16px 12px;
        font-size: 16px;
        border-bottom: 1px solid #DDE1E6;
        color: #373B3E;
        cursor: pointer;
    }
    .qna-box > li .q.active {
        color: #2656F6;
    }
    .qna-box > li .a {
        box-sizing: border-box;
        padding: 16px 12px;
        font-size: 14px;
        border-bottom: 1px solid #DDE1E6;
        color: #373B3E;
        display: none;
    }
    .qna-box > li .a.active {
        display: block;
    }
</style>

<div class="modal-area-1 modal-area wh100per fixed zi9" class:active="{isActive}">

    <!-- 1대1 문의하기 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w480" class:active="{isActiveAdd}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">1대1 문의하기</h3>
            <button class="x-btn img-box abs" on:click="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <div class="flex fdc g36">
                <div>
                    <h2 class="c333 f15 tm mb8">이메일<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="email" placeholder="이메일" bind:value={email}>
                    </div>
                    <div class="error-text-box">
                        {#if email.trim() === ""}
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        {:else if !isValidEmail(email)}
                        <span class="f13 mt8 cr">올바른 이메일 형식이 아닙니다.</span>
                        {/if}
                    </div>
                </div>
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
                    <div class="textarea-type-1 f14 w100per h160">
                        <textarea placeholder="내용" bind:value={content}></textarea>
                    </div>
                    <div class="error-text-box">
                        {#if content.trim() === ""}
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        {/if}
                    </div>
                </div>
            </div>
            <div class="btn-area flex aic jcc g8 mt40">
                <button class="w120 h40 btn-type-2 bdr4 bm cfff tm f14" on:click="{submitContact}">전송</button>
                <button class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" on:click="{deactivateModal}">취소</button>
            </div>
        </div>
    </div>

</div>

<div class="store-management-area cnt-area w100per">
    <div class="title-box flex aic jcsb">
        <h1 class="tb c121619">자주 묻는 질문</h1>
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
            <div class="all-text c121619 f14 mt16">
                전체 <span class="number inblock cm tm">{data.data.questions.totalElements}</span>개
            </div>
            <ul class="qna-box mt12">
                {#each data.data.questions.content as question, index (question.id)}
                <li>
                    <div class="q {activeStates[index] ? 'active' : ''}" on:click={() => toggle(index)}>
                        <p class="lh140 tb">Q. {question.subject}</p>
                    </div>
                    <div class="a {activeStates[index] ? 'active' : ''}">
                        <p class="lh140">
                            {question.content}
                        </p>
                    </div>
                </li>
            {/each}
            </ul>
            <div class="flex aic jce mt8">
                <div class="flex aic g4">
                    <button class="w80 h30 btn-type-1 bm bdr4 f12 cfff" on:click="{activateModalAdd}">1대1 문의하기</button>
                </div>
            </div>
            <div class="paging-box flex jcc mt40">
                <ul class="flex aic jcc">
                    {#if data.data.questions.number > 0}
                        <!-- 현재 페이지가 첫 페이지가 아닐 때만 이전 버튼을 표시 -->
                        <!-- svelte-ignore a11y-click-events-have-key-events -->
                        <!-- svelte-ignore a11y-no-noninteractive-element-interactions -->
                        <li class="page-btn"
                            on:click={() => changePage(data.searchKeyword, data.data.questions.number - 1)}>
                            <a href="">이전</a>
                        </li>
                    {/if}
                    {#each generatePageButtons(data.data.questions.totalPages) as button}
                        <!-- svelte-ignore a11y-click-events-have-key-events -->
                        <!-- svelte-ignore a11y-no-noninteractive-element-interactions -->
                        <li
                                class="num"
                                on:click={() => data.data.questions.number !== button - 1 && changePage(data.searchKeyword, button - 1)}
                        >
                            <a href="" class:active={data.data.questions.number === button - 1}>{button}</a>
                        </li>
                    {/each}
                    {#if data.data.questions.number < data.data.questions.totalPages - 1}
                        <!-- 현재 페이지가 마지막 페이지가 아닐 때만 다음 버튼을 표시 -->
                        <!-- svelte-ignore a11y-click-events-have-key-events -->
                        <!-- svelte-ignore a11y-no-noninteractive-element-interactions -->
                        <li class="page-btn"
                            on:click={() => changePage(data.searchKeyword, data.data.questions.number + 1)}>
                            <a href="">다음</a>
                        </li>
                    {/if}
                </ul>
            </div>
        </div>
    </div>
</div>