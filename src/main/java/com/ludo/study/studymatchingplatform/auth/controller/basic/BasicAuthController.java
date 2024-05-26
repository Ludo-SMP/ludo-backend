package com.ludo.study.studymatchingplatform.auth.controller.basic;

import com.ludo.study.studymatchingplatform.auth.common.AuthUserPayload;
import com.ludo.study.studymatchingplatform.auth.common.provider.CookieProvider;
import com.ludo.study.studymatchingplatform.auth.common.provider.JwtTokenProvider;
import com.ludo.study.studymatchingplatform.auth.service.basic.BasicAuthService;
import com.ludo.study.studymatchingplatform.auth.service.dto.BasicLoginResponse;
import com.ludo.study.studymatchingplatform.auth.service.dto.BasicSignupResponse;
import com.ludo.study.studymatchingplatform.auth.service.google.dto.request.BasicLoginRequest;
import com.ludo.study.studymatchingplatform.auth.service.google.dto.request.BasicSignupRequest;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BasicAuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieProvider cookieProvider;
    private final BasicAuthService basicAuthService;

    @PostMapping("/auth/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "회원 가입")
    @ApiResponse(description = "회원 가입 성공", responseCode = "201", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
    public BasicSignupResponse signup(@RequestBody final BasicSignupRequest request, final HttpServletResponse response) {

        log.info("requested sign up username = {}, nickname = {}", request.email(), request.nickname());
        final User user = basicAuthService.signup(request);
        final String accessToken = jwtTokenProvider.createAccessToken(AuthUserPayload.from(user));
        cookieProvider.setAuthCookie(accessToken, response);
        return BasicSignupResponse.of(user, accessToken);
    }

    @PostMapping("/auth/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(description = "로그인")
    @ApiResponse(description = "로그인 성공", responseCode = "200", useReturnTypeSchema = true, content = @Content(mediaType = "application/json"))
    public BasicLoginResponse login(@RequestBody final BasicLoginRequest request, final HttpServletResponse response) {
        log.info("requested log in username = {}", request.email());

        final User user = basicAuthService.login(request);
        final String accessToken = jwtTokenProvider.createAccessToken(AuthUserPayload.from(user));
        cookieProvider.setAuthCookie(accessToken, response);
        return BasicLoginResponse.of(user, accessToken);
    }

}
