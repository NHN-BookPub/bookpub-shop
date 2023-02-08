package com.nhnacademy.bookpubshop.health;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * L4 상태값을 위한 컨트롤러 입니다.
 *
 * @author : 유호철
 * @since : 1.0
 **/
@RestController
public class HealthCheckController {
    private boolean health = true;

    @GetMapping("/monitor/l7check")
    public ResponseEntity<String> getHealth() {
        if (health) {
            return ResponseEntity.ok("정상적 실행");
        }
        return ResponseEntity.badRequest()
                .body("배포를 위한 종료");
    }

    @GetMapping("/deploy/ready")
    public void readyToDeploy() {
        this.health = false;
    }
}
