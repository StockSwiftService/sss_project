<script>
    let codeFormData = {
        name: '',
        businessNumber: '',
        email: ''
    }

    let idFormData = {
        companyCode: '',
        repName: '',
        email: ''
    }

    let pwFormData = {
        companyCode: '',
        username: '',
        email: '',
        password: '',
        passwordConfirm: ''
    }

    let data = []
    let CodeDisplay = false;
    let IdDisplay = false;
    let PwDisplay = false;
    let isCodeClick = true;
    let isIdClick = false;
    let isPwClick = false;

    //비번
    let pwSuccessMessage = ''
    let pwErrorMessage = ''
    //비번 확인
    let pwConfirmSuccessMessage = ''
    let pwConfirmErrorMessage = ''

    //회사 코드 찾기
    const codeSearch = async () => {
        isCodeClick = true
        try {
            const response = await fetch('http://localhost:8080/api/v1/company/code-search', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(codeFormData)
            });
            if (response.ok) {
                const data = await response.json();
                console.log(data)

                if (data.resultCode === 'S-9') {
                    alert(data.data.company)
                    CodeDisplay = true;
                } else {
                    const errorMessage = data.errorMessage;
                    console.error('찾기 실패:', errorMessage);
                }
            } else {
                console.error('서버 응답 오류:', response.statusText);
                alert('값이 없거나 올바르지 않습니다. 다시 입력해 주세요.');
                return;
            }
        } catch (error) {
            alert('다시 입력해 주세요.')
            console.error('오류 발생:', error);
        }
    }

    //아이디 찾기
    const idSearch = async () => {
        isIdClick = true
        try {
            const response = await fetch('http://localhost:8080/api/v1/company/id-search', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(idFormData)
            });
            if (response.ok) {
                const data = await response.json();
                console.log(data)

                if (data.resultCode === 'S-11') {
                    alert(data.data.member)
                    IdDisplay = true;
                } else {
                    const errorMessage = data.errorMessage;
                    console.error('찾기 실패:', errorMessage);
                }
            } else {
                console.error('서버 응답 오류:', response.statusText);
                alert('값이 없거나 올바르지 않습니다. 다시 입력해 주세요.');
                return;
            }
        } catch (error) {
            alert('다시 입력해 주세요.')
            console.error('오류 발생:', error);
        }
    }

    //비번 찾기
    const PwSearch = async () => {
        isPwClick = true
        try {
            const response = await fetch('http://localhost:8080/api/v1/company/pw-search', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(pwFormData)
            });
            if (response.ok) {
                const data = await response.json();
                console.log(data)

                if (data.resultCode === 'S-12') {
                    PwDisplay = true;
                } else {
                    const errorMessage = data.errorMessage;
                    console.error('찾기 실패:', errorMessage);
                    alert('값이 없거나 올바르지 않습니다. 다시 입력해 주세요.')
                }
            } else {
                console.error('서버 응답 오류:', response.statusText);
                alert('값이 없거나 올바르지 않습니다. 다시 입력해 주세요.');
                return;
            }
        } catch (error) {
            alert('다시 입력해 주세요.')
            console.error('오류 발생:', error);
        }
    }

    //비번 검증
    function validatePassword() {
        const passwordRegex = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{8,16}$/;
        if (!passwordRegex.test(pwFormData.password)) {
            pwSuccessMessage = ''
            pwErrorMessage = '비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용해야 합니다';
        } else {
            pwErrorMessage = ''
            return pwFormData.password;
        }
    }

    //비번 확인 검증
    function confirmValidatePassword() {
        if (pwFormData.password === pwFormData.passwordConfirm) {
            pwConfirmSuccessMessage = '비밀번호가 일치합니다'
            pwConfirmErrorMessage = '';
        } else {
            pwConfirmSuccessMessage = ''
            pwConfirmErrorMessage = '비밀번호가 일치하지 않습니다';
        }
    }

    //비번 수정
    const PwModify = async () => {
        isPwClick = true
        try {
            const response = await fetch('http://localhost:8080/api/v1/company/pw-modify', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(pwFormData)
            });
            if (response.ok) {
                const data = await response.json();
                console.log(data)

                if (data.resultCode === 'S-14') {
                    alert('비밀번호 변경이 완료되었습니다. 로그인 해주세요.')
                    window.location.href = '/';
                } else {
                    const errorMessage = data.errorMessage;
                    console.error('찾기 실패:', errorMessage);
                }
            } else {
                console.error('서버 응답 오류:', response.statusText);
                alert('값이 없거나 올바르지 않습니다. 다시 입력해 주세요.');
                return;
            }
        } catch (error) {
            alert('다시 입력해 주세요.')
            console.error('오류 발생:', error);
        }
    }

    //버튼 클릭시 이벤트
    function CodeMenuClick(event) {
        isCodeClick = true
        isIdClick = false
        isPwClick = false
        const id = event.target.id;
        document.getElementById(id).classList.add('active');
        document.getElementById('id-search').classList.remove('active');
        document.getElementById('pw-search').classList.remove('active');
    }

    function IdMenuClick(event) {
        isCodeClick = false
        isPwClick = false
        isIdClick = true
        const id = event.target.id;
        document.getElementById(id).classList.add('active');
        document.getElementById('code-search').classList.remove('active');
        document.getElementById('pw-search').classList.remove('active');
    }

    function PwMenuClick(event) {
        isPwClick = true
        isCodeClick = false
        isIdClick = false
        const id = event.target.id;
        document.getElementById(id).classList.add('active');
        document.getElementById('id-search').classList.remove('active');
        document.getElementById('code-search').classList.remove('active');
    }

