<script>

    let employeeFormData = {
        employeeName: '',
        position: '',
        authority: '',
        username: '',
        password: '',
        birthday: ''
    }

    let isActive = false;
    let isActiveAdd = false;
    let isActiveModifi = false;
    let isActiveNewPassword = false;

    //아이디
    let confirmUsernameErrorMessage = ''
    let confirmUsernameSuccessMessage = ''

    //이름
    let nameErrorMessage = ''
    let nameSuccessMessage = ''

    //직급
    let positionErrorMessage = ''
    let positionSuccessMessage = ''

    //권한
    let authorityErrorMessage = ''
    let authoritySuccessMessage = ''

    function activateModalAdd() {
        isActive = true;
        isActiveAdd = true;
    }

    function activateModalModifi() {
        isActive = true;
        isActiveModifi = true;
    }

    function activateModalNewPassword() {
        isActive = true;
        isActiveNewPassword = true;
    }

    function deactivateModal() {
        isActive = false;
        isActiveAdd = false;
        isActiveModifi = false;
        isActiveNewPassword = false;
    }


    //사원 아이디 중복 검사
    async function checkUsernameDuplicate() {
        if (!employeeFormData.username.trim()) {
            confirmUsernameErrorMessage = '필수 항목입니다'
            confirmUsernameSuccessMessage = ''
        } else {
            try {
                const response = await fetch('http://localhost:8080/api/v1/company/check-username', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({username: employeeFormData.username}),
                });
                if (response.ok) {
                    const data = await response.json();
                    if (data.resultCode == 'S-7') {
                        confirmUsernameErrorMessage = '중복된 아이디 입니다'
                        confirmUsernameSuccessMessage = ''
                    } else {
                        confirmUsernameErrorMessage = ''
                        confirmUsernameSuccessMessage = '사용가능한 아이디 입니다'
                    }
                }
            } catch (error) {
                console.error('오류 발생:', error);
            }
        }
    }


    //사원 등록
    const employeeSubmit = async () => {
        try {
            if (!employeeFormData.employeeName.trim()) {
                nameErrorMessage = '필수 항목입니다'
                nameSuccessMessage = ''
            } else if (!employeeFormData.position.trim()) {
                positionErrorMessage = '필수 항목입니다'
                positionSuccessMessage = ''
            } else if (!employeeFormData.authority) {
                authorityErrorMessage = '필수 항목입니다'
                authoritySuccessMessage = ''
            }

            const response = await fetch('http://localhost:8080/api/v1/member/join', {
                method: 'POST',
                // credentials: "include",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(employeeFormData)
            });
            if (response.ok) {
                const data = await response.json();
                console.log(data)
                // 회원가입 성공
                if (data.resultCode === 'S-1') {
                    alert('사원 등록이 완료되었습니다.');
                } else {
                    // 회원가입 실패
                    const errorMessage = data.errorMessage;
                    console.error('가입 실패:', errorMessage);
                }
            } else {
                console.error('서버 응답 오류:', response);
                return;
            }
        } catch (error) {
            console.error('오류 발생:', error);
        }
    }
