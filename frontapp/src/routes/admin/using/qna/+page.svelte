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

<div class="modal-area wh100per fixed zi9" class:active="{isActive}">

    <!-- 질문 등록 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9" class:active="{isActiveAdd}">
        <button class="x-btn img-box abs" on:click="{deactivateModal}">
            <img src="../img/ico_x_121619.svg" alt="닫기 아이콘">
        </button>
        <h3 class="tb c121619 f18">질문 등록</h3>
        <div class="flex fdc g36 mt40">
            <div>
                <h2 class="c333 f15 tm mb8">제목<span class="cr f16 tm inblock">*</span></h2>
                <div class="input-type-1 f14 w100per">
                    <input type="text" placeholder="제목">
                </div>
                <div class="error-text-box">
                    <span class="f13 mt4 cr">필수 입력 항목입니다.</span>
                </div>
            </div>
            <div>
                <h2 class="c333 f15 tm mb8">내용<span class="cr f16 tm inblock">*</span></h2>
                <div class="textarea-type-1 f14 w100per">
                    <textarea placeholder="내용"></textarea>
                </div>
                <div class="error-text-box">
                    <span class="f13 mt4 cr">필수 입력 항목입니다.</span>
                </div>
            </div>
        </div>
        <div class="btn-area flex aic jcc g8 mt40">
            <button class="w120 h40 btn-type-2 bdr4 bm cfff tm f14">등록</button>
            <button class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" on:click="{deactivateModal}">취소</button>
        </div>
    </div>

    <!-- 질문 수정 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9" class:active="{isActiveModifi}">
        <button class="x-btn img-box abs" on:click="{deactivateModal}">
            <img src="../img/ico_x_121619.svg" alt="닫기 아이콘">
        </button>
        <h3 class="tb c121619 f18">질문 수정</h3>
        <div class="flex fdc g36 mt40">
            <div>
                <h2 class="c333 f15 tm mb8">제목<span class="cr f16 tm inblock">*</span></h2>
                <div class="input-type-1 f14 w100per">
                    <input type="text" placeholder="제목" value="수정제목 수정제목">
                </div>
                <div class="error-text-box">
                    <span class="f13 mt4 cr">필수 입력 항목입니다.</span>
                </div>
            </div>
            <div>
                <h2 class="c333 f15 tm mb8">내용<span class="cr f16 tm inblock">*</span></h2>
                <div class="textarea-type-1 f14 w100per">
                    <textarea placeholder="내용">수정내용입니다.</textarea>
                </div>
                <div class="error-text-box">
                    <span class="f13 mt4 cr">필수 입력 항목입니다.</span>
                </div>
            </div>
        </div>
        <div class="btn-area flex aic jcc g8 mt40">
            <button class="w120 h40 btn-type-2 bdr4 bm cfff tm f14">수정</button>
            <button class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" on:click="{deactivateModal}">취소</button>
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
                전체 <span class="number inblock cm tm">0</span>건
            </div>
            <div class="table-box-1 table-type-1 scr-type-1 mt12">
                <table>
                    <thead>
                        <tr>
                            <th class="wsn" style="width: 44px;">
                                <div class="check-type-1">
                                    <input type="checkbox" id="all">
                                    <label for="all"></label>
                                </div> 
                            </th>
                            <th class="wsn">제목</th>
                            <th class="wsn">내용</th>
                            <th class="wsn">공개 여부</th>
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
                            <td class="wsn tal">회원가입했는데 왜 거절당했나요?</td>
                            <td class="tal lh140">
                                안녕하세요. 저의 SSS를 사용해주셔서 감사합니다.<br>
                                거절된 사유는 ㅇㅇㅇㅇㅇㅇ입니다.<br>
                                다시 한 번 확인 후 가입 신청해주시길 바랍니다.
                            </td>
                            <td class="wsn">
                                <span class="cr">비공개</span>
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
                            <td class="wsn tal">회원가입했는데 왜 거절당했나요?</td>
                            <td class="tal lh140">
                                안녕하세요. 저의 SSS를 사용해주셔서 감사합니다.<br>
                                거절된 사유는 ㅇㅇㅇㅇㅇㅇ입니다.<br>
                                다시 한 번 확인 후 가입 신청해주시길 바랍니다.
                            </td>
                            <td class="wsn">
                                <span class="cg">공개</span>
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
                    <button class="w50 h30 btn-type-1 bm bdr4 f12 cfff" id="add_btn" on:click="{activateModalAdd}">등록</button>
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