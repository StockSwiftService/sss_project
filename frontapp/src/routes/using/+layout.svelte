<script>
    import Header from '../../components/Header.svelte';
    import LeftMenu from '../../components/LeftMenu.svelte';
</script>
<div class="wrap flex">
    <div class="left-area scr-type-1">
        <LeftMenu />
    </div>
    <div class="right-area">
        <Header />  
        <slot />
    </div>
</div>