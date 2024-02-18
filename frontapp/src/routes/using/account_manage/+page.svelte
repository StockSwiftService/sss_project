<script>
    let isActive = false;
    let isActiveAdd = false;
    let isActiveModify = false;

    const resetForm = () => {
        const form = document.querySelector('form');
        if (form) {
            form.reset(); // 기본 폼 초기화
            const allErrorElements = document.querySelectorAll('.error-text-box .error-text');
            allErrorElements.forEach((errorElement) => {
                errorElement.textContent = '';
            });
        }
    };
    function activateModalAdd() {
        isActive = true;
        isActiveAdd = true;
        resetForm();
    }

    function activateModalModify() {
        isActive = true;
        isActiveModify = true;
    }

    function deactivateModal() {
        isActive = false;
        isActiveAdd = false;
        isActiveModify = false;
    }

    const submitClientForm = async (event) => {
        event.preventDefault();
        console.log('Submit button clicked');

        const form = event.target;
        const formData = new FormData(form);

        // 클라이언트 측 유효성 검사 수행
        const clientName = formData.get('clientName');
        const repName = formData.get('repName');
        const phoneNumber = formData.get('phoneNumber');
        const address = formData.get('address');

        const errors = {};

        if (!clientName.trim()) {
            errors.clientName = '거래처명을 입력하세요.';
        }

        if (!repName.trim()) {
            errors.repName = '대표자명을 입력하세요.';
        }

        if (!phoneNumber.trim()) {
            errors.phoneNumber = '연락처를 입력하세요.';
        }

        if (!address.trim()) {
            errors.address = '주소를 입력하세요.';
        }

        // 클라이언트 측에서 유효성 검사 실패 시 제출 중단
        if (Object.keys(errors).length > 0) {
            updateErrorMessages(errors);
            return;
        }

        // 서버로 데이터 전송
        try {
            const response = await fetch('http://localhost:8080/api/v1/clients', {
                method: 'POST',
                credentials: 'include',
                body: formData
            });

            if (response.ok) {
                const responseData = await response.json();
                console.log(responseData);
                window.alert('저장되었습니다.');
                // 적절한 페이지로 이동하도록 수정
            } else {
                const responseData = await response.json();
                console.error(responseData);
                window.alert('저장에 실패했습니다.');
            }
        } catch (error) {
            console.error('Error submitting form:', error);
        }
    }

    const updateErrorMessages = (errors) => {
        // 각 오류 메시지 업데이트
        for (const fieldName in errors) {
            const errorElement = document.querySelector(
                `.error-text-box[data-field="${fieldName}"] .error-text`
            );
            if (errorElement) {
                errorElement.textContent = errors[fieldName];
            }
        }
        const inputFields = document.querySelectorAll('.input-type-1 input');
        inputFields.forEach((inputField) => {
            inputField.addEventListener('input', () => {
                const fieldName = inputField.getAttribute('name');
                const errorElement = document.querySelector(
                    `.error-text-box[data-field="${fieldName}"] .error-text`
                );
                if (errorElement) {
                    errorElement.textContent = '';
                }
            });
        });
    };

    let isAddress = false;
    let element_layer;

    //주소 찾기 스크립트 추가
    function loadScript() {
        return new Promise((resolve, reject) => {
            const script = document.createElement('script');
            script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
            script.async = true;  //비동기로 로드됨
            script.onload = resolve;  //호출 성공
            script.onerror = reject;  //호출 실패
            document.body.appendChild(script);  //생성된 script를 body에 추가
        });
    }

    //클릭 시 창 열림
    async function initDaumPostcode() {
        element_layer = document.getElementById('layer');
        await loadScript();
        new daum.Postcode({
            oncomplete: function (data) {
                var addr = ''; // 주소 변수

                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                document.getElementById('postcode').value = data.zonecode;
                document.getElementById("address").value = addr;
                formData.address = addr;
                document.getElementById("detailAddress").focus();
                isAddress = true;
                element_layer.style.display = 'none';
            },
            width: '100%',
            height: '100%',
            maxSuggestItems: 5
        }).embed(element_layer);

        element_layer.style.display = 'block';
        initLayerPosition();

        // 외부 클릭 이벤트를 감지
        window.addEventListener('click', closeOnOutsideClick);
    }

    //창 닫기
    function closeOnOutsideClick(event) {
        // 클릭 이벤트가 주소 검색 창 외부에서 발생했을 경우, 주소 검색 창 닫기
        if (element_layer && !element_layer.contains(event.target)) {
            element_layer.style.display = 'none';
            // 외부 클릭 이벤트 리스너를 제거
            window.removeEventListener('click', closeOnOutsideClick);
        }
    }

    function initLayerPosition() {
        var width = 300; //우편번호서비스가 들어갈 element의 width
        var height = 400; //우편번호서비스가 들어갈 element의 height
        var borderWidth = 5; //샘플에서 사용하는 border의 두께

        element_layer.style.width = width + 'px';
        element_layer.style.height = height + 'px';
        element_layer.style.border = borderWidth + 'px solid';

        element_layer.style.left = (((window.innerWidth || document.documentElement.clientWidth) - width) / 2 - borderWidth) + 'px';
        element_layer.style.top = (((window.innerHeight || document.documentElement.clientHeight) - height) / 2 - borderWidth) + 'px';
    }
