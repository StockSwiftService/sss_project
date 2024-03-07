<script>
	import { onMount } from 'svelte';
	import { loginUser } from '../stores'; // 스토어 import

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
                loginUser.set(data.data.member); // 스토어 업데이트
            } else {
                console.error('서버 응답 오류:', response.statusText);
            }
        } catch (error) {
            console.error('오류 발생:', error);
        }
    });
</script>