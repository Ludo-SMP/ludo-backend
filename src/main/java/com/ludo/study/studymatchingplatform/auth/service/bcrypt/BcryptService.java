package com.ludo.study.studymatchingplatform.auth.service.bcrypt;

import com.password4j.Password;
import org.springframework.stereotype.Service;

@Service
public class BcryptService {

    public String hashPassword(final String password) {
        System.out.println("============================");
        System.out.println("password : " + password);
        System.out.println("============================");
        return Password.hash(password)
//                .addRandomSalt()
                .withBcrypt()
                .getResult();
    }

    public boolean verifyPassword(final String password, final String hashedPassword) {
        System.out.println("============================");
        System.out.println("password : " + password);
        System.out.println("hashedpassword : " + hashedPassword);
        System.out.println("============================");
        try {
            Password.check(
                    password.getBytes(),
                    hashedPassword.getBytes()
            ).withBcrypt();
            return true;
        } catch (final Exception e) {
            return false;
        }
    }
}
