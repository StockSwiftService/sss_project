<script>
	import pkg from 'cookie';
	import { onMount } from 'svelte';
	import CryptoJS from 'crypto-js';

	const { setCookie, getCookie } = pkg;
//
	// 데이터 암호화 함수
	function encryptData(data, key) {
		return CryptoJS.AES.encrypt(data, key).toString();
	}

	// 데이터 복호화 함수
	function decryptData(encryptedData, key) {
		const bytes = CryptoJS.AES.decrypt(encryptedData, key);
		return bytes.toString(CryptoJS.enc.Utf8);
	}
	//시크릿 키
	const secret_key = import.meta.env.VITE_SECRET_KEY;

	onMount(async () => {
		formData.username = decryptData(getRememberId(), secret_key);
		if (formData.username != '') {
			rememberID = true;
		} else {
			rememberID = false;
		}

		formData.companyCode = decryptData(getRememberCompanyCode(), secret_key);
		if (formData.companyCode != '') {
			rememberCompany = true;
		} else {
			rememberCompany = false;
		}
	});

	let rememberID = false;
	let rememberCompany = false;
	let formData = {
		companyCode: '',
		username: '',
		password: ''
	};

	const loginButton = async () => {
		try {
			const response = await fetch('http://localhost:8080/api/v1/member/login', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				credentials: 'include',
				body: JSON.stringify(formData)
			});

			if (response.ok) {
				const data = await response.json();

				// 로그인이 성공한 경우
				if (data.resultCode === 'S-1-2') {
					if (rememberCompany) {
						const remComCode = encryptData(formData.companyCode, secret_key);
						setCookies('companyCode', remComCode, 365);
					} else {
						deleteCookie('companyCode');
					}
					if (rememberID) {
						const remUserId = encryptData(formData.username, secret_key);
						setCookies('userId', remUserId, 365);
					} else {
						deleteCookie('userId');
					}
					window.location.href = '/using/user_manage';
				} else if(data.resultCode === 'S-1-4' || data.resultCode === 'S-0'){
					if (rememberCompany) {
						const remComCode = encryptData(formData.companyCode, secret_key);
						setCookies('companyCode', remComCode, 365);
					} else {
						deleteCookie('companyCode');
					}
					if (rememberID) {
						const remUserId = encryptData(formData.username, secret_key);
						setCookies('userId', remUserId, 365);
					} else {
						deleteCookie('userId');
					}
					window.location.href = '/using/account_manage';

				}
				else {
					// 로그인이 실패한 경우
					const errorMessage = data.errorMessage;
					console.error('로그인 실패:', errorMessage);

					alert('회사 코드, 아이디, 비밀번호가 일치하지 않습니다.');
				}
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

	function setCookies(name, value, expirationDays) {
		const d = new Date();
		d.setTime(d.getTime() + expirationDays * 24 * 60 * 60 * 1000);
		const expires = 'expires=' + d.toUTCString();
		document.cookie = name + '=' + value + ';' + expires + ';path=/';
	}

	function deleteCookie(name) {
		document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
	}

	function getRememberId() {
		const name = 'userId' + '=';
		const decodedCookie = decodeURIComponent(document.cookie);
		const cookieArray = decodedCookie.split(';');
		for (let i = 0; i < cookieArray.length; i++) {
			let cookie = cookieArray[i].trim();
			if (cookie.indexOf(name) === 0) {
				return cookie.substring(name.length, cookie.length);
			}
		}
		return '';
	}
	function getRememberCompanyCode() {
		const name = 'companyCode' + '=';
		const decodedCookie = decodeURIComponent(document.cookie);
		const cookieArray = decodedCookie.split(';');
		for (let i = 0; i < cookieArray.length; i++) {
			let cookie = cookieArray[i].trim();
			if (cookie.indexOf(name) === 0) {
				return cookie.substring(name.length, cookie.length);
			}
		}
		return '';
	}
</script>

<div class="login-area w100per hvh100">
	<div class="cnt-in wh100per flex fdc aic jcc">
		<div class="login-box w100per bfff">
			<div class="logo-box img-box mgc" style="width: 220px;">
				<img src="/img/logo.svg" alt="" />
			</div>
			<div class="flex fdc g8 mt60">
				<form>
					<div>
						<div class="input-type-1 w100per mt4">
							<input bind:value={formData.companyCode} type="text" placeholder="회사코드" />
						</div>
					</div>
					<div>
						<div class="input-type-1 w100per mt4">
							<input bind:value={formData.username} type="text" placeholder="아이디" />
						</div>
					</div>
					<div>
						<div class="input-type-1 w100per mt4">
							<input bind:value={formData.password} type="password" placeholder="비밀번호" />
						</div>
					</div>
					<button on:click={loginButton} class="hidden"></button>
				</form>
			</div>
			<div class="flex aic jcsb mt12">
				<div class="check-text-box-area flex aic g16">
					<div class="check-text-box-1 check-text-type-1">
						<input type="checkbox" id="v1" bind:checked={rememberCompany} />
						<label for="v1">회사코드 저장</label>
					</div>
					<div class="check-text-box-2 check-text-type-1">
						<input type="checkbox" id="v2" bind:checked={rememberID} />
						<label for="v2">아이디 저장</label>
					</div>
				</div>
				<div class="flex aic g4">
					<a href="/information_search" class="cbbb f14">정보 찾기</a>
					<span class="f14 cbbb">|</span>
					<a href="/join" class="cbbb f14">회원가입</a>
				</div>
			</div>
			<button on:click={loginButton} class="btn-type-1 bm w100per h50 bdr4 mt60">
				<span class="text f16 tb cfff">로그인</span>
			</button>
		</div>
	</div>
</div>
