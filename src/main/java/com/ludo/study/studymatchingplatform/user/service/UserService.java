package com.ludo.study.studymatchingplatform.user.service;

import com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker;
import com.ludo.study.studymatchingplatform.study.service.exception.SocialAccountNotFoundException;
import com.ludo.study.studymatchingplatform.user.domain.user.User;
import com.ludo.study.studymatchingplatform.user.repository.user.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepositoryImpl userRepository;
    private final UtcDateTimePicker utcDateTimePicker;

    public void withdraw(final User user) {

        final User foundUser = findById(user.getId());

        if (foundUser.isDeleted()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "비활성화 된 회원입니다. 복구하시려면 해당 계정으로 다시 회원 가입을 시도해주세요.");
        }

        user.softDelete(utcDateTimePicker.now());
    }

    public User findById(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new SocialAccountNotFoundException("가입되지 않은 회원입니다."));
    }

}
