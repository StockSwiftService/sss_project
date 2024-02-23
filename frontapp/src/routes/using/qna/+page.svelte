<script>
    let isActive = false;
    let isActiveAdd = false;

    function activateModalAdd() {
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
                        <input type="email" placeholder="이메일">
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">제목<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="제목">
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">내용<span class="cr f16 tm inblock">*</span></h2>
                    <div class="textarea-type-1 f14 w100per h160">
                        <textarea placeholder="내용"></textarea>
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    </div>
                </div>
            </div>
            <div class="btn-area flex aic jcc g8 mt40">
                <button class="w120 h40 btn-type-2 bdr4 bm cfff tm f14">전송</button>
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
            <div class="all-text c121619 f14 mt16">
                전체 <span class="number inblock cm tm">0</span>개
            </div>
            <ul class="qna-box mt12">
            {#each [1, 2, 3] as index}
                <li>
                    <div class="q {activeStates[index] ? 'active' : ''}" on:click={() => toggle(index)}>
                        <p class="lh140 tb">Q. 질문입니다.{index}</p>
                    </div>
                    <div class="a {activeStates[index] ? 'active' : ''}">
                        <p class="lh140">
                        답변입니다.답변입니다.답변입니다.답변입니다.<br>
                        답변입니다.답변입니다.답변입니다.답변입니다.<br>
                        답변입니다.답변입니다.답변입니다.답변입니다.
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
