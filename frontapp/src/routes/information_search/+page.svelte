<style>
    .tab-menu-box > li {
        width: 33.3333%;
        text-align: center;
    }
    .tab-menu-box > li > a {
        font-size: 16px;
        color: #bbb;
        box-sizing: border-box;
        padding: 16px 0;
    }
    .tab-menu-box > li > a.active {
        font-family: "SUIT-Bold";
        color: #2656F6;
        border-bottom: 4px solid #2656F6;
    }
</style>

<div class="w100per bsb pt120 pb120 mgc" style="max-width: 480px;">
    <h1 class="c121619 tb tac f32">정보 찾기</h1>
    <p class="tac f16 c555 mt28 tac">회사의 대표자가 아닌 직원의 경우 대표자에게 문의하세요.</p>
    <ul class="tab-menu-box flex mt60">
        <li>
            <a href="" class="active">회사코드 찾기</a>
        </li>
        <li>
            <a href="">아이디 찾기</a>
        </li>
        <li>
            <a href="">비밀번호 찾기</a>
        </li>
    </ul>
    <ul class="tab-cnt-box mt60">

        <!-- 회사코드 찾기 -->
        <li>

            <!-- 찾기 전 -->
            <div>
                <div class="flex fdc g8">
                    <div>
                        <div class="input-type-1 w100per">
                            <input type="text" placeholder="회사명">
                        </div>
                        <div class="error-text-box">
                            <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        </div>
                    </div>  
                    <div>
                        <div class="input-type-1 w100per">
                            <input type="text" placeholder="사업자 번호 (-자 빼고 입력해 주세요.)">
                        </div>
                        <div class="error-text-box">
                            <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        </div>
                    </div>  
                    <div>
                        <div class="input-type-1 w100per">
                            <input type="text" placeholder="이메일">
                        </div>
                        <div class="error-text-box">
                            <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        </div>
                    </div>  
                </div>

                <!-- 입력값 3개 중 1개라도 일치하는게 없을 경우
                "입력하신 정보가 일치하지 않습니다."
                alert 띄우기 -->
                <a href="#" class="btn-type-1 bm w100per h50 bdr4 mt60" id="test">
                    <span class="text f16 tb cfff">회사코드 찾기</span>
                </a>
            </div>

            <!-- 결과 -->
            <div>
                <p class="f16 tm c333">찾으시는 회사코드는 "" 입니다.</p>
                <a href="/" class="btn-type-1 bm w100per h50 bdr4 mt60" id="test">
                    <span class="text f16 tb cfff">로그인</span>
                </a>
            </div>

        </li>

        <!-- 아이디 찾기 -->
        <li>

            <!-- 찾기 전 -->
            <div>
                <div class="flex fdc g8">
                    <div>
                        <div class="input-type-1 w100per">
                            <input type="text" placeholder="회사코드">
                        </div>
                        <div class="error-text-box">
                            <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        </div>
                    </div>  
                    <div>
                        <div class="input-type-1 w100per">
                            <input type="text" placeholder="이메일">
                        </div>
                        <div class="error-text-box">
                            <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        </div>
                    </div>  
                    <div>
                        <div class="input-type-1 w100per">
                            <input type="text" placeholder="대표자명">
                        </div>
                        <div class="error-text-box">
                            <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        </div>
                    </div>  
                </div>

                <!-- 입력값 3개 중 1개라도 일치하는게 없을 경우
                "입력하신 정보가 일치하지 않습니다."
                alert 띄우기 -->
                <a href="#" class="btn-type-1 bm w100per h50 bdr4 mt60" id="test">
                    <span class="text f16 tb cfff">아이디 찾기</span>
                </a>
            </div>

            <!-- 결과 -->
            <div>
                <p class="f16 tm c333">찾으시는 아이디는 "" 입니다.</p>
                <a href="/" class="btn-type-1 bm w100per h50 bdr4 mt60" id="test">
                    <span class="text f16 tb cfff">로그인</span>
                </a>
            </div>

        </li>

        <!-- 비밀번호 찾기 -->
        <li>

            <!-- 찾기 전 -->
            <div>
                <div class="flex fdc g8">
                    <div>
                        <div class="input-type-1 w100per">
                            <input type="text" placeholder="회사코드">
                        </div>
                        <div class="error-text-box">
                            <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        </div>
                    </div>  
                    <div>
                        <div class="input-type-1 w100per">
                            <input type="text" placeholder="아이디">
                        </div>
                        <div class="error-text-box">
                            <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        </div>
                    </div>  
                    <div>
                        <div class="input-type-1 w100per">
                            <input type="text" placeholder="이메일">
                        </div>
                        <div class="error-text-box">
                            <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        </div>
                    </div>  
                </div>

                <!-- 입력값 3개 중 1개라도 일치하는게 없을 경우
                "입력하신 정보가 일치하지 않습니다."
                alert 띄우기 -->
                <a href="#" class="btn-type-1 bm w100per h50 bdr4 mt60" id="test">
                    <span class="text f16 tb cfff">비밀번호 찾기</span>
                </a>
            </div>

            <!-- 결과 -->
            <div>
                <div class="flex fdc g8">
                    <div>
                        <div class="input-type-1 w100per">
                            <input type="password" placeholder="비밀번호">
                        </div>
                        <div class="error-text-box">
                            <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                        </div>
                    </div>  
                    <div>
                        <div class="input-type-1 w100per">
                            <input type="password" placeholder="비밀번호 확인">
                        </div>
                        <div class="error-text-box">
                            <span class="f13 mt8 cr">필수 입력 항목입니다.</span>
                            <span class="f13 mt8 cr">비밀번호가 일치하지 않습니다.</span>
                        </div>
                    </div>  
                </div>

                <!-- 비밀번호 변경되면 확인 버튼 누르면 로그인 페이지로 이동 -->
                <a href="/" class="btn-type-1 bm w100per h50 bdr4 mt60" id="test">
                    <span class="text f16 tb cfff">비밀번호 변경</span>
                </a>
            </div>

        </li>

    </ul>
</div>