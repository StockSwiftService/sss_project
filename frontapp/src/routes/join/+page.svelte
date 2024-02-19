<script>
    import {getFirstAndLastTokens} from "eslint-plugin-svelte/lib/rules/indent-helpers/commons.js";

    let formData = {
        name: '',
        businessNumber: '',
        repName: '',
        username: '',
        email: '',
        companyCode: '',
        password: '',
        passwordConfirm: '',
        birthday: '',
        address: '',
        detailAddress: ''
    };
    //인증번호 담을 변수
    let emailNum;

    //기업명 검증
    let confirmNameErrorMessage = ''
    let confirmNameSuccessMessage = ''
    //사업자 번호 검증
    let businessNumberErrorMessage = ''
    let businessNumberSuccessMessage = ''
    //이메일 검증
    let emailSuccessMessage = ''
    let emailErrorMessage = ''
    //이메일 인증코드 검증
    let emailNumberSuccessMessage = ''
    let emailNumberErrorMessage = ''
    //대표자 아이디 검증
    let usernameErrorMessage = ''
    let usernameSuccessMessage = ''
    //패스워드
    let passwordSuccessMessage = '';
    let passwordErrorMessage = '';
    //패스워드 확인
    let passwordConfirmErrorMessage = '';
    let passwordConfirmSuccessMessage = '';

    let isEmailConfirmed = false; // 이메일 인증 여부
    let isbusinessNumberConfirm = false; // 사업자 번호 인증 여부
    let isAddress = false;

    let element_layer;

    //주소 찾기 스크립트 추가
    function loadScript() {
        return new Promise((resolve, reject) => {
            const script = document.createElement('script');
            script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
            script.async = true;  //비동기로 로드됨
            script.onload = resolve;  //호출 성공
            script.onerror = reject;  //호출 실패
            document.body.appendChild(script);  //생성된 script를 body에 추가
        });
    }

    //클릭 시 창 열림
    async function initDaumPostcode() {
        element_layer = document.getElementById('layer');
        await loadScript();
        new daum.Postcode({
            oncomplete: function (data) {
                var addr = ''; // 주소 변수

                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                document.getElementById('postcode').value = data.zonecode;
                document.getElementById("address").value = addr;
                formData.address = addr;
                document.getElementById("detailAddress").focus();
                isAddress = true;
                element_layer.style.display = 'none';
            },
            width: '100%',
            height: '100%',
            maxSuggestItems: 5
        }).embed(element_layer);

        element_layer.style.display = 'block';
        initLayerPosition();

        // 외부 클릭 이벤트를 감지
        window.addEventListener('click', closeOnOutsideClick);
    }

    //창 닫기
    function closeOnOutsideClick(event) {
        // 클릭 이벤트가 주소 검색 창 외부에서 발생했을 경우, 주소 검색 창 닫기
        if (element_layer && !element_layer.contains(event.target)) {
            element_layer.style.display = 'none';
            // 외부 클릭 이벤트 리스너를 제거
            window.removeEventListener('click', closeOnOutsideClick);
        }
    }

    function initLayerPosition() {
        var width = 300; //우편번호서비스가 들어갈 element의 width
        var height = 400; //우편번호서비스가 들어갈 element의 height
        var borderWidth = 5; //샘플에서 사용하는 border의 두께

        element_layer.style.width = width + 'px';
        element_layer.style.height = height + 'px';
        element_layer.style.border = borderWidth + 'px solid';

        element_layer.style.left = (((window.innerWidth || document.documentElement.clientWidth) - width) / 2 - borderWidth) + 'px';
        element_layer.style.top = (((window.innerHeight || document.documentElement.clientHeight) - height) / 2 - borderWidth) + 'px';
    }


    //기업명 중복 검사
    async function checkNameDuplicate() {
        try {
            if (!formData.name.trim()) {
                confirmNameErrorMessage = '필수 항목입니다'
                confirmNameSuccessMessage = ''
            }
            const response = await fetch('http://localhost:8080/api/v1/company/check-name', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({name: formData.name}),
            });
            if (response.ok) {
                const data = await response.json();
                if (data.resultCode == 'S-2') {
                    confirmNameErrorMessage = '중복된 아이디 입니다'
                    confirmNameSuccessMessage = ''
                } else {
                    confirmNameErrorMessage = ''
                    confirmNameSuccessMessage = '사용가능한 아이디 입니다'
                }
            }
        } catch (error) {
            console.error('오류 발생:', error);
        }
    }

    // 사업자 번호 검사
    async function businessNumberConfirm() {
        try {
            if (!formData.businessNumber.trim()) {
                businessNumberErrorMessage = '필수 항목입니다'
                businessNumberSuccessMessage = ''

            } else if (!/^\d{10}$/.test(formData.businessNumber.trim())) {
                businessNumberErrorMessage = '사업자 번호를 모두 입력하세요(10자리)';
                businessNumberSuccessMessage = '';
            }
            const response = await fetch('http://localhost:8080/api/v1/company/check-businessNumber', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({businessNumber: formData.businessNumber}),
            });
            if (response.ok) {
                const data = await response.json();
                if (data.resultCode == 'S-3') {
                    businessNumberErrorMessage = ''
                    businessNumberSuccessMessage = '등록 가능한 사업자 번호 입니다'
                    isbusinessNumberConfirm = true
                } else {
                    businessNumberErrorMessage = '사용하실 수 없는 사업자 번호 입니다'
                    businessNumberSuccessMessage = ''
                    isbusinessNumberConfirm = false
                }
            }
        } catch (error) {
            console.error('오류 발생:', error);
        }
    }

    //이메일
    async function emailButton() {

        try {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // 이메일 형식을 나타내는 정규 표현식
            if (!emailRegex.test(formData.email.trim())) {
                emailErrorMessage = '올바른 이메일 형식을 입력해 주세요'
                emailSuccessMessage = ''
            }
            const response = await fetch('http://localhost:8080/api/v1/email/send', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({email: formData.email}),
            });

            if (response.ok) {
                const data = await response.json();
                if (data.resultCode === 'S-5') {
                    emailSuccessMessage = ''
                    emailErrorMessage = '이미 가입된 메일입니다';
                } else if (data.resultCode === 'S-6' && data.resultCode !== null) {
                    emailSuccessMessage = '메일 발송이 완료되었습니다';
                    emailErrorMessage = ''

                    const num = data.data.number
                    emailNum = num;
                    console.log(emailNum)
                    return formData.email;
                } else {
                    emailSuccessMessage = ''
                    emailErrorMessage = '메일이 존재하지 않습니다';
                    console.error('서버 응답 오류:', response.statusText);
                }
            }
        } catch (error) {
            emailErrorMessage = '이미 가입된 이메일 입니다';
            emailSuccessMessage = ''
            console.error('오류 발생:', error);
        }
    }

    //이메일 인증번호 확인
    async function emailConfirm() {
        try {
            const userVerificationCode = document.getElementById('userVerificationCode').value;
            if (userVerificationCode === emailNum && userVerificationCode !== null) {
                emailNumberSuccessMessage = '인증이 완료되었습니다';
                emailNumberErrorMessage = '';
                isEmailConfirmed = true;
            } else {
                emailNumberSuccessMessage = '';
                emailNumberErrorMessage = '인증번호가 일치하지 않습니다';
                isEmailConfirmed = false;
            }
        } catch (error) {
            console.error('오류 발생:', error);
        }
    }

    //대표자 아이디 검증
    async function checkUsernameDuplicate() {
        try {
            const response = await fetch('http://localhost:8080/api/v1/company/check-username', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({username: formData.username}),
            });
            if (response.ok) {
                const data = await response.json();
                if (data.resultCode == 'S-7') {
                    usernameErrorMessage = '중복된 아이디 입니다'
                    usernameSuccessMessage = ''
                } else {
                    usernameErrorMessage = ''
                    usernameSuccessMessage = '사용가능한 아이디 입니다'
                }
            }
        } catch (error) {
            console.error('오류 발생:', error);
        }

    }

    function validatePassword() {
        const passwordRegex = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{8,16}$/;
        if (!passwordRegex.test(formData.password)) {
            passwordSuccessMessage = ''
            passwordErrorMessage = '비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용해야 합니다';
        } else {
            passwordErrorMessage = ''
            return formData.password;
        }
    }

    //비번 확인
    function confirmValidatePassword() {
        if (formData.password === formData.passwordConfirm) {
            passwordConfirmSuccessMessage = '비밀번호가 일치합니다'
            passwordConfirmErrorMessage = '';
        } else {
            passwordConfirmSuccessMessage = ''
            passwordConfirmErrorMessage = '비밀번호가 일치하지 않습니다';
        }
    }


    //회원가입 제출
    const joinSubmit = async () => {

        if (formData != null && !isAddress) {
            alert('주소를 입력해 주세요.')
        } else if (formData != null && !isbusinessNumberConfirm) {
            alert('사업자 번호를 다시 확인 해주세요.')
        } else if (formData != null && !isEmailConfirmed) {
            alert('이메일 코드를 다시 확인 해주세요.')
        } else {
            try {
                const response = await fetch('http://localhost:8080/api/v1/company/join', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(formData)
                });
                if (response.ok) {
                    const data = await response.json();
                    console.log(data)
                    // 회원가입 성공
                    if (data.resultCode === 'S-1') {
                        // window.location.href = '/';
                        alert('회원가입 신청이 완료되었습니다. 관리자의 승인 후 서비스를 이용하실 수 있습니다. 승인 여부는 입력하신 메일로 발송이 되며, 신청일 기준 2-3 소요될 수 있습니다.');
                    } else {
                        // 회원가입 실패
                        const errorMessage = data.errorMessage;
                        console.error('가입 실패:', errorMessage);
                    }
                } else {
                    console.error('서버 응답 오류:', response.statusText);
                    alert('양식을 모두 입력 해주세요.');
                    return;
                }
            } catch (error) {
                console.error('오류 발생:', error);
            }
        }
    }
