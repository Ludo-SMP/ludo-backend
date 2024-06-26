package com.ludo.study.studymatchingplatform.user.domain.user;

import com.ludo.study.studymatchingplatform.common.entity.BaseEntity;
import com.ludo.study.studymatchingplatform.user.domain.exception.UserExceptionMessage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "`user`")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Slf4j
@ToString(of = {"id", "nickname"})
public class User extends BaseEntity {

    public static final String DEFAULT_NICKNAME_PREFIX = "스따-디 %d";
    public static final Long DEFAULT_NICKNAME_ID = 716L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, columnDefinition = "char(10)")
    private Social social;

    @Column
    @Size(min = 1, max = 20)
    private String nickname;

    @Column(nullable = false, length = 320)
    @Email
    private String email;

    @Column(nullable = true, length = 100)
    private String password;

    public User(final Social social, final String nickname, final String email) {
        this.social = social;
        this.nickname = nickname;
        this.email = email;
    }

    public static User basic(final String nickname, final String email, final String password) {
        final User user = new User(Social.NONE, nickname, email);
        user.password = password;
        return user;
    }

    public void setInitialDefaultNickname() {
        validateInitDefaultNickname();
        this.nickname = String.format(DEFAULT_NICKNAME_PREFIX, DEFAULT_NICKNAME_ID + id);
    }

    private void validateInitDefaultNickname() {
        if (this.id == null) {
            throw new IllegalArgumentException(UserExceptionMessage.INIT_DEFAULT_NICKNAME.getMessage());
        }
    }

    public void changeNickname(final String nickname) {
        this.nickname = nickname;
    }

    public boolean equalsNickname(final String nickname) {
        return this.nickname.equals(nickname);
    }

}
