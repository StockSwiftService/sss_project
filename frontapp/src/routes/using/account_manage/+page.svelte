<script>
    let isActive = false;
    let isActiveAdd = false;
    let isActiveModifi = false;

    function activateModalAdd() {
        isActive = true;
        isActiveAdd = true;
    }

    function activateModalModifi() {
        isActive = true;
        isActiveModifi = true;
    }

    function deactivateModal() {
        isActive = false;
        isActiveAdd = false;
        isActiveModifi = false;
    }
</script>

<div class="modal-area-1 modal-area wh100per fixed zi9" class:active="{isActive}">

    <!-- 거래처 등록 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w480" class:active="{isActiveAdd}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">거래처 등록</h3>
            <button class="x-btn img-box abs" on:click="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <div class="flex fdc g36">
                <div>
                    <h2 class="c333 f15 tm mb8">거래처명<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" placeholder="거래처명">
                        </div>
                        <button class="btn-type-1 w80 f14 bdr4 b333 cfff">확인</button>
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        <span class="f13 mt8 cr">중복된 거래처명입니다.</span>
                        <span class="f13 mt8 cg">사용 가능한 거래처명입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">대표자명<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="대표자명">
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">연락처<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="연락처 (-자 빼고 입력해 주세요.)">
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">주소<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" placeholder="주소">
                        </div>
                        <button class="btn-type-1 w80 f14 bdr4 b333 cfff">찾기</button>
                    </div>
                    <div class="input-type-1 f14 w100per mt8">
                        <input type="text" placeholder="상세주소">
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    </div>
                </div>
            </div>
            <div class="btn-area flex aic jcc g8 mt40">
                <button class="w120 h40 btn-type-2 bdr4 bm cfff tm f14">등록</button>
                <button class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" on:click="{deactivateModal}">취소</button>
            </div>
        </div>
    </div>

    <!-- 거래처 수정 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w480" class:active="{isActiveModifi}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">거래처 수정</h3>
            <button class="x-btn img-box abs" on:click="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <div class="flex fdc g36">
                <div>
                    <h2 class="c333 f15 tm mb8">거래처명<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" placeholder="거래처명">
                        </div>
                        <button class="btn-type-1 w80 f14 bdr4 b333 cfff">확인</button>
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        <span class="f13 mt8 cr">중복된 거래처명입니다.</span>
                        <span class="f13 mt8 cg">사용 가능한 거래처명입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">대표자명<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="대표자명">
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">연락처<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="연락처 (-자 빼고 입력해 주세요.)">
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">주소<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" placeholder="주소">
                        </div>
                        <button class="btn-type-1 w80 f14 bdr4 b333 cfff">찾기</button>
                    </div>
                    <div class="input-type-1 f14 w100per mt8">
                        <input type="text" placeholder="상세주소">
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

<div class="store-management-area cnt-area w100per">
    <div class="title-box flex aic jcsb">
        <h1 class="tb c121619">거래처 관리</h1>
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
            <div class="all-text c121619 f14">
                전체 <span class="number inblock cm tm">0</span>개
            </div>
            <div class="table-box-1 table-type-1 scr-type-2 mt12">
                <table>
                    <thead>
                        <tr>
                            <th class="wsn" style="width: 44px;">
                                <div class="check-type-1">
                                    <input type="checkbox" id="all">
                                    <label for="all"></label>
                                </div> 
                            </th>
                            <th class="wsn">거래처명</th>
                            <th class="wsn">대표자명</th>
                            <th class="wsn">연락처</th>
                            <th class="wsn">주소</th>
                            <th class="wsn">수정</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="wsn" style="width: 44px;">   
                                <div class="check-type-1">
                                    <input type="checkbox" id="v1">
                                    <label for="v1"></label>
                                </div> 
                            </td>
                            <td class="wsn">(주)네모컴퍼니</td>
                            <td class="wsn">김네모</td>
                            <td class="wsn">01033333333</td>
                            <td class="wsn">대전광역시 대덕구 덕암동 572-3번지</td>
                            <td class="wsn tac">
                                <button class="w40 h24 btn-type-2 bdr4 bdbbb cbbb f13" on:click="{activateModalModifi}">수정</button>
                            </td>
                        </tr>
                        <tr>
                            <td class="wsn" style="width: 44px;">   
                                <div class="check-type-1">
                                    <input type="checkbox" id="v2">
                                    <label for="v2"></label>
                                </div> 
                            </td>
                            <td class="wsn">(주)네모컴퍼니</td>
                            <td class="wsn">김네모</td>
                            <td class="wsn">01033333333</td>
                            <td class="wsn">대전광역시 대덕구 덕암동 572-3번지</td>
                            <td class="wsn tac">
                                <button class="w40 h24 btn-type-2 bdr4 bdbbb cbbb f13" on:click="{activateModalModifi}">수정</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="flex aic jcsb mt8">
                <div class="flex aic g4">
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