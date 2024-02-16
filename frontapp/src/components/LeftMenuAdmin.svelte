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
    <img src="../img/logo.svg" alt="">
</div>
<ul class="menu-box">
    <li>
        <a href="/admin/company" class="flex aic" on:click="{(event) => setActive(0, event)}" class:active="{activeIndex === 0}">
            <span class="ico-box img-box">
                <img src="../img/menu_ico_7.svg" alt="메뉴 아이콘">
                <img src="../img/menu_ico_7_active.svg" alt="메뉴 액티브 아이콘" class="on">
            </span>    
            <span class="text tm">회사 관리</span>
        </a>
    </li>
    <li>
        <a href="/admin/qna" class="flex aic" on:click="{(event) => setActive(1, event)}" class:active="{activeIndex === 1}">
            <span class="ico-box img-box">
                <img src="../img/menu_ico_6.svg" alt="메뉴 아이콘">
                <img src="../img/menu_ico_6_active.svg" alt="메뉴 액티브 아이콘" class="on">
            </span>    
            <span class="text tm">질문 관리</span>
        </a>
    </li>
</ul>