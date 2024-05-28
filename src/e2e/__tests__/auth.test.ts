import { AxiosError, HttpStatusCode } from "axios";
import dns from "node:dns";
import { describe } from "node:test";
import { ApiClient } from "../config/api-client";
import { fakeLoginBody, fakeSignupBody } from "../fixtures/auth-fixture";
import { randEmail, randNickname } from "../fixtures/rand-fixture";
import { login, signup } from "../helpers/auth-api-helper";
import { LoginResponse, SignupResponse } from "../types/auth-types";
import { BaseResponse } from "../types/base-types";

dns.setDefaultResultOrder("ipv4first");

describe("basic user authentication flows", () => {
  test("[200 OK] username/password 기반 가입 시 로그인 가능", async () => {
    // given
    const client = ApiClient.default();
    const { email, nickname, password } = fakeSignupBody();

    await signup(client, {
      email,
      nickname,
      password,
    });

    // when
    const resp = await login(client, {
      email,
      password,
    });

    const { user, accessToken } = resp.data!.data;

    expect(resp.status).toBe(HttpStatusCode.Ok);
    expect(accessToken).toBeDefined();
    expect(user.id).toBeDefined();
    expect(user.nickname).toBe(nickname);
    expect(user.email).toBe(email);
  });
  test("[201 CREATED] username/password 기반 회원 가입 가능", async () => {
    // given
    const client = ApiClient.default();
    const { email, nickname, password } = fakeSignupBody();

    // when
    const resp = await signup(client, {
      email,
      nickname,
      password,
    });

    // then
    const { accessToken, user } = resp.data.data;
    expect(resp.status).toBe(HttpStatusCode.Created);
    expect(accessToken).not.toBeUndefined();
    expect(typeof user.id).toBe("number");
    expect(user.nickname).toBe(nickname);
    expect(user.email).toBe(email);
  });
  
  it("[401 UNAUTHORIZED] 유효하지 않은 비밀번호로 로그인 불가", async () => {
    // given
    const client = ApiClient.default();
    const { email, nickname, password } = fakeSignupBody();

    await signup(client, {
      email,
      nickname,
      password,
    });

    // when
    try {
      await login(client, {
        email,
        password: "invalid password",
      });
    } catch (err) {
      const e = err as AxiosError<BaseResponse<LoginResponse>>;
      expect(e.response!.status).toBe(HttpStatusCode.Unauthorized);
      expect(e.response!.data.message).toBe("비밀번호가 일치하지 않습니다.");
    }
  });

  it("[404 NOT_FOUND] 가입되지 않은 이메일로 로그인 불가", async () => {
    // given
    const client = ApiClient.default();
    const { email, password } = fakeLoginBody();

    // when
    try {
      await login(client, {
        email,
        password,
      });
    } catch (err) {
      const e = err as AxiosError<BaseResponse<LoginResponse>>;
      expect(e.response!.status).toBe(HttpStatusCode.NotFound);
      expect(e.response!.data.message).toBe("가입되지 않은 이메일입니다.");
    }
  });

  test("[409 CONFLICT] 중복된 메일로 가입 불가", async () => {
    // given
    const client = ApiClient.default();
    const { email, nickname, password } = fakeSignupBody();

    // when
    // then
    await signup(client, {
      email,
      nickname,
      password,
    });

    try {
      await signup(client, {
        email,
        nickname: randNickname(),
        password,
      });
    } catch (err) {
      const e = err as AxiosError<BaseResponse<SignupResponse>>;
      expect(e.response!.status).toBe(HttpStatusCode.Conflict);
      expect(e.response!.data.message).toBe("이미 가입된 이메일입니다.");
    }
  });

  test("[409 CONFLICT] 중복 닉네임 가입 불가", async () => {
    // given
    const client = ApiClient.default();
    const { email, nickname, password } = fakeSignupBody();

    // when
    // then
    await signup(client, {
      email,
      nickname,
      password,
    });

    try {
      await signup(client, {
        email: randEmail(),
        nickname,
        password,
      });
    } catch (err) {
      const e = err as AxiosError<BaseResponse<SignupResponse>>;
      expect(e.response!.status).toBe(HttpStatusCode.Conflict);
      expect(e.response!.data.message).toBe("이미 존재하는 닉네임입니다.");
    }
  });
});
