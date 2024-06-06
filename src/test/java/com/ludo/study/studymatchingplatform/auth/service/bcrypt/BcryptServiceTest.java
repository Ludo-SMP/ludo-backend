package com.ludo.study.studymatchingplatform.auth.service.bcrypt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.util.Pair;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BcryptServiceTest {

    private BcryptService bcryptService = new BcryptService();

    @DisplayName("[Success] Bcrypt hash Test")
    @ParameterizedTest
    @MethodSource("generatePasswordPairs")
    void bcryptTest(final Pair<String, String> pair) {
        final String plainPassword = pair.getFirst();
        final String hashedPassword = pair.getSecond();
        final String hashedPassword2 = bcryptService.hashPassword(plainPassword);
        System.out.println(plainPassword);
        System.out.println(hashedPassword);
        System.out.println(hashedPassword2);

        assertThat(hashedPassword).isNotEqualTo(hashedPassword2);
        assertThat(hashedPassword2).isNotNull();
        assertThat(hashedPassword2.length()).isEqualTo(60);
    }

    @DisplayName("[Success] Bcrypt verify Test")
    @ParameterizedTest
    @MethodSource("generatePasswordPairs")
    void bcryptTest2(final Pair<String, String> pair) {
        final String plainPassword = pair.getFirst();
        final String hashedPassword = pair.getSecond();

        final boolean verified = bcryptService.verifyPassword(plainPassword, hashedPassword);
        assertThat(verified).isTrue();
    }

    @DisplayName("[Success] Bcrypt verify failure Test")
    @ParameterizedTest
    @MethodSource("generatePasswordPairs")
    void bcryptTest3(final Pair<String, String> pair) {
        final String plainPassword = pair.getFirst();
        final String hashedPassword = pair.getSecond();

        assertThat(bcryptService.verifyPassword(hashedPassword, plainPassword)).isFalse();
    }


    private static Stream<Pair<String, String>> generatePasswordPairs() {
        return Stream.of(
                Pair.of("ax&*asdbxa#12", "$2b$10$fiaLHLdtPh7NF07YhIOlreHEpNetqcoKQBxr6mwXba1NRSSowlzv."),
                Pair.of("123assdddaWD@#", "$2b$10$9bSa62fZ6G/47TuLC/452.R8vAAZcIHwCMPOamINLEkTiMhPSpEE6"),
                Pair.of("asdasd1KJD##2dq", "$2b$10$fKvBhYLvGO.9v1psmuGJUO7BWqaOrBpfeSZczk3nPXwP5RE.Pz7d."),
                Pair.of("&sdads2WW(wesD", "$2b$10$GScpq7msHwQ1Io4x5WEyRuZNB8st2ghQg46VubjzWe7WPnaJ0CiNG"),
                Pair.of("CXXCdads6DSidkkjmc(", "$2b$10$TgGgLzFgOcoxSS13szJyduvfuzJn4ak2VIIMJoW5LXpML161p/8py")
        );

    }


}