<script>
	import pkg from 'cookie';
	const { setCookie, getCookie } = pkg;

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
				if (data.resultCode === 'S-1') {
					window.location.href = '/using/user_manage';
				} else {
					// 로그인이 실패한 경우
					const errorMessage = data.errorMessage;
					console.error('로그인 실패:', errorMessage);

					alert('회사 코드, 아이디, 비밀번호가 일치하지 않습니다.');
				}
			} else {
				console.error('서버 응답 오류:', response.statusText);
				if(!response.ok && response.status != 401) {
				alert('다시 입력 해주세요.');
				}
			}
		} catch (error) {
			console.error('오류 발생:', error);
			alert('존재하지 않는 계정입니다.');
		}
	};

	function check() {
		console.log(1);
	}
	function checkboxID(event) {
		rememberID = event.target.checked;
		if (rememberID) {
			setCookie('username', formData.username, { expires: 365 }); // 1년 동안 쿠키를 유지하도록 설정
		} else {
			setCookie('username', '', { expires: -1 }); // 쿠키 삭제
		}
	}

	function checkboxCompany(event) {
		rememberCompany = event.target.checked;
		if (rememberCompany) {
			setCookie('companyCode', '사용자 아이디', { expires: 365 }); // 1년 동안 쿠키를 유지하도록 설정
		} else {
			setCookie('companyCode', '', { expires: -1 }); // 쿠키 삭제
		}
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
						<div class="input-type-1 w100per">
							<input bind:value={formData.companyCode} type="text" placeholder="회사코드" />
						</div>
					</div>
					<div>
						<div class="input-type-1 w100per">
							<input bind:value={formData.username} type="text" placeholder="아이디" />
						</div>
					</div>
					<div>
						<div class="input-type-1 w100per">
							<input bind:value={formData.password} type="password" placeholder="비밀번호" />
						</div>
					</div>
				</form>
			</div>
			<div class="flex aic jcsb mt12">
				<div class="check-text-box-area flex aic g16">
					<div class="check-text-box-1 check-text-type-1">
						<input type="checkbox" id="v1" />
						<label for="v1">회사코드 저장</label>
					</div>
					<div class="check-text-box-2 check-text-type-1">
						<input type="checkbox" id="v2" />
						<label for="v2">아이디 저장</label>
					</div>
				</div>
				<div class="flex aic g4">
					<a href="" class="cbbb f14">정보 찾기</a>
					<span class="f14 cbbb">|</span>
					<a href="" class="cbbb f14">회원가입</a>
				</div>
			</div>
			<button on:click={loginButton} class="btn-type-1 bm w100per h50 bdr4 mt60">
				<span class="text f16 tb cfff">로그인</span>
			</button>
		</div>
	</div>
</div>