</script>
<div class="w100per bsb pt120 pb120 mgc" style="max-width: 480px;">
    <h1 class="c121619 tb tac f32">회원가입</h1>
    <form class="flex fdc g36 mt60" on:submit|preventDefault={joinSubmit}>
        <button type="submit" class="hidden"></button>
        <div>
            <h2 class="c333 f16 tm mb8">회사명<span class="cr f16 tm inblock">*</span></h2>
            <div class="input-type-1 w100per">
                <input type="text" placeholder="회사명" bind:value={formData.name} on:input={checkNameDuplicate}>
            </div>
            {#if confirmNameErrorMessage}
                <span class="f13 mt4 cr">{confirmNameErrorMessage}</span>
            {/if}
            {#if confirmNameSuccessMessage}
                <span class="f13 mt4 cg">{confirmNameSuccessMessage}</span>
            {/if}
        </div>

        <div id="layer" style="display: none; position: fixed; overflow: hidden; z-index: 1;">
        </div>
        <div>
            <h2 class="c333 f16 tm mb8">사업장 소재지<span class="cr f16 tm inblock">*</span></h2>
            <div class="flex g8">
                <div class="input-type-1 w100per">
                    <input type="text" id="postcode" placeholder="우편번호" style="background-color: floralwhite"
                           disabled>
                </div>
                <button class="btn-type-1 w80 f15 bdr4 b333 cfff" on:click|preventDefault={initDaumPostcode}>주소 검색
                </button>
                <br>
            </div>
        </div>

        <div>
            <h3 class="c333 f16 tm mb8">[상세 주소]</h3>
            <div class="input-type-1 w100per">
                <input type="text" id="address" placeholder="주소" style="background-color: floralwhite" disabled
                >
            </div>
            <br>
            <div class="input-type-1 w100per">
                <input type="text" id="detailAddress" placeholder="상세주소" bind:value={formData.detailAddress}>
            </div>
        </div>

        <div>
            <h2 class="c333 f16 tm mb8">사업자 번호<span class="cr f16 tm inblock">*</span></h2>
            <div class="flex g8">
                <div class="input-type-1 w100per">
                    <input type="text" placeholder="사업자 번호 (-자 빼고 입력해 주세요.)" bind:value={formData.businessNumber}>
                </div>
                <button class="btn-type-1 w80 f15 bdr4 b333 cfff" on:click|preventDefault={businessNumberConfirm}>확인
                </button>
            </div>
            {#if businessNumberErrorMessage}
                <span class="f13 mt4 cr">{businessNumberErrorMessage}</span>
            {/if}
            {#if businessNumberSuccessMessage}
                <span class="f13 mt4 cg">{businessNumberSuccessMessage}</span>
            {/if}
        </div>

        {#if !isEmailConfirmed}
            <div>
                <h2 class="c333 f16 tm mb8">이메일<span class="cr f16 tm inblock">*</span></h2>
                <div class="flex g8">
                    <div class="input-type-1 w100per">
                        <input type="text" placeholder="이메일" bind:value={formData.email}>
                    </div>
                    <button class="btn-type-1 w80 f15 bdr4 b333 cfff" on:click|preventDefault={emailButton}>
                        전송
                    </button>
                </div>
                {#if emailErrorMessage}
                    <span class="f13 mt4 cr">{emailErrorMessage}</span>
                {/if}
                {#if emailSuccessMessage}
                    <span class="f13 mt4 cg">{emailSuccessMessage}</span>
                {/if}

                <div class="flex g8 mt8">
                    <div class="input-type-1 w100per">
                        <input type="text" placeholder="인증 번호" id="userVerificationCode">
                    </div>
                    <button class="btn-type-1 w80 f15 bdr4 b333 cfff" on:click|preventDefault={emailConfirm}>
                        확인
                    </button>
                </div>
                {#if emailNumberErrorMessage}
                    <span class="f13 mt4 cr">{emailNumberErrorMessage}</span>
                {/if}
                {#if emailNumberSuccessMessage}
                    <span class="f13 mt4 cg">{emailNumberSuccessMessage}</span>
                {/if}
            </div>
        {/if}

        {#if isEmailConfirmed}
            <div>
                <h2 class="c333 f16 tm mb8">이메일<span class="cr f16 tm inblock">*</span></h2>
                <div class="flex g8">
                    <div class="input-type-1 w100per">
                        <input type="text" placeholder="이메일" style="background-color: floralwhite" bind:value={formData.email} disabled>
                    </div>
                    <button class="btn-type-1 w80 f15 bdr4 b333 cfff" disabled on:click|preventDefault={emailButton}>
                        전송
                    </button>
                </div>
                {#if emailErrorMessage}
                    <span class="f13 mt4 cr">{emailErrorMessage}</span>
                {/if}
                {#if emailSuccessMessage}
                    <span class="f13 mt4 cg">{emailSuccessMessage}</span>
                {/if}

                <div class="flex g8 mt8">
                    <div class="input-type-1 w100per">
                        <input type="text" placeholder="인증 완료" id="userVerificationCode" style="background-color: floralwhite" disabled>
                    </div>
                    <button class="btn-type-1 w80 f15 bdr4 b333 cfff" disabled on:click|preventDefault={emailConfirm}>
                        확인
                    </button>
                </div>
                <!--{#if emailNumberErrorMessage}-->
                <!--    <span class="f13 mt4 cr">{emailNumberErrorMessage}</span>-->
                <!--{/if}-->
                <!--{#if emailNumberSuccessMessage}-->
                <!--    <span class="f13 mt4 cg">{emailNumberSuccessMessage}</span>-->
                <!--{/if}-->
            </div>
        {/if}

        <div>
            <h2 class="c333 f16 tm mb8">대표자명<span class="cr f16 tm inblock">*</span></h2>
            <div class="input-type-1 w100per">
                <input type="text" placeholder="대표자명" bind:value={formData.repName}>
            </div>
        </div>
        <div>
            <h2 class="c333 f16 tm mb8">아이디<span class="cr f16 tm inblock">*</span></h2>
            <div class="input-type-1 w100per">
                <input type="text" placeholder="아이디" bind:value={formData.username}
                       on:input={checkUsernameDuplicate}>
            </div>
            {#if usernameErrorMessage}
                <span class="f13 mt4 cr">{usernameErrorMessage}</span>
            {/if}
            {#if usernameSuccessMessage}
                <span class="f13 mt4 cg">{usernameSuccessMessage}</span>
            {/if}
        </div>
        <div>
            <h2 class="c333 f16 tm mb8">비밀번호<span class="cr f16 tm inblock">*</span></h2>
            <div class="input-type-1 w100per">
                <input type="password" placeholder="비밀번호" bind:value={formData.password}
                       on:input={validatePassword}>
            </div>
            {#if passwordErrorMessage}
                <span class="f13 mt4 cr">{passwordErrorMessage}</span>
            {/if}
            {#if passwordSuccessMessage}
                <span class="f13 mt4 cg">{passwordSuccessMessage}</span>
            {/if}
            <div class="input-type-1 w100per mt8">
                <input type="password" placeholder="비밀번호 확인" bind:value={formData.passwordConfirm}
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
            <h2 class="c333 f16 tm mb8">생년월일<span class="cr f16 tm inblock">*</span></h2>
            <div class="input-type-1 w100per">
                <input type="date" placeholder="생년월일" bind:value={formData.birthday}>
            </div>
        </div>
        <button type="submit" class="btn-type-1 bm w100per h50 bdr8 mt60">
            <span class="text f16 tb cfff">회원가입</span>
        </button>
    </form>
</div>