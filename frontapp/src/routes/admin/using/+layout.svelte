<script>
    import Header from '../../../components/Header.svelte';
    import LeftMenuAdmin from '../../../components/LeftMenuAdmin.svelte';
</script>
<div class="wrap flex">
    <div class="left-area scr-type-1">
        <LeftMenuAdmin />
    </div>
    <div class="right-area">
        <Header />  
        <slot />
    </div>
</div>