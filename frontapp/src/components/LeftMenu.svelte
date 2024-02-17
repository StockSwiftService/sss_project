<script>
    let activeIndex = -1;

    function setActive(index) {
        activeIndex = index;
    }
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
    <li>
        <a href="/using/user_manage" class="flex aic" on:click="{(event) => setActive(0, event)}" class:active="{activeIndex === 0}">
            <span class="ico-box img-box">
                <img src="/img/menu_ico_2.svg" alt="메뉴 아이콘">
                <img src="/img/menu_ico_2_active.svg" alt="메뉴 액티브 아이콘" class="on">
            </span>    
            <span class="text tm">회원 관리</span>
        </a>
    </li>
    <li>
        <a href="/using/account_manage" class="flex aic" on:click="{(event) => setActive(1, event)}" class:active="{activeIndex === 1}">
            <span class="ico-box img-box">
                <img src="/img/menu_ico_7.svg" alt="메뉴 아이콘">
                <img src="/img/menu_ico_7_active.svg" alt="메뉴 액티브 아이콘" class="on">
            </span>    
            <span class="text tm">거래처 관리</span>
        </a>
    </li>
    <li>
        <a href="/using/inventory_manage" class="flex aic" on:click="{(event) => setActive(2, event)}" class:active="{activeIndex === 2}">
            <span class="ico-box img-box">
                <img src="/img/menu_ico_3.svg" alt="메뉴 아이콘">
                <img src="/img/menu_ico_3_active.svg" alt="메뉴 액티브 아이콘" class="on">
            </span>    
            <span class="text tm">재고 관리</span>
        </a>
    </li>
    <li>
        <a href="/using/sell_manage" class="flex aic" on:click="{(event) => setActive(3, event)}" class:active="{activeIndex === 3}">
            <span class="ico-box img-box">
                <img src="/img/menu_ico_5.svg" alt="메뉴 아이콘">
                <img src="/img/menu_ico_5_active.svg" alt="메뉴 액티브 아이콘" class="on">
            </span>    
            <span class="text tm">판매 관리</span>
        </a>
    </li>
    <li>
        <a href="/using/purchase_manage" class="flex aic" on:click="{(event) => setActive(4, event)}" class:active="{activeIndex === 4}">
            <span class="ico-box img-box">
                <img src="/img/menu_ico_13.svg" alt="메뉴 아이콘">
                <img src="/img/menu_ico_13_active.svg" alt="메뉴 액티브 아이콘" class="on">
            </span>    
            <span class="text tm">구매 관리</span>
        </a>
    </li>
    <li>
        <a href="/using/sale_manage" class="flex aic" on:click="{(event) => setActive(5, event)}" class:active="{activeIndex === 5}">
            <span class="ico-box img-box">
                <img src="/img/menu_ico_11.svg" alt="메뉴 아이콘">
                <img src="/img/menu_ico_11_active.svg" alt="메뉴 액티브 아이콘" class="on">
            </span>    
            <span class="text tm">매출 관리</span>
        </a>
    </li>
    <li>
        <a href="/using/qna" class="flex aic" on:click="{(event) => setActive(6, event)}" class:active="{activeIndex === 6}">
            <span class="ico-box img-box">
                <img src="/img/menu_ico_6.svg" alt="메뉴 아이콘">
                <img src="/img/menu_ico_6_active.svg" alt="메뉴 액티브 아이콘" class="on">
            </span>    
            <span class="text tm">자주 묻는 질문</span>
        </a>
    </li>
</ul>
<a href="/using/calender" class="h50 flex aic jcc abs cm tm bdm bdr999 x-middle f16" style="width: calc(100% - 48px); bottom: 28px;" on:click="{(event) => setActive(7, event)}" class:active="{activeIndex === 7}">캘린더</a>