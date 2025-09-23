package com.korit.perfume.controller;

import com.korit.perfume.dto.ApiRespDto;
import com.korit.perfume.dto.mail.SendMailReqDto;
import com.korit.perfume.security.model.PrincipalUser;
import com.korit.perfume.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<ApiRespDto<?>> sendMail(@RequestBody SendMailReqDto sendMailReqDto,
                                                  @AuthenticationPrincipal PrincipalUser principalUser) {
        try {
            if (principalUser == null) {
                return ResponseEntity.status(401).body(new ApiRespDto<>("fail", "인증 정보가 없습니다.", null));
            }
            Map<String, Object> result = mailService.sendMail(sendMailReqDto, principalUser);
            return ResponseEntity.ok(new ApiRespDto<>("success", "메일 전송 완료", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiRespDto<>("fail", "메일 전송 실패: " + e.getMessage(), null));
        }
    }

    @GetMapping("/verify")
    public String verify(Model model, @RequestParam String verifyToken) {
        Map<String, Object> resultMap = mailService.verify(verifyToken);
        model.addAllAttributes(resultMap);
        return "result_page";  // verify 결과를 보여주는 뷰 이름
    }
}
