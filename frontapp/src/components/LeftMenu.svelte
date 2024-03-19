<script>
    let activeIndex = -1;

    function setActive(index) {
        activeIndex = index;
        localStorage.setItem('activeMenuIndex', index.toString());
    }

    import {onMount} from 'svelte';

    const backendUrl = import.meta.env.VITE_BACKEND_URL;
    let loginUser = [];
    onMount(async () => {
        try {
            const response = await fetch(`${backendUrl}/api/v1/member/loginUser`, {
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

        const savedIndex = localStorage.getItem('activeMenuIndex');
        if (savedIndex !== null) {
            activeIndex = parseInt(savedIndex, 10);
        }
    });
</script>

<style>
    .logo-box {
        width: 120px;
    }
</style>

<div class="logo-box img-box mgc">
    <img src="/img/logo.svg" alt="">
</div>
<ul class="menu-box">
    {#if loginUser.authority === 2}
        <li>
            <a href="/using/user_manage" class="flex aic" on:click="{(event) => setActive(0, event)}"
               class:active="{activeIndex === 0}">
            <span class="ico-box img-box">
                <img src="/img/menu_ico_2.svg" alt="메뉴 아이콘">
                <img src="/img/menu_ico_2_active.svg" alt="메뉴 액티브 아이콘" class="on">
            </span>
                <span class="text tm">회원 관리</span>
            </a>
        </li>
    {/if}
    <li>
        <a href="/using/account_manage" class="flex aic" on:click="{(event) => setActive(1, event)}"
           class:active="{activeIndex === 1}">
            <span class="ico-box img-box">
                <img src="/img/menu_ico_7.svg" alt="메뉴 아이콘">
                <img src="/img/menu_ico_7_active.svg" alt="메뉴 액티브 아이콘" class="on">
            </span>
            <span class="text tm">거래처 관리</span>
        </a>
    </li>
    <li>
        <a href="/using/inventory_manage" class="flex aic" on:click="{(event) => setActive(2, event)}"
           class:active="{activeIndex === 2}">
            <span class="ico-box img-box">
                <img src="/img/menu_ico_3.svg" alt="메뉴 아이콘">
                <img src="/img/menu_ico_3_active.svg" alt="메뉴 액티브 아이콘" class="on">
            </span>
            <span class="text tm">재고 관리</span>
        </a>
    </li>
    <li>
        <a href="/using/sell_manage" class="flex aic" on:click="{(event) => setActive(3, event)}"
           class:active="{activeIndex === 3}">
            <span class="ico-box img-box">
                <img src="/img/menu_ico_5.svg" alt="메뉴 아이콘">
                <img src="/img/menu_ico_5_active.svg" alt="메뉴 액티브 아이콘" class="on">
            </span>
            <span class="text tm">판매 관리</span>
        </a>
    </li>
    <li>
        <a href="/using/sale_manage" class="flex aic" on:click="{(event) => setActive(5, event)}"
           class:active="{activeIndex === 5}">
            <span class="ico-box img-box">
                <img src="/img/menu_ico_11.svg" alt="메뉴 아이콘">
                <img src="/img/menu_ico_11_active.svg" alt="메뉴 액티브 아이콘" class="on">
            </span>
            <span class="text tm">매출 관리</span>
        </a>
    </li>
    <li>
        <a href="/using/qna" class="flex aic" on:click="{(event) => setActive(6, event)}"
           class:active="{activeIndex === 6}">
            <span class="ico-box img-box">
                <img src="/img/menu_ico_6.svg" alt="메뉴 아이콘">
                <img src="/img/menu_ico_6_active.svg" alt="메뉴 액티브 아이콘" class="on">
            </span>
            <span class="text tm">자주 묻는 질문</span>
        </a>
    </li>
</ul>
<a href="/using/calender" class="h50 flex aic jcc abs cm tm bdm bdr999 x-middle f16"
           style="width: calc(100% - 48px); bottom: 28px;" on:click="{(event) => setActive(7, event)}"
           class:active="{activeIndex === 7}">캘린더</a>
