<script>
	import { onMount } from 'svelte';
	import { loginUser } from '../stores.js';

	const backendUrl = import.meta.env.VITE_BACKEND_URL;

	const logout = async () => {
		try {
			const response = await fetch(`${backendUrl}/api/v1/member/logout`, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				}
			});
		} catch (error) {
			console.error('오류 발생:', error);
			alert('다시 시도 해주세요2.');
		}
	};
</script>

<header class="header-area w100per">
	<div class="cnt-in wh100per flex aic jce rel">
		<div class="flex aic">
			<a
				href="/"
				on:click={() => {
					logout();
				}}
				class="user-infor-box flex aic"
			>
				<div class="text-box">
					<span class="text-1 c121619 tm">{$loginUser.name}</span>
					<span class="text-2 c121619 tar op60">{$loginUser.position}</span>
				</div>
			</a>
			<a
				href="/"
				on:click={() => {
					logout();
				}}
				class="logout-btn img-box"
			>
				<img src="/img/ico_logout.svg" alt="로그아웃 아이콘" />
			</a>
		</div>
	</div>
</header>