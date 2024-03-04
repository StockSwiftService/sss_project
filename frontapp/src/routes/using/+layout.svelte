<script>
	import Header from '../../components/Header.svelte';
	import LeftMenu from '../../components/LeftMenu.svelte';
	import { useFetch } from '../../hooks.js';
	if (typeof window !== 'undefined') {
		useFetch();
	}
</script>

<div class="wrap flex">
	<div class="left-area scr-type-1 rel">
		<LeftMenu />
	</div>
	<div class="right-area">
		<Header />
		<slot />
	</div>
</div>
