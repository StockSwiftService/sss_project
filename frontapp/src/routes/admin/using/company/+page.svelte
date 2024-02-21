<script>
	import { onMount } from 'svelte';

	let companyList = [];
	onMount(async () => {
		try {
			const response = await fetch('http://localhost:8080/api/v1/company/lists', {
				method: 'GET',
				headers: {
					'Content-Type': 'application/json'
				},
				credentials: 'include'
			});

			if (response.ok) {
				const data = await response.json();

				if (data.resultCode === 'E-1') {
					window.location.href = '/using/user_manage';
					alert('관리자만 접근할 수 있습니다.');
				}
				companyList = data.data.companyList;
				console.log(companyList);
			} else {
				console.error('서버 응답 오류:', response.statusText);
				if (!response.ok && response.status != 401) {
					alert('다시 시도 해주세요.');
				}
			}
		} catch (error) {
			console.error('오류 발생:', error);
			alert('다시 시도 해주세요2.');
		}
	});


    let formData = {
        approveList:[]
    }
	// let approveList = [];

	function handleCheckboxChange(event) {
		const checkboxId = event.target.id;
		if (event.target.checked) {
			formData.approveList = [...formData.approveList, checkboxId];
		} else {
			formData.approveList = formData.approveList.filter((id) => id !== checkboxId);
		}
		console.log(formData.approveList);
	}

	function allCheck(event) {
		if (event.target.checked) {
            formData.approveList = [];
			for (const company of companyList) {
				const checkbox = document.getElementById(company.id);
				checkbox.checked = true;
				formData.approveList.push(company.id.toString());
			}
			console.log(formData.approveList);
		} else {
            for (const company of companyList) {
				const checkbox = document.getElementById(company.id);
				checkbox.checked = false;
			}
			formData.approveList = [];
			console.log(formData.approveList);
		}
	}
</script>

<div class="store-management-area cnt-area w100per">
	<div class="title-box flex aic jcsb">
		<h1 class="tb c121619">회사 관리</h1>
	</div>
	<div class="cnt-box-1 cnt-box">
		<div class="top-area">
			<div class="space-area-2 flex aic jcsb">
				<div class="left-box flex aic">
					<div class="select-box-1 flex aic">
						<span class="input-text c697077">승인 상태</span>
						<div class="select-type-1 h36">
							<select name="select">
								<option value="">전체</option>
								<option value="">미승인</option>
								<option value="">승인</option>
							</select>
						</div>
					</div>
				</div>
				<div class="right-box flex aic">
					<div class="search-type-1 flex aic">
						<div class="search-box w200">
							<input type="search" placeholder="검색어 입력" />
						</div>
						<button class="search-btn flex aic jcc">
							<span class="ico-box img-box w16">
								<img src="/img/ico_search.svg" alt="검색 아이콘" />
							</span>
						</button>
					</div>
				</div>
			</div>
		</div>
		<div class="line"></div>
		<div class="middle-area">
			<div class="all-text c121619 f14">
				전체 <span class="number inblock cm tm">0</span>건
			</div>
			<div class="table-box-1 table-type-1 scr-type-2 mt12">
				<table>
					<thead>
						<tr>
							<th class="wsn" style="width: 44px;">
								<div class="check-type-1">
									<input type="checkbox" id="all" on:change={allCheck} />
									<label for="all"></label>
								</div>
							</th>
							<th class="wsn">승인여부</th>
							<th class="wsn">회사명</th>
							<th class="wsn">주소</th>
							<th class="wsn">사업자 번호</th>
							<th class="wsn">이메일</th>
							<th class="wsn">대표자명</th>
							<th class="wsn">회사코드</th>
						</tr>
					</thead>
					<tbody>
						{#each companyList as company}
							<tr>
								<td class="wsn" style="width: 44px;">
									<div class="check-type-1">
										<input type="checkbox" id={company.id} on:change={handleCheckboxChange} />
										<label for={company.id}></label>
									</div>
								</td>
								<td class="wsn">
									{#if company.approved}
										<span class="cg">승인</span>
									{:else}
										<span class="cr">미승인</span>
									{/if}
								</td>
								<td class="wsn">{company.name}</td>
								<td class="wsn">{company.address} {company.detailAddress}</td>
								<td class="wsn">{company.businessNumber}</td>
								<td class="wsn">{company.email}</td>
								<td class="wsn">{company.repName}</td>
								<td class="wsn">{company.companyCode}</td>
							</tr>
						{/each}
					</tbody>
				</table>
			</div>
			<div class="flex aic jcsb mt8">
				<div class="flex aic g4">
					<button class="w50 h30 btn-type-1 bdm bdr4 f12 cm" on:click={handleCheckboxChange}
						>승인</button
					>
					<button class="w50 h30 btn-type-1 bdA2A9B0 bdr4 f12 cA2A9B0" on:click={allCheck}
						>탈퇴</button
					>
				</div>
			</div>
			<div class="paging-box flex jcc mt40">
				<ul class="flex aic jcc">
					<li class="page-btn">
						<a href="">이전</a>
					</li>
					<li class="num">
						<a href="" class="active">1</a>
					</li>
					<li class="num">
						<a href="">2</a>
					</li>
					<li class="num">
						<a href="">3</a>
					</li>
					<li class="num">
						<a href="">4</a>
					</li>
					<li class="num">
						<a href="">5</a>
					</li>
					<li class="page-btn">
						<a href="">다음</a>
					</li>
				</ul>
			</div>
		</div>
	</div>
</div>