</script>
<div class="modal-area-1 modal-area wh100per fixed zi9" class:active="{isActive}">
    <!-- 회원 등록 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w480" class:active="{isActiveAdd}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">회원 등록</h3>
            <button class="x-btn img-box abs" on:click|preventDefault="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <form class="middle-box scr-type-1" on:submit|preventDefault={employeeSubmit}>
            <div class="flex fdc g36">
                <div>
                    <h2 class="c333 f15 tm mb8">이름<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" placeholder="이름" on:value={employeeFormData.employeeName}>
                        </div>
                    </div>
                    {#if nameErrorMessage}
                        <span class="f13 mt4 cr">{nameErrorMessage}</span>
                    {/if}
                    {#if nameSuccessMessage}
                        <span class="f13 mt4 cg">{nameSuccessMessage}</span>
                    {/if}
                </div>

                <div>
                    <h2 class="c333 f15 tm mb8">직급<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="직급" on:value={employeeFormData.position}>
                    </div>
                    {#if positionErrorMessage}
                        <span class="f13 mt4 cr">{positionErrorMessage}</span>
                    {/if}
                    {#if positionSuccessMessage}
                        <span class="f13 mt4 cg">{positionSuccessMessage}</span>
                    {/if}
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">등급<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex aic g12">
                        <div class="check-type-2">
                            <input type="radio" id="1" name="rating" on:value={employeeFormData.authority}>
                            <label for="1" class="flex aic g4">
                                <span class="img-box w16">
                                    <img src="/img/ico_check_2.svg" alt="">
                                </span>
                                <span class="text f14 cA2A9B0">일반</span>
                            </label>
                        </div>
                        <div class="check-type-2">
                            <input type="radio" id="2" name="rating" on:value={employeeFormData.authority}>
                            <label for="2" class="flex aic g4">
                                <span class="img-box w16">
                                    <img src="/img/ico_check_2.svg" alt="">
                                </span>
                                <span class="text f14 cA2A9B0">관리자</span>
                            </label>
                        </div>
                    </div>
                    {#if authorityErrorMessage}
                        <span class="f13 mt4 cr">{authorityErrorMessage}</span>
                    {/if}
                    {#if authoritySuccessMessage}
                        <span class="f13 mt4 cg">{authoritySuccessMessage}</span>
                    {/if}
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">아이디<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" placeholder="아이디" on:value={employeeFormData.username}>
                        </div>
                        <button class="btn-type-1 w80 f14 bdr4 b333 cfff"
                                on:click|preventDefault={checkUsernameDuplicate}>확인
                        </button>
                    </div>
                    {#if confirmUsernameErrorMessage}
                        <span class="f13 mt4 cr">{confirmUsernameErrorMessage}</span>
                    {/if}
                    {#if confirmUsernameSuccessMessage}
                        <span class="f13 mt4 cg">{confirmUsernameSuccessMessage}</span>
                    {/if}
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">비밀번호<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="password" placeholder="비밀번호" on:value={employeeFormData.password}>
                    </div>
                    <!--                    <div class="error-text-box">-->
                    <!--                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>-->
                    <!--                    </div>-->
                    <div class="input-type-1 f14 w100per mt8">
                        <input type="password" placeholder="비밀번호 확인">
                    </div>
                    <!--                    <div class="error-text-box">-->
                    <!--                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>-->
                    <!--                        <span class="f13 mt8 cr">비밀번호가 일치하지 않습니다.</span>-->
                    <!--                    </div>-->
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">생년월일<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="date" placeholder="생년월일" on:value={employeeFormData.birthday}>
                    </div>
                    <!--                    <div class="error-text-box">-->
                    <!--                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>-->
                    <!--                    </div>-->
                </div>
            </div>
            <div class="btn-area flex aic jcc g8 mt40">
                <button type="submit" class="w120 h40 btn-type-2 bdr4 bm cfff tm f14">등록</button>
                <button class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" on:click="{deactivateModal}">취소</button>
            </div>
        </form>
    </div>
    <!-- 회원 수정 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w480" class:active="{isActiveModifi}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">회원 수정</h3>
            <button class="x-btn img-box abs" on:click="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <div class="flex fdc g36">
                <div>
                    <h2 class="c333 f15 tm mb8">이름<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" placeholder="이름">
                        </div>
                        <button class="btn-type-1 w80 f14 bdr4 b333 cfff">확인</button>
                    </div>
                    <!--                    <div class="error-text-box">-->
                    <!--                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>-->
                    <!--                        <span class="f13 mt8 cr">중복된 이름입니다.</span>-->
                    <!--                        <span class="f13 mt8 cg">사용 가능한 이름입니다.</span>-->
                    <!--                    </div>-->
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">직급<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="직급">
                    </div>
                    <!--                    <div class="error-text-box">-->
                    <!--                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>-->
                    <!--                    </div>-->
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">등급<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex aic g12">
                        <div class="check-type-2">
                            <input type="radio" id="1" name="rating" checked>
                            <label for="1" class="flex aic g4">
                                <span class="img-box w16">
                                    <img src="/img/ico_check_2.svg" alt="">
                                </span>
                                <span class="text f14 cA2A9B0">일반</span>
                            </label>
                        </div>
                        <div class="check-type-2">
                            <input type="radio" id="2" name="rating">
                            <label for="2" class="flex aic g4">
                                <span class="img-box w16">
                                    <img src="/img/ico_check_2.svg" alt="">
                                </span>
                                <span class="text f14 cA2A9B0">관리자</span>
                            </label>
                        </div>
                    </div>
                    <!--                    <div class="error-text-box">-->
                    <!--                        <span class="f13 mt8 cr">필수 선택 항목입니다.</span>-->
                    <!--                    </div>-->
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">아이디<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" placeholder="아이디">
                        </div>
                        <button class="btn-type-1 w80 f14 bdr4 b333 cfff">확인</button>
                    </div>
                    <!--                    <div class="error-text-box">-->
                    <!--                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>-->
                    <!--                        <span class="f13 mt8 cr">중복된 아이디입니다.</span>-->
                    <!--                        <span class="f13 mt8 cg">사용 가능한 아이디입니다.</span>-->
                    <!--                    </div>-->
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">생년월일<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="date" placeholder="생년월일">
                    </div>
                    <!--                    <div class="error-text-box">-->
                    <!--                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>-->
                    <!--                    </div>-->
                </div>
            </div>
            <div class="btn-area flex aic jcc g8 mt40">
                <button class="w120 h40 btn-type-2 bdr4 bm cfff tm f14">수정</button>
                <button class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" on:click="{deactivateModal}">취소</button>
            </div>
        </div>
    </div>

    <!-- 회원 비밀번호 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w480" class:active="{isActiveNewPassword}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">새로운 비밀번호</h3>
            <button class="x-btn img-box abs" on:click="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <div class="flex fdc g36">
                <div>
                    <h2 class="c333 f15 tm mb8">비밀번호<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="password" placeholder="비밀번호">
                    </div>
                    <!--                    <div class="error-text-box">-->
                    <!--                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>-->
                    <!--                    </div>-->
                    <div class="input-type-1 f14 w100per mt8">
                        <input type="password" placeholder="비밀번호 확인">
                    </div>
                    <!--                    <div class="error-text-box">-->
                    <!--                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>-->
                    <!--                        <span class="f13 mt8 cr">비밀번호가 일치하지 않습니다.</span>-->
                    <!--                    </div>-->
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
        <h1 class="tb c121619">회원 관리</h1>
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
                전체 <span class="number inblock cm tm">0</span>명
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
                        <th class="wsn">이름</th>
                        <th class="wsn">직급</th>
                        <th class="wsn">등급</th>
                        <th class="wsn">아이디</th>
                        <th class="wsn">생년월일</th>
                        <th class="wsn">수정</th>
                        <th class="wsn">비밀번호</th>
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
                        <td class="wsn">김직원</td>
                        <td class="wsn">사원</td>
                        <td class="wsn">일반</td>
                        <td class="wsn">user1</td>
                        <td class="wsn">1999-07-14</td>
                        <td class="wsn tac">
                            <button class="w40 h24 btn-type-2 bdr4 bdbbb cbbb f13" on:click="{activateModalModifi}">수정
                            </button>
                        </td>
                        <td class="wsn tac">
                            <button class="w50 h24 btn-type-2 bdr4 bdbbb cbbb f13"
                                    on:click="{activateModalNewPassword}">초기화
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td class="wsn" style="width: 44px;">
                            <div class="check-type-1">
                                <input type="checkbox" id="v2">
                                <label for="v2"></label>
                            </div>
                        </td>
                        <td class="wsn">이직원</td>
                        <td class="wsn">과장</td>
                        <td class="wsn">관리자</td>
                        <td class="wsn">user2</td>
                        <td class="wsn">1990-11-03</td>
                        <td class="wsn tac">
                            <button class="w40 h24 btn-type-2 bdr4 bdbbb cbbb f13" on:click="{activateModalModifi}">수정
                            </button>
                        </td>
                        <td class="wsn tac">
                            <button class="w50 h24 btn-type-2 bdr4 bdbbb cbbb f13"
                                    on:click="{activateModalNewPassword}">초기화
                            </button>
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