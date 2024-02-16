<script>
    let isActive = false;
    let isActiveAdd = false;
    let isActiveModifi = false;
    let isActiveRecord = false;

    function activateModalAdd() {
        isActive = true;
        isActiveAdd = true;
    }

    function activateModalModifi() {
        isActive = true;
        isActiveModifi = true;
    }

    function activateModalRecord() {
        isActive = true;
        isActiveRecord = true;
    }

    function deactivateModal() {
        isActive = false;
        isActiveAdd = false;
        isActiveModifi = false;
        isActiveRecord = false;
    }
</script>

<div class="modal-area wh100per fixed zi9" class:active="{isActive}">

    <!-- 재고 등록 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w480" class:active="{isActiveAdd}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">재고 등록</h3>
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
                            <input type="text" placeholder="거래처명" readonly>
                        </div>
                        <button class="btn-type-1 w80 f14 bdr4 b333 cfff">찾기</button>
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 선택 항목입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">품목명<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" placeholder="품목명">
                        </div>
                        <button class="btn-type-1 w80 f14 bdr4 b333 cfff">확인</button>
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">중복된 품목명입니다.</span>
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        <span class="f13 mt8 cg">사용할 수 있는 품목명입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">수량<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="수량">
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">구매 단가<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="구매 단가">
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">판매 단가<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="판매 단가">
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

    <!-- 재고 수정 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w480" class:active="{isActiveModifi}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">재고 수정</h3>
            <button class="x-btn img-box abs" on:click="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <div class="flex fdc g36">
                <div>
                    <h2 class="c333 f15 tm mb8">거래처명<span class="cr f16 tm inblock">*</span></h2>
                    <div class="select-type-3 w100per f14 rel">
                        <select name="account">
                            <option value="">모든 거래처</option>
                            <option value="">거래처1</option>
                            <option value="">거래처2</option>
                            <option value="">거래처3</option>
                        </select>
                        <span class="arrow img-box abs y-middle">
                            <img src="/img/arrow_bottom_A2A9B0.svg" alt="" />
                        </span>
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 선택 항목입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">품목명<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" placeholder="품목명">
                        </div>
                        <button class="btn-type-1 w80 f14 bdr4 b333 cfff">확인</button>
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">중복된 품목명입니다.</span>
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        <span class="f13 mt8 cg">사용할 수 있는 품목명입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">구매 단가<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="구매 단가">
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">판매 단가<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="판매 단가">
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
                            <th class="wsn">품목명</th>
                            <th class="wsn">수량</th>
                            <th class="wsn">구매 단가</th>
                            <th class="wsn">판매 단가</th>
                            <th class="wsn">이력</th>
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
                            <td class="wsn tal">네모네모 스낵 100g</td>
                            <td class="wsn">300</td>
                            <td class="wsn">1,000</td>
                            <td class="wsn">1,500</td>
                            <td class="wsn tac">
                                <button class="w40 h24 btn-type-2 bdr4 bdbbb cbbb f13" on:click={activateModalRecord}>이력</button>
                            </td>
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
                            <td class="wsn tal">네모네모 사탕 30g</td>
                            <td class="wsn">500</td>
                            <td class="wsn">500</td>
                            <td class="wsn">800</td>
                            <td class="wsn tac">
                                <button class="w40 h24 btn-type-2 bdr4 bdbbb cbbb f13">이력</button>
                            </td>
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