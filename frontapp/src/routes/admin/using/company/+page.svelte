<script>
	import { onMount } from 'svelte';

	let companyList = [];
	let resList = [];
	let companyTotal = [];
	let keyword = '';
	let isApprove = 'ALL';

	const changePage = async (page) => {
		try {
			const response = await fetch(
				`http://localhost:8080/api/v1/company/lists?page=${page}&keyword=${keyword}&isApprove=${isApprove}`,
				{
					method: 'GET',
					headers: {
						'Content-Type': 'application/json'
					},
					credentials: 'include'
				}
			);

			if (response.ok) {
				const data = await response.json();

				if (data.resultCode === 'E-1') {
					window.location.href = '/using/user_manage';
					alert('관리자만 접근할 수 있습니다.');
				}
				resList = data.data.pageingList;
				companyList = resList.content;
				companyTotal = data.data.companyList;
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
	};

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
				resList = data.data.pageingList;
				companyList = resList.content;
				companyTotal = data.data.companyList;
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
	});

	let formData = {
		approveList: []
	};

	function handleCheckboxChange(event) {
		const checkboxId = event.target.id;
		if (event.target.checked) {
			formData.approveList = [...formData.approveList, checkboxId];
		} else {
			formData.approveList = formData.approveList.filter((id) => id !== checkboxId);
		}
	}

	function allCheck(event) {
		if (event.target.checked) {
			formData.approveList = [];
			for (const company of companyList) {
				const checkbox = document.getElementById(company.id);
				checkbox.checked = true;
				formData.approveList.push(company.id.toString());
			}
		} else {
			for (const company of companyList) {
				const checkbox = document.getElementById(company.id);
				checkbox.checked = false;
			}
			formData.approveList = [];
		}
	}
	const approve = async () => {
		const confirmResult = window.confirm('정말로 활성화 하시겠습니까?');
		if (confirmResult) {
			const response = await fetch('http://localhost:8080/api/v1/company/approve', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(formData)
			});
			if (response.ok) {
				const data = await response.json();
				changePage(0);
				alert('처리가 완료되었습니다.');
			}
		}
	};

	const disapprove = async () => {
		const confirmResult = window.confirm('정말로 비활성화 하시겠습니까?');
		if (confirmResult) {
			const response = await fetch('http://localhost:8080/api/v1/company/disapprove', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(formData)
			});
			if (response.ok) {
				const data = await response.json();
				changePage(0);
				alert('처리가 완료되었습니다.');
			}
		}
	};

	const reject = async () => {
		const confirmResult = window.confirm('정말로 반려하시겠습니까?');
		if (confirmResult) {
			const response = await fetch('http://localhost:8080/api/v1/company/reject', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(formData)
			});
			if (response.ok) {
				const data = await response.json();
				changePage(0);
				alert('처리가 완료되었습니다.');
			}
		}
	};

	let companyMemo = '';

	function generatePageButtons(totalPages) {
		const buttons = [];
		for (let i = 0; i < totalPages; i++) {
			buttons.push(i + 1);
		}
		return buttons;
	}

	function memoModify(companyId, memo) {
		document.getElementById(`memo_after_${companyId}`).style.display = 'flex';
		document.getElementById(`memo_before_${companyId}`).style.display = 'none';
		companyMemo = memo;
	}

	async function memoCheck(companyId) {
		document.getElementById(`memo_before_${companyId}`).style.display = 'flex';
		document.getElementById(`memo_after_${companyId}`).style.display = 'none';

		let formData = {
			companyId: companyId,
			memo: companyMemo
		};

		const response = await fetch(`http://localhost:8080/api/v1/company/memo`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			credentials: 'include',
			body: JSON.stringify(formData)
		});
		if (response.ok) {
			try {
				const response = await fetch(
					`http://localhost:8080/api/v1/company/lists?page=${resList.number}&keyword=${keyword}&isApprove=${isApprove}`,
					{
						method: 'GET',
						headers: {
							'Content-Type': 'application/json'
						},
						credentials: 'include'
					}
				);
				if (response.ok) {
					const data = await response.json();
					if (data.resultCode === 'E-1') {
						window.location.href = '/using/user_manage';
						alert('관리자만 접근할 수 있습니다.');
					}
					resList = data.data.pageingList;
					companyList = resList.content;
					companyTotal = data.data.companyList;
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
							<select name="select" bind:value={isApprove}>
								<option value="ALL">전체</option>
								<option value="NOT_APPROVED">미승인</option>
								<option value="APPROVED">승인</option>
							</select>
						</div>
					</div>
				</div>
				<div class="right-box flex aic">
					<form>
						<div class="search-type-1 flex aic">
							<div class="search-box w200">
								<input type="search" placeholder="검색어 입력" bind:value={keyword} />
							</div>
							<button class="search-btn flex aic jcc" on:click={() => changePage(0)}>
								<span class="ico-box img-box w16">
									<img src="/img/ico_search.svg" alt="검색 아이콘" />
								</span>
							</button>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="line"></div>
		<div class="middle-area">
			<div class="all-text c121619 f14">
				전체 <span class="number inblock cm tm">{companyTotal.length}</span>건
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
							<th class="wsn">메모</th>
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
								<td>{company.address} {company.detailAddress}</td>
								<td class="wsn">{company.businessNumber}</td>
								<td class="wsn">{company.email}</td>
								<td class="wsn">{company.repName}</td>
								<td class="wsn">{company.companyCode}</td>
								<td class="tal wsn">
									<div id="memo_before_{company.id}" class="memo-before flex aic jcsb g12 active">
										<p class="">{company.memo}</p>
										<button
											id="memo_before_{company.id}"
											class="w40 h24 btn-type-2 bdr4 bdbbb cbbb f13 flex"
											on:click={() => memoModify(company.id, company.memo)}>수정</button
										>
									</div>
									<div id="memo_after_{company.id}" class="memo-after flex fdc aie g12">
										<div id="memo_after_{company.id}" class="input-type-1 f14 w100per h160">
											<input type="text" placeholder="내용" bind:value={companyMemo} />
										</div>
										<button
											id="memo_after_{company.id}"
											class="w40 h24 btn-type-2 bdr4 bdbbb cbbb f13 mt4"
											on:click={() => memoCheck(company.id)}>저장</button
										>
									</div>
								</td>
							</tr>
						{/each}
					</tbody>
				</table>
			</div>
			<div class="flex aic jcsb mt8">
				<div class="flex aic g4">
					<button class="w50 h30 btn-type-1 bdm bdr4 f12 cm" on:click={approve}>활성화</button>
					<button class="w50 h30 btn-type-1 bdA2A9B0 bdr4 f12 cA2A9B0" on:click={disapprove}
						>비활성화</button
					>
					<button class="w50 h30 btn-type-1 bdA2A9B0 bdr4 f12 cA2A9B0" on:click={reject}
						>반려</button
					>
				</div>
			</div>
			<div class="paging-box flex jcc mt40">
				<ul class="flex aic jcc">
					{#if resList.number > 0}
						<li class="page-btn" on:click={() => changePage(resList.number - 1)}>
							<a href="">이전</a>
						</li>
					{/if}

					{#each generatePageButtons(resList.totalPages) as button}
						<li
							class="num"
							on:click={() => resList.number !== button - 1 && changePage(button - 1)}
						>
							<a href="" class:active={resList.number === button - 1}>{button}</a>
						</li>
					{/each}

					{#if resList.number < resList.totalPages - 1}
						<li class="page-btn" on:click={() => changePage(resList.number + 1)}>
							<a href="">다음</a>
						</li>
					{/if}
				</ul>
			</div>
		</div>
	</div>
</div>

<style>
	.memo-before,
	.memo-after {
		display: none;
	}
	.memo-before.active,
	.memo-after.active {
		display: flex;
	}
</style>
