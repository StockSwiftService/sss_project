<div class="w100per bsb pt120 pb120 mgc" style="max-width: 480px;">
    <h1 class="c121619 tb tac f32">회원가입</h1>
    <div class="flex fdc g36 mt60">
        <div>
            <h2 class="c333 f16 tm mb8">회사명<span class="cr f16 tm inblock">*</span></h2>
            <div class="input-type-1 w100per">
                <input type="text" placeholder="회사명">
            </div>
            <div class="error-text-box">
                <span class="f13 mt4 cr">필수 입력 항목입니다.</span>
            </div>
        </div>
        <div>
            <h2 class="c333 f16 tm mb8">사업자 번호<span class="cr f16 tm inblock">*</span></h2>
            <div class="flex g8">
                <div class="input-type-1 w100per">
                    <input type="text" placeholder="사업자 번호 (-자 빼고 입력해 주세요.)">
                </div>
                <button class="btn-type-1 w80 f15 bdr4 b333 cfff">확인</button>
            </div>
            <div class="error-text-box">
                <span class="f13 mt4 cr">필수 입력 항목입니다.</span>
                <span class="f13 mt4 cr">사용하실 수 없는 사업자 번호 입니다.</span>
                <span class="f13 mt4 cg">사용하실 수 있는 사업자 번호 입니다.</span>
            </div>
        </div>
        <div>
            <h2 class="c333 f16 tm mb8">이메일<span class="cr f16 tm inblock">*</span></h2>
            <div class="flex g8">
                <div class="input-type-1 w100per">
                    <input type="text" placeholder="이메일">
                </div>
                <button class="btn-type-1 w80 f15 bdr4 b333 cfff">전송</button>
            </div>
            <div class="error-text-box">
                <span class="f13 mt4 cr">필수 입력 항목입니다.</span>
                <span class="f13 mt4 cr">사용하실 수 없는 이메일 입니다.</span>
                <span class="f13 mt4 cg">이메일 인증 번호가 발송되었습니다.</span>
            </div>
            <div class="flex g8 mt8">
                <div class="input-type-1 w100per">
                    <input type="text" placeholder="인증 번호">
                </div>
                <button class="btn-type-1 w80 f15 bdr4 b333 cfff">확인</button>
            </div>
            <div class="error-text-box">
                <span class="f13 mt4 cr">필수 입력 항목입니다.</span>
                <span class="f13 mt4 cr">인증 번호가 일치하지 않습니다.</span>
                <span class="f13 mt4 cg">인증이 완료되었습니다.</span>
            </div>
        </div>
        <div>
            <h2 class="c333 f16 tm mb8">대표자명<span class="cr f16 tm inblock">*</span></h2>
            <div class="input-type-1 w100per">
                <input type="text" placeholder="대표자명">
            </div>
            <div class="error-text-box">
                <span class="f13 mt4 cr">필수 입력 항목입니다.</span>
            </div>  
        </div>
        <div>
            <h2 class="c333 f16 tm mb8">아이디<span class="cr f16 tm inblock">*</span></h2>
            <div class="input-type-1 w100per">
                <input type="text" placeholder="아이디">
            </div>
            <div class="error-text-box">
                <span class="f13 mt4 cr">필수 입력 항목입니다.</span>
            </div>  
        </div>
        <div>
            <h2 class="c333 f16 tm mb8">비밀번호<span class="cr f16 tm inblock">*</span></h2>
            <div class="input-type-1 w100per">
                <input type="password" placeholder="비밀번호">
            </div>
            <div class="error-text-box">
                <span class="f13 mt4 cr">필수 입력 항목입니다.</span>
            </div>
            <div class="input-type-1 w100per mt8">
                <input type="password" placeholder="비밀번호 확인">
            </div>
            <div class="error-text-box">
                <span class="f13 mt4 cr">필수 입력 항목입니다.</span>
                <span class="f13 mt4 cr">비밀번호가 일치하지 않습니다.</span>
            </div>
        </div>
        <div>
            <h2 class="c333 f16 tm mb8">생년월일<span class="cr f16 tm inblock">*</span></h2>
            <div class="input-type-1 w100per">
                <input type="date" placeholder="생년월일">
            </div>
            <div class="error-text-box">
                <span class="f13 mt4 cr">필수 입력 항목입니다.</span>
            </div>
        </div>
    </div>

    <!-- 입력값이 모두 통과하면
    "회원가입 신청이 완료되었습니다.\n관리자의 승인 후 서비스를 이용하실 수 있습니다.\n승인 여부는 입력하신 메일로 발송이 되며, 신청일 기준 2-3 소요될 수 있습니다."
    alert 호출 후 닫으면 페이지 닫히게-->
    <a href="#" class="btn-type-1 bm w100per h50 bdr4 mt60">
        <span class="text f16 tb cfff">회원가입</span>
    </a>
</div>