</script>

<div class="modal-area-1 modal-area wh100per fixed zi9" class:active="{isActive}">

    <!-- 거래처 등록 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w480" class:active="{isActiveAdd}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">거래처 등록</h3>
            <button type="button" class="x-btn img-box abs" on:click="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <form on:submit|preventDefault={submitClientForm}>
            <div class="flex fdc g36">
                <div>
                    <h2 class="c333 f15 tm mb8">거래처명<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" name="clientName" placeholder="거래처명">
                        </div>
                        <button type="button" class="btn-type-1 w80 f14 bdr4 b333 cfff">확인</button>
                    </div>

                    <div class="error-text-box" data-field="clientName">
                        <span class="error-text f13 mt8 cr"></span>
                        <span class="f13 mt8 cr">중복된 거래처명입니다.</span>
                        <span class="f13 mt8 cg">사용 가능한 거래처명입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">대표자명<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" name="repName" placeholder="대표자명">
                    </div>
                    <div class="error-text-box" data-field="repName">
                        <span class="error-text f13 mt8 cr"></span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">연락처<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" name="phoneNumber" placeholder="연락처 (-자 빼고 입력해 주세요.)">
                    </div>
                    <div class="error-text-box" data-field="phoneNumber">
                        <span class="error-text f13 mt8 cr"></span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">주소<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" name="address" placeholder="주소">
                        </div>
                        <button type="button" class="btn-type-1 w80 f14 bdr4 b333 cfff" on:click|preventDefault={initDaumPostcode}>찾기</button>
                    </div>
                    <div class="input-type-1 f14 w100per mt8">
                        <input type="text" placeholder="상세주소">
                    </div>
                    <div class="error-text-box" data-field="address">
                        <span class="error-text f13 mt8 cr"></span>
                    </div>
                </div>
            </div>
            <div class="btn-area flex aic jcc g8 mt40">
                <button type="submit"  class="w120 h40 btn-type-2 bdr4 bm cfff tm f14" >등록</button>
                <button type="button" class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" on:click="{deactivateModal}">취소</button>
            </div>
            </form>
        </div>

    </div>

    <!-- 거래처 수정 모달 -->
    <div class="modal-type-1 modal-box abs xy-middle bfff zi9 w480" class:active="{isActiveModify}">
        <div class="top-box rel">
            <h3 class="tb c121619 f18">거래처 수정</h3>
            <button class="x-btn img-box abs" on:click="{deactivateModal}">
                <img src="/img/ico_x_121619.svg" alt="닫기 아이콘">
            </button>
        </div>
        <div class="middle-box scr-type-1">
            <div class="flex fdc g36">
                <div>
                    <h2 class="c333 f15 tm mb8">거래처명<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" placeholder="거래처명">
                        </div>
                        <button class="btn-type-1 w80 f14 bdr4 b333 cfff">확인</button>
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        <span class="f13 mt8 cr">중복된 거래처명입니다.</span>
                        <span class="f13 mt8 cg">사용 가능한 거래처명입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">대표자명<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="대표자명">
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">연락처<span class="cr f16 tm inblock">*</span></h2>
                    <div class="input-type-1 f14 w100per">
                        <input type="text" placeholder="연락처 (-자 빼고 입력해 주세요.)">
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    </div>
                </div>
                <div>
                    <h2 class="c333 f15 tm mb8">주소<span class="cr f16 tm inblock">*</span></h2>
                    <div class="flex g8">
                        <div class="input-type-1 f14 w100per">
                            <input type="text" placeholder="주소">
                        </div>
                        <button class="btn-type-1 w80 f14 bdr4 b333 cfff">찾기</button>
                    </div>
                    <div class="input-type-1 f14 w100per mt8">
                        <input type="text" placeholder="상세주소">
                    </div>
                    <div class="error-text-box">
                        <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                    </div>
                </div>
            </div>
            <div class="btn-area flex aic jcc g8 mt40">
                <button class="w120 h40 btn-type-2 bdr4 bm cfff tm f14">수정</button>
                <button class="w120 h40 btn-type-2 bdr4 bdm cm tm f14" on:click="{deactivateModal}">취소</button>
            </div>
        </div>
    </div>

