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
                } else {
                    businessNumberErrorMessage = '사용하실 수 없는 사업자 번호 입니다'
                    businessNumberSuccessMessage = ''
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
            } else {
                emailNumberSuccessMessage = '';
                emailNumberErrorMessage = '인증번호가 일치하지 않습니다';
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
                // 회원가입 성공
                if (data.resultCode === 'S-1') {
                    window.location.href = '/';
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

</script>
<div class="w100per bsb pt120 pb120 mgc" style="max-width: 480px;">
    <h1 class="c121619 tb tac f32">회원가입</h1>
    <form class="flex fdc g36 mt60" on:submit|preventDefault={joinSubmit}>
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
        <div>
            <h2 class="c333 f16 tm mb8">이메일<span class="cr f16 tm inblock">*</span></h2>
            <div class="flex g8">
                <div class="input-type-1 w100per">
                    <input type="text" placeholder="이메일" bind:value={formData.email}>
                </div>
                <button class="btn-type-1 w80 f15 bdr4 b333 cfff" on:click|preventDefault={emailButton}>전송</button>
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
                <button class="btn-type-1 w80 f15 bdr4 b333 cfff" on:click|preventDefault={emailConfirm}>확인</button>
            </div>
            {#if emailNumberErrorMessage}
                <span class="f13 mt4 cr">{emailNumberErrorMessage}</span>
            {/if}
            {#if emailNumberSuccessMessage}
                <span class="f13 mt4 cg">{emailNumberSuccessMessage}</span>
            {/if}
        </div>
        <div>
            <h2 class="c333 f16 tm mb8">대표자명<span class="cr f16 tm inblock">*</span></h2>
            <div class="input-type-1 w100per">
                <input type="text" placeholder="대표자명" bind:value={formData.repName}>
            </div>
        </div>
        <div>
            <h2 class="c333 f16 tm mb8">아이디<span class="cr f16 tm inblock">*</span></h2>
            <div class="input-type-1 w100per">
                <input type="text" placeholder="아이디" bind:value={formData.username} on:input={checkUsernameDuplicate}>
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
                <input type="password" placeholder="비밀번호" bind:value={formData.password} on:input={validatePassword}>
            </div>
            {#if passwordErrorMessage}
                <span class="f13 mt4 cr">{passwordErrorMessage}</span>
            {/if}
            {#if passwordSuccessMessage}
                <span class="f13 mt4 cg">{passwordSuccessMessage}</span>
            {/if}
            <div class="input-type-1 w100per mt8">
                <input type="password" placeholder="비밀번호 확인" bind:value={formData.passwordConfirm} on:input={confirmValidatePassword}>
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