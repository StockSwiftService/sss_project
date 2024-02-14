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
                                <div class="search-box">
                                    <input type="search" placeholder="검색어 입력">
                                </div>
                                <button class="search-btn flex aic jcc">
                                    <span class="ico-box img-box w16">
                                        <img src="../img/ico_search.svg" alt="검색 아이콘">
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
                    <div class="table-box-1 table-type-1 scr-type-1 mt12">
                        <table>
                            <thead>
                                <tr>
                                    <th class="wsn">NO</th>
                                    <th class="wsn">등록일</th>
                                    <th class="wsn">회원사</th>
                                    <th class="wsn">사업자상호</th>
                                    <th class="wsn">사업자번호</th>
                                    <th class="wsn">대표자명</th>
                                    <th class="wsn">대표 생년월일</th>
                                    <th class="wsn">사업장연락처</th>
                                    <th class="wsn">휴대폰번호</th>
                                    <th class="wsn">은행명</th>
                                    <th class="wsn">계좌번호</th>
                                    <th class="wsn">사업장주소</th>
                                    <th class="wsn">매장비서 사용</th>
                                    <th class="wsn">사용 요금제</th>
                                    <th class="wsn">매월 정산일</th>
                                    <th class="wsn">결제수단</th>
                                    <th class="wsn">데모여부</th>
                                    <th class="wsn">자/타사</th>
                                    <th class="wsn">수수료</th>
                                    <th class="wsn">세무전송</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td class="wsn">999</td>
                                    <td class="wsn">2022-12-12</td>
                                    <td class="wsn">벤대리점</td>
                                    <td class="wsn">회사명</td>
                                    <td class="wsn">123-90-123455</td>
                                    <td class="wsn">홍길동</td>
                                    <td class="wsn">770315</td>
                                    <td class="wsn">02-1234-0000</td>
                                    <td class="wsn">010-1234-0000</td>
                                    <td class="wsn">국민</td>
                                    <td class="wsn">123-000-0000000</td>
                                    <td class="wsn">경기도 남양주시 순화궁로 282 에이스하이앤드</td>
                                    <td class="wsn">유료</td>
                                    <td class="wsn">베이직</td>
                                    <td class="wsn">1일</td>
                                    <td class="wsn">카드</td>
                                    <td class="wsn">운영</td>
                                    <td class="wsn">타사</td>
                                    <td class="wsn">33.0%</td>
                                    <td class="wsn">미전송</td>
                                </tr>
                                <tr>
                                    <td class="wsn">999</td>
                                    <td class="wsn">2022-12-12</td>
                                    <td class="wsn">프랜차이즈</td>
                                    <td class="wsn">회사명</td>
                                    <td class="wsn">123-90-123455</td>
                                    <td class="wsn">홍길동</td>
                                    <td class="wsn">770315</td>
                                    <td class="wsn">02-1234-0000</td>
                                    <td class="wsn">010-1234-0000</td>
                                    <td class="wsn">신한</td>
                                    <td class="wsn">123-000-0000000</td>
                                    <td class="wsn">경기도 남양주시 순화궁로 282 에이스하이앤드</td>
                                    <td class="wsn">유료</td>
                                    <td class="wsn">베이직</td>
                                    <td class="wsn">3일</td>
                                    <td class="wsn">계좌이체</td>
                                    <td class="wsn">운영</td>
                                    <td class="wsn">타사</td>
                                    <td class="wsn">50.0%</td>
                                    <td class="wsn">미전송</td>
                                </tr>
                            </tbody>
                        </table>
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
    </div>
</div>