</script>

<style>
    .tab-menu-box > li {
        width: 33.3333%;
        text-align: center;
    }

    .tab-menu-box > li > button {
        font-size: 16px;
        color: #bbb;
        box-sizing: border-box;
        margin: auto;
        padding: 16px 0;
    }

    .tab-menu-box > li > button.active {
        font-family: "SUIT-Bold";
        color: #2656F6;
        border-bottom: 4px solid #2656F6;
    }
</style>

<div class="w100per bsb pt120 pb120 mgc" style="max-width: 480px;">
    <h1 class="c121619 tb tac f32">정보 찾기</h1>
    <p class="tac f16 c555 mt28 tac">회사의 대표자가 아닌 직원의 경우 대표자에게 문의하세요.</p>
    <ul class="tab-menu-box flex mt60">
        <li>
            <button id="code-search" type="button" class="active" on:click={CodeMenuClick}>회사코드 찾기</button>
        </li>
        <li>
            <button id="id-search" type="button" on:click={IdMenuClick}>아이디 찾기</button>
        </li>
        <li>
            <button id="pw-search" type="button" on:click={PwMenuClick}>비밀번호 찾기</button>
        </li>
    </ul>
    <ul class="tab-cnt-box mt60">

        <!-- 회사코드 찾기 -->
        <li>
            {#if isCodeClick}
                <!-- 찾기 전 -->
                <form on:submit|preventDefault={codeSearch}>
                    <div class="flex fdc g8">
                        <div>
                            <div class="input-type-1 w100per">
                                <input type="text" placeholder="회사명" bind:value={codeFormData.name}>
                            </div>
                        </div>
                        <div>
                            <div class="input-type-1 w100per">
                                <input type="text" placeholder="사업자 번호 (-자 빼고 입력해 주세요.)"
                                       bind:value={codeFormData.businessNumber}>
                            </div>
                        </div>
                        <div>
                            <div class="input-type-1 w100per">
                                <input type="text" placeholder="이메일" bind:value={codeFormData.email}>
                            </div>
                        </div>
                    </div>
                    {#if CodeDisplay === false}
                        <button type="submit" class="btn-type-1 bm w100per h50 bdr4 mt60" id="test">
                            <span class="text f16 tb cfff">회사코드 찾기</span>
                        </button>
                    {/if}
                </form>

                <!-- 결과 -->
                {#if CodeDisplay === true}
                    <div>
                        <a href="/" class="btn-type-1 bm w100per h50 bdr4 mt60" id="test">
                            <span class="text f16 tb cfff">로그인 하기</span>
                        </a>
                    </div>
                {/if}
            {/if}
        </li>

        <!-- 아이디 찾기 -->
        <li>
            {#if isIdClick}
                <!-- 찾기 전 -->
                <form on:submit|preventDefault={idSearch}>
                    <div class="flex fdc g8">
                        <div>
                            <div class="input-type-1 w100per">
                                <input type="text" placeholder="회사코드" bind:value={idFormData.companyCode}>
                            </div>
                        </div>
                        <div>
                            <div class="input-type-1 w100per">
                                <input type="text" placeholder="이메일" bind:value={idFormData.email}>
                            </div>
                        </div>
                        <div>
                            <div class="input-type-1 w100per">
                                <input type="text" placeholder="대표자명" bind:value={idFormData.repName}>
                            </div>
                        </div>
                    </div>
                    {#if IdDisplay === false}
                        <button type="submit" class="btn-type-1 bm w100per h50 bdr4 mt60" id="test">
                            <span class="text f16 tb cfff">아이디 찾기</span>
                        </button>
                    {/if}
                </form>

                <!-- 결과 -->
                {#if IdDisplay === true}
                    <div>
                        <a href="/" class="btn-type-1 bm w100per h50 bdr4 mt60" id="test">
                            <span class="text f16 tb cfff">로그인 하기</span>
                        </a>
                    </div>
                {/if}
            {/if}
        </li>

        <!-- 비밀번호 찾기 -->
        <li>
            {#if PwDisplay === false && isPwClick}
                <!-- 찾기 전 -->
                <form on:submit|preventDefault={PwSearch}>
                    <div class="flex fdc g8">
                        <div>
                            <div class="input-type-1 w100per">
                                <input type="text" placeholder="회사코드" bind:value={pwFormData.companyCode}>
                            </div>
                        </div>
                        <div>
                            <div class="input-type-1 w100per">
                                <input type="text" placeholder="아이디" bind:value={pwFormData.username}>
                            </div>
                        </div>
                        <div>
                            <div class="input-type-1 w100per">
                                <input type="text" placeholder="이메일" bind:value={pwFormData.email}>
                            </div>
                        </div>
                    </div>
                    <button type="submit" class="btn-type-1 bm w100per h50 bdr4 mt60" id="test">
                        <span class="text f16 tb cfff">비밀번호 찾기</span>
                    </button>
                </form>
            {/if}
            <!-- 결과 -->
            {#if PwDisplay === true && isPwClick}
                <form on:submit|preventDefault={PwModify}>
                    <div class="flex fdc g8">
                        <div>
                            <div class="input-type-1 w100per">
                                <input type="password" placeholder="비밀번호" bind:value={pwFormData.password} on:input={validatePassword}>
                            </div>
                            {#if pwErrorMessage}
                                <span class="f13 mt4 cr">{pwErrorMessage}</span>
                            {/if}
                            {#if pwSuccessMessage}
                                <span class="f13 mt4 cg">{pwSuccessMessage}</span>
                            {/if}
                        </div>
                        <div>
                            <div class="input-type-1 w100per">
                                <input type="password" placeholder="비밀번호 확인" bind:value={pwFormData.passwordConfirm} on:input={confirmValidatePassword}>
                            </div>
                            {#if pwConfirmErrorMessage}
                                <span class="f13 mt4 cr">{pwConfirmErrorMessage}</span>
                            {/if}
                            {#if pwConfirmSuccessMessage}
                                <span class="f13 mt4 cg">{pwConfirmSuccessMessage}</span>
                            {/if}
                        </div>
                    </div>
                    <!-- 비밀번호 변경되면 확인 버튼 누르면 로그인 페이지로 이동 -->
                    <button type="submit" class="btn-type-1 bm w100per h50 bdr4 mt60" id="test">
                        <span class="text f16 tb cfff">비밀번호 변경</span>
                    </button>
                </form>
            {/if}
        </li>
    </ul>
</div>
