<script>
    import Header from '../../../components/Header.svelte';
    import LeftMenuAdmin from '../../../components/LeftMenuAdmin.svelte';

    import { useFetch } from '../../../hooks.js';
	if (typeof window !== 'undefined') {
		useFetch();
	}
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