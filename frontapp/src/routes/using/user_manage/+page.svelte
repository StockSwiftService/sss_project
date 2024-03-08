<script>
    import {onMount} from 'svelte';

    //페이징
    let memberList = [];
    let resList = [];
    let keyword = '';
    let memberTotal = [];

    let members = [];
    let selectedMemberIds = []; //체크박스에 체크된 멤버의 아이디를 저장
    let loginUser = [];

    onMount(async () => {
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
                loginUser = data.data.member
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
    });


    const changePage = async (page) => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/member/user-manages?page=${page}&keyWord=${keyword}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include'
            });

            if (response.ok) {
                const data = await response.json();
                resList = data.data.pagingList;
                memberList = resList.content;
                memberTotal = data.data.memberList;
                console.log(memberTotal)
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
    };

    function generatePageButtons(totalPages) {
        const buttons = [];
        for (let i = 0; i < totalPages; i++) {
            buttons.push(i + 1);
        }
        return buttons;
    }

    //회원 리스트 불러오기
    onMount(async () => {
        try {
            const response = await fetch('http://localhost:8080/api/v1/member/user-manages', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            if (response.ok) {
                const data = await response.json();
                if (data.resultCode === 'S-1') {
                    members = ''
                    alert('접근 권한이 없습니다.')
                    window.location.href = '/using/account_manage'
                } else {
                    members = data.data.memberList
                }
                resList = data.data.pagingList;
                memberList = resList.content;
                memberTotal = data.data.memberList;
                console.log(resList)
            } else {
                console.error('서버 응답 오류:', response.statusText);
            }
        } catch (error) {
            console.error('오류 발생:', error);
        }
    });

    let employeeFormData = {
        employeeName: '',
        position: '',
        authority: '',
        username: '',
        password: '',
        passwordConfirm: '',
        birthday: ''
    }

    let isActive = false;
    let isActiveAdd = false;
    let isActiveModifi = false;
    let isActiveNewPassword = false;

    //아이디
    let confirmUsernameErrorMessage = ''
    let confirmUsernameSuccessMessage = ''
    let duplicatedUsername = false
    let isCheckUsernameDuplicate = false

    //패스워드
    let passwordConfirm = false
    let passwordSuccessMessage = ''
    let passwordErrorMessage = ''

    //패스워드 확인
    let passwordConfirmErrorMessage = ''
    let passwordConfirmSuccessMessage = ''
    let isPasswordConfirm = false

    function activateModalAdd() {
        isActive = true;
        isActiveAdd = true;
    }

    let modifyData = {
        id: '',
        employeeName: '',
        position: '',
        authority: '',
        username: '',
        birthday: '',
        password: '',
        PasswordConfirm: ''
    }

    function activateModalModifi(event) {
        isActive = true;
        isActiveModifi = true;

        // 클릭된 버튼의 id에서 member를 추출
        const buttonId = event.target.id;
        const memberId = buttonId.split('_')[1];
        const member = JSON.parse(memberId); // member를 객체로 파싱

        // 회원 정보를 모달 창에 적절히 표시 (예시: input 요소에 값을 할당)
        modifyData.id = member.id
        modifyData.employeeName = member.name
        modifyData.position = member.position;
        modifyData.authority = member.authority;
        modifyData.username = member.username;
        modifyData.password = member.password;
        modifyData.birthday = member.birthday;
    }


    function activateModalNewPassword(event) {
        isActive = true;
        isActiveNewPassword = true;

        // 클릭된 버튼의 id에서 member를 추출
        const buttonId = event.target.id;
        const memberId = buttonId.split('_')[1]; // 'modify_{member.id}' 형식에서 member.id 부분만 추출
        const member = JSON.parse(memberId); // member를 객체로 파싱

        modifyData.id = member.id
        modifyData.employeeName = member.name
        modifyData.position = member.position;
        modifyData.authority = member.authority;
        modifyData.username = member.username;
        modifyData.birthday = member.birthday;
        modifyData.password = null
    }

    function deactivateModal() {
        isActive = false;
        isActiveAdd = false;
        isActiveModifi = false;
        isActiveNewPassword = false;
    }


    //사원 아이디 중복 검사
    async function checkUsernameDuplicate() {
        isCheckUsernameDuplicate = true
        if (!employeeFormData.username.trim()) {
            confirmUsernameErrorMessage = '아이디를 입력해 주세요.'
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
                        return null;
                    } else {
                        confirmUsernameErrorMessage = ''
                        confirmUsernameSuccessMessage = '사용가능한 아이디 입니다'
                        duplicatedUsername = true
                        return employeeFormData.username
                    }
                }
            } catch (error) {
                console.error('오류 발생:', error);
            }
        }
    }

    //사원 아이디 중복 검사
    async function checkModifyUsernameDuplicate() {
        isCheckUsernameDuplicate = true
        if (!modifyData.username.trim()) {
            confirmUsernameErrorMessage = '아이디를 입력해 주세요.'
            confirmUsernameSuccessMessage = ''
        } else {
            try {
                const response = await fetch('http://localhost:8080/api/v1/company/check-username', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({username: modifyData.username}),
                });
                if (response.ok) {
                    const data = await response.json();
                    if (data.resultCode == 'S-7') {
                        confirmUsernameErrorMessage = '중복된 아이디 입니다'
                        confirmUsernameSuccessMessage = ''
                        return null;
                    } else {
                        confirmUsernameErrorMessage = ''
                        confirmUsernameSuccessMessage = '사용가능한 아이디 입니다'
                        duplicatedUsername = true
                        return modifyData.username
                    }
                }
            } catch (error) {
                console.error('오류 발생:', error);
            }
        }
    }

    //비번 검증
    function validatePassword() {
        const passwordRegex = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{6,16}$/;
        if (!passwordRegex.test(employeeFormData.password)) {
            passwordSuccessMessage = ''
            passwordErrorMessage = '비밀번호는 6~16자 영문 대 소문자, 숫자, 특수문자를 사용해야 합니다';
            passwordConfirm = false
        } else {
            passwordErrorMessage = ''
            passwordConfirm = true
            return employeeFormData.password;
        }
    }

    //비번 확인 검증
    function confirmValidatePassword() {
        if (employeeFormData.password === employeeFormData.passwordConfirm) {
            passwordConfirmSuccessMessage = '비밀번호가 일치합니다'
            passwordConfirmErrorMessage = '';
            isPasswordConfirm = true
        } else {
            passwordConfirmSuccessMessage = ''
            passwordConfirmErrorMessage = '비밀번호가 일치하지 않습니다';
            isPasswordConfirm = false
        }
    }


    //사원 등록
    const employeeSubmit = async () => {
        if (!employeeFormData.username.trim() || !employeeFormData.employeeName.trim() || !employeeFormData.password.trim() || !employeeFormData.passwordConfirm ||
            !employeeFormData.birthday.trim() || !employeeFormData.authority.trim() || !employeeFormData.position.trim()) {
            alert('양식을 모두 입력해주세요.')
        } else if (duplicatedUsername == false || isCheckUsernameDuplicate == false) {
            alert('아이디를 다시 확인해 주세요.')
        } else if (isPasswordConfirm == false || employeeFormData.password !== employeeFormData.passwordConfirm || passwordConfirm == false) {
            alert('패스워드를 확인해 주세요.')
        } else {
            try {
                const response = await fetch('http://localhost:8080/api/v1/member/join', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(employeeFormData)
                });
                if (response.ok) {
                    const data = await response.json();
                    // 회원가입 성공
                    if (data.resultCode === 'S-1') {
                        alert('사원 등록이 완료되었습니다.');
                        window.location.href = '/using/user_manage';
                    } else {
                        // 회원가입 실패
                        const errorMessage = data.errorMessage;
                        console.error('가입 실패:', errorMessage);
                    }
                } else {
                    console.error('서버 응답 오류:', response.statusText());
                    alert('양식을 모두 입력해주세요')

                }
            } catch (error) {
                console.error('오류 발생:', error);
            }
        }
    }


    //체크 박스
    let yes = false; // "전체 선택" 체크박스의 상태를 나타내는 변수
    // "전체 선택" 체크박스
    function toggleAll() {
        yes = !yes;

        // 모든 항목의 체크 상태를 "전체 선택" 체크박스와 동기화
        let memberCheckboxes = document.querySelectorAll('[id^="checkbox_"]');
        memberCheckboxes.forEach(checkbox => {
            checkbox.checked = yes;

            // 체크된 경우 해당 체크박스의 아이디 값을 배열에 추가
            if (yes) {
                // 체크된 경우에만 아이디 값을 배열에 추가
                let memberId = checkbox.id.replace('checkbox_', ''); // 체크박스의 아이디에서 'checkbox_' 부분을 제거하여 memberId를 추출
                selectedMemberIds.push(memberId);
            } else {
                // 체크가 해제된 경우에는 배열에서 해당 아이디를 제거
                let memberId = checkbox.id.replace('checkbox_', ''); // 체크박스의 아이디에서 'checkbox_' 부분을 제거하여 memberId를 추출
                let index = selectedMemberIds.indexOf(memberId);
                if (index !== -1) {
                    selectedMemberIds.splice(index, 1); // 해당 아이디를 배열에서 제거
                }
            }
        });
    }

    // 멤버의 ID 값을 처리하는 함수
    function handleCheckboxChange(event, memberId) {
        const isChecked = event.target.checked;
        if (isChecked) {
            selectedMemberIds.push(memberId);
        } else {
            const index = selectedMemberIds.indexOf(memberId);
            if (index !== -1) {
                selectedMemberIds.splice(index, 1); // 배열에서 해당 멤버 아이디 제거
            }
        }
    }

    //회원 삭제
    async function deleteMember() {
        if (selectedMemberIds.length === 0) {
            alert('삭제할 회원을 선택해주세요.');
            return;
        } else {
            const confirmation = confirm('선택한 회원을 정말 삭제하시겠습니까?');

            if (confirmation) {
                const url = 'http://localhost:8080/api/v1/member/delete?ids=' + selectedMemberIds.join(','); // 선택된 멤버들의 아이디를 URL에 포함시킴
                try {
                    const response = await fetch(url, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(selectedMemberIds)
                    });
                    if (response.ok) {
                        const data = await response.json();

                        if (data.resultCode === 'S-3') {
                            alert('사원 삭제가 완료되었습니다.');
                            window.location.href = '/using/user_manage';
                        } else {
                            const errorMessage = data.errorMessage;
                            console.error('삭제 실패:', errorMessage);
                        }
                    } else {
                        console.error('서버 응답 오류:', response.statusText);
                    }
                } catch (error) {
                    console.error('오류 발생:', error);
                }
            } else {
                console.log('삭제가 취소되었습니다.');
            }
        }
    }

    //비번 수정시 검증
    function validateModifyPassword() {
        const passwordRegex = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{6,16}$/;
        if (!passwordRegex.test(modifyData.password)) {
            passwordSuccessMessage = ''
            passwordErrorMessage = '비밀번호는 6~16자 영문 대 소문자, 숫자, 특수문자를 사용해야 합니다';
            passwordConfirm = false
        } else {
            passwordErrorMessage = ''
            return modifyData.password;
            passwordConfirm = true
        }
    }

    //비번 확인 수정시 검증
    function confirmValidateModifyPassword() {
        if (modifyData.password === modifyData.passwordConfirm) {
            passwordConfirmSuccessMessage = '비밀번호가 일치합니다'
            passwordConfirmErrorMessage = '';
            isPasswordConfirm = true
            passwordConfirm = true
        } else {
            passwordConfirmSuccessMessage = ''
            passwordConfirmErrorMessage = '비밀번호가 일치하지 않습니다';
            isPasswordConfirm = false
        }
    }

    //회원 수정
    const employeeModifySubmit = async () => {
        if (!modifyData.password.trim()) {
            alert('변경사항이 없습니다. 다시 입력해 주세요.')
            return
        }
        try {
            const response = await fetch('http://localhost:8080/api/v1/member/modify', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(modifyData)
            });
            if (response.ok) {
                const data = await response.json();

                if (data.resultCode === 'S-4') {
                    alert('수정이 완료되었습니다.');
                    window.location.href = '/using/user_manage';
                } else {

                    const errorMessage = data.errorMessage;
                    console.error('수정 실패:', errorMessage);
                }
            } else {
                console.error('서버 응답 오류:', response.statusText);
            }
        } catch (error) {
            console.error('오류 발생:', error);
        }
    }

    //회원 비번 초기화
    const passwordModifySubmit = async () => {
        if (!passwordConfirm) {
            alert('비밀번호를 다시 확인해 주세요.')
        } else {
            try {
                const response = await fetch('http://localhost:8080/api/v1/member/modify-password', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(modifyData)
                });
                if (response.ok) {
                    const data = await response.json();

                    if (data.resultCode === 'S-4') {
                        alert('수정이 완료되었습니다.');
                        window.location.href = '/using/user_manage';
                    } else {

                        const errorMessage = data.errorMessage;
                        console.error('수정 실패:', errorMessage);
                    }
                } else {
                    console.error('서버 응답 오류:', response.statusText);
                }
            } catch (error) {
                console.error('오류 발생:', error);
            }
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
                            <input type="text" placeholder="이름" bind:value={employeeFormData.employeeName}>
                        </div>
                    </div>
                </div>

                <div>
                    <h2 class="c333 f15 tm mb8">직급<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="직급" bind:value={employeeFormData.position}>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">등급<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex aic g12">
                        <div class="check-type-2">
                            <input type="radio" id="1" name="rating" value="4" bind:group={employeeFormData.authority}>
                            <label for="1" class="flex aic g4">
                                <span class="img-box w16">
                                    <img src="/img/ico_check_2.svg" alt="">
                                </span>
                                <span class="text f14 cA2A9B0">일반</span>
                            </label>
                        </div>
                        <div class="check-type-2">
                            <input type="radio" id="2" name="rating" value="3" bind:group={employeeFormData.authority}>
                            <label for="2" class="flex aic g4">
                                <span class="img-box w16">
                                    <img src="/img/ico_check_2.svg" alt="">
                                </span>
                                <span class="text f14 cA2A9B0">관리자</span>
                            </label>
                        </div>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">아이디<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" placeholder="아이디" bind:value={employeeFormData.username}>
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
                        <input type="password" placeholder="비밀번호" bind:value={employeeFormData.password}
                               on:input={validatePassword}>
                    </div>
                    {#if passwordErrorMessage}
                        <span class="f13 mt4 cr">{passwordErrorMessage}</span>
                    {/if}
                    {#if passwordSuccessMessage}
                        <span class="f13 mt4 cg">{passwordSuccessMessage}</span>
                    {/if}
                    <div class="input-type-1 f14 w100per mt8">
                        <input type="password" placeholder="비밀번호 확인" bind:value={employeeFormData.passwordConfirm}
                               on:input={confirmValidatePassword}>
                    </div>
                    {#if passwordConfirmErrorMessage}
                        <span class="f13 mt4 cr">{passwordConfirmErrorMessage}</span>
                    {/if}
                    {#if passwordConfirmSuccessMessage}
                        <span class="f13 mt4 cg">{passwordConfirmSuccessMessage}</span>
                    {/if}
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">생년월일<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="date" placeholder="생년월일" bind:value={employeeFormData.birthday}>
                    </div>
                </div>
            </div>
            <div class="btn-area flex aic jcc g8 mt40">
                <button type="submit" class="w120 h40 btn-type-2 bdr4 bm cfff tm f14">등록</button>
                <button type="button" class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" on:click="{deactivateModal}">취소
                </button>
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
        <form class="middle-box scr-type-1" on:submit|preventDefault={employeeModifySubmit}>
            <div class="flex fdc g36">
                <div>
                    <h2 class="c333 f15 tm mb8">이름<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" placeholder="이름" bind:value={modifyData.employeeName}>
                        </div>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">직급<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="직급" bind:value={modifyData.position}>
                    </div>
                </div>
                <div>
                    {#if modifyData.authority === 3}
                        <h2 class="c333 f15 tm mb8">등급<span class="cr f14 tm inblock">* (관리자)</span>
                        </h2>
                    {:else if modifyData.authority === 4}
                        <h2 class="c333 f15 tm mb8">등급<span class="cr f14 tm inblock">* (일반)</span>
                        </h2>
                    {/if}
                    <div class="flex aic g12">
                        <div class="check-type-2">
                            <input type="radio" id="radio1" name="rating" value="4" bind:group={modifyData.authority}>
                            <label for="radio1" class="flex aic g4">
                                <span class="img-box w16">
                                    <img src="/img/ico_check_2.svg" alt="">
                                </span>
                                <span class="text f14 cA2A9B0">일반</span>
                            </label>
                        </div>
                        <div class="check-type-2">
                            <input type="radio" id="radio2" name="rating" value="3" bind:group={modifyData.authority}>
                            <label for="radio2" class="flex aic g4">
                                <span class="img-box w16">
                                    <img src="/img/ico_check_2.svg" alt="">
                                </span>
                                <span class="text f14 cA2A9B0">관리자</span>
                            </label>
                        </div>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">아이디<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" placeholder="아이디" style="background-color: floralwhite"
                                   bind:value={modifyData.username} disabled>
                        </div>
                        <button class="btn-type-1 w80 f14 bdr4 b333 cfff" disabled>확인
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
                    <h2 class="c333 f15 tm mb8">생년월일<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input id="birthdayInput" type="date" placeholder="생년월일" bind:value={modifyData.birthday}>
                    </div>
                </div>
            </div>
            <div class="btn-area flex aic jcc g8 mt40">
                <button type="submit" class="w120 h40 btn-type-2 bdr4 bm cfff tm f14">
                    수정
                </button>
                <button type="button" class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" on:click="{deactivateModal}">취소
                </button>
            </div>
        </form>
    </div>

    <!-- 회원 비밀번호 모달 -->
    <form class="modal-type-1 modal-box abs xy-middle bfff zi9 w480" class:active="{isActiveNewPassword}"
          on:submit|preventDefault={passwordModifySubmit}>
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
                        <input type="password" placeholder="비밀번호" bind:value={modifyData.password}
                                on:input={validateModifyPassword}>
                    </div>
                    {#if passwordErrorMessage}
                        <span class="f13 mt4 cr">{passwordErrorMessage}</span>
                    {/if}
                    {#if passwordSuccessMessage}
                        <span class="f13 mt4 cg">{passwordSuccessMessage}</span>
                    {/if}
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">비밀번호 확인<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per mt8">
                        <input type="password" placeholder="비밀번호 확인" bind:value={modifyData.passwordConfirm}
                                on:input={confirmValidateModifyPassword}>
                        {#if passwordConfirmErrorMessage}
                            <span class="f13 mt4 cr">{passwordConfirmErrorMessage}</span>
                        {/if}
                        {#if passwordConfirmSuccessMessage}
                            <span class="f13 mt4 cg">{passwordConfirmSuccessMessage}</span>
                        {/if}
                    </div>
                </div>
                
            </div>
        </div>
        <div class="btn-area flex aic jcc g8 mt40 mb16">
            <button type="submit" class="w120 h40 btn-type-2 bdr4 bm cfff tm f14">수정</button>
            <button type="button" class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" on:click="{deactivateModal}">취소
            </button>
        </div>
    </form>
</div>
<div class="store-management-area cnt-area w100per">
    <div class="title-box flex aic jcsb">
        <h1 class="tb c121619">회원 관리</h1>
    </div>
    <div class="cnt-box-1 cnt-box">
        <div class="top-area">
            <div class="space-area-2 flex aic jce">
                <div class="right-box flex aic">
                    <form>
                        <div class="search-type-1 flex aic">
                            <div class="search-box w200">
                                <input type="search" placeholder="검색어 입력" bind:value={keyword}/>
                            </div>
                            <button class="search-btn flex aic jcc" on:click={() => changePage(0)}>
								<span class="ico-box img-box w16">
									<img src="/img/ico_search.svg" alt="검색 아이콘"/>
								</span>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="line"></div>
        <div class="middle-area">
            <div class="all-text c121619 f14">
                전체 <span class="number inblock cm tm">{members.length}</span>명
            </div>
            <div class="table-box-1 table-type-1 scr-type-2 mt12">
                <table>
                    <thead>
                    <tr>
                        <th class="wsn" style="width: 44px;">
                            <div class="check-type-1">
                                <input type="checkbox" id="all" on:click="{toggleAll}">
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
                    {#each memberList as member}
                        {#if member.authority !== 1 && member.authority !== 2}
                            <tbody>
                            <tr>
                                <td class="wsn" style="width: 44px;">
                                    <div class="check-type-1">
                                        <input type="checkbox" id="checkbox_{member.id}"
                                               on:change={(e) => handleCheckboxChange(e, member.id)}>
                                        <label for="checkbox_{member.id}"></label>
                                    </div>
                                </td>
                                <td class="wsn">{member.name}</td>
                                <td class="wsn">{member.position}</td>
                                {#if member.authority === 4}
                                    <td class="wsn">일반</td>
                                {:else if member.authority === 3}
                                    <td class="wsn">관리자</td>
                                {/if}
                                <td class="wsn">{member.username}</td>
                                <td class="wsn">{member.birthday}</td>
                                <td class="wsn tac">
                                    <button class="w40 h24 btn-type-2 bdr4 bdbbb cbbb f13"
                                            on:click="{activateModalModifi}" id="modify_{JSON.stringify(member)}">
                                        수정
                                    </button>
                                </td>
                                <td class="wsn tac">
                                    <button class="w50 h24 btn-type-2 bdr4 bdbbb cbbb f13"
                                            on:click="{activateModalNewPassword}"
                                            id="password_{JSON.stringify(member)}">초기화
                                    </button>
                                </td>
                            </tr>
                            </tbody>
                        {/if}
                    {/each}
                </table>
            </div>
            <div class="flex aic jcsb mt8">
                <div class="flex aic g4">
                    <button class="w50 h30  btn-type-1 bdA2A9B0 bdr4 f12 cA2A9B0"
                            on:click={deleteMember}>삭제
                    </button>
                </div>
                <div class="flex aic g4">
                    <button class="w50 h30 btn-type-1 bm bdr4 f12 cfff"
                            on:click="{activateModalAdd}">등록
                    </button>
                </div>
            </div>
            <div class="paging-box flex jcc mt40">
                <ul class="flex aic jcc">
                    {#if resList.number > 0}
                        <li class="page-btn" on:click={() => changePage(resList.number - 1)}>
                            <a href="">이전</a>
                        </li>
                    {/if}
                    {#each generatePageButtons(resList.totalPages) as button}
                        <li
                                class="num"
                                on:click={() => resList.number !== button - 1 && changePage(button - 1)}
                        >
                            <a href="" class:active={resList.number === button - 1}>{button}</a>
                        </li>
                    {/each}
                    {#if resList.number < resList.totalPages - 1}
                        <li class="page-btn" on:click={() => changePage(resList.number + 1)}>
                            <a href="">다음</a>
                        </li>
                    {/if}
                </ul>
            </div>
        </div>
    </div>
</div>