</div>

<div class="store-management-area cnt-area w100per">
    <div class="title-box flex aic jcsb">
        <h1 class="tb c121619">거래처 관리</h1>
    </div>
    <div class="cnt-box-1 cnt-box">
        <div class="top-area">
            <div class="space-area-2 flex aic jce">
                <div class="right-box flex aic">
                    <div class="search-type-1 flex aic">
                        <div class="search-box w200">
                            <input type="search" placeholder="검색어 입력">
                        </div>
                        <button class="search-btn flex aic jcc">
                            <span class="ico-box img-box w16">
                                <img src="/img/ico_search.svg" alt="검색 아이콘">
                            </span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="line"></div>
        <div class="middle-area">
            <div class="all-text c121619 f14">
                전체 <span class="number inblock cm tm">0</span>개
            </div>
            <div class="table-box-1 table-type-1 scr-type-2 mt12">
                <table>
                    <thead>
                        <tr>
                            <th class="wsn" style="width: 44px;">
                                <div class="check-type-1">
                                    <input type="checkbox" id="all">
                                    <label for="all"></label>
                                </div> 
                            </th>
                            <th class="wsn">거래처명</th>
                            <th class="wsn">대표자명</th>
                            <th class="wsn">연락처</th>
                            <th class="wsn">주소</th>
                            <th class="wsn">수정</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="wsn" style="width: 44px;">   
                                <div class="check-type-1">
                                    <input type="checkbox" id="v1">
                                    <label for="v1"></label>
                                </div> 
                            </td>
                            <td class="wsn">(주)네모컴퍼니</td>
                            <td class="wsn">김네모</td>
                            <td class="wsn">01033333333</td>
                            <td class="wsn">대전광역시 대덕구 덕암동 572-3번지</td>
                            <td class="wsn tac">
                                <button class="w40 h24 btn-type-2 bdr4 bdbbb cbbb f13" on:click="{activateModalModify}">수정</button>
                            </td>
                        </tr>
                        <tr>
                            <td class="wsn" style="width: 44px;">   
                                <div class="check-type-1">
                                    <input type="checkbox" id="v2">
                                    <label for="v2"></label>
                                </div> 
                            </td>
                            <td class="wsn">(주)네모컴퍼니</td>
                            <td class="wsn">김네모</td>
                            <td class="wsn">01033333333</td>
                            <td class="wsn">대전광역시 대덕구 덕암동 572-3번지</td>
                            <td class="wsn tac">
                                <button class="w40 h24 btn-type-2 bdr4 bdbbb cbbb f13" on:click="{activateModalModify}">수정</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="flex aic jcsb mt8">
                <div class="flex aic g4">
                    <button class="w50 h30  btn-type-1 bdA2A9B0 bdr4 f12 cA2A9B0">삭제</button>
                </div>
                <div class="flex aic g4">
                    <button class="w50 h30 btn-type-1 bm bdr4 f12 cfff" on:click="{activateModalAdd}">등록</button>
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