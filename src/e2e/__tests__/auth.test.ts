import { AxiosError, HttpStatusCode } from "axios";
import { Knex } from "knex";
import dns from "node:dns";
import { describe } from "node:test";
import { ApiClient } from "../config/api-client";
import { db } from "../config/db-client";
import { fakeLoginBody, fakeSignupBody } from "../fixtures/auth-fixture";
import { randEmail, randNickname } from "../fixtures/rand-fixture";
import { login, signup } from "../helpers/auth-api-helper";
import { LoginResponse, SignupResponse } from "../types/auth-types";
import { BaseResponse } from "../types/base-types";

dns.setDefaultResultOrder("ipv4first");

let tx: Knex.Transaction<any, any[]>;

describe("basic user authentication flows", () => {
  beforeAll(async () => {
    await db("user").del();
  });

  beforeEach(async () => {
    tx = await db.transaction();
  });

  afterEach(async () => {
    await tx.commit();
  });

  it("should create a new user with given info", async () => {
    // given
    const client = ApiClient.newInstance();
    const { email, nickname, password } = fakeSignupBody();
    console.log(
      `email: ${email}, nickname: ${nickname}, password: ${password}`
    );

    // when
    const resp = await signup(client, {
      email,
      nickname,
      password,
    });

    // then
    const { user, accessToken } = resp.data!.data;

    expect(resp.status).toBe(HttpStatusCode.Created);
    expect(accessToken).not.toBeUndefined();
    expect(typeof user.id).toBe("number");
    expect(user.nickname).toBe(nickname);
    expect(user.email).toBe(email);
  });

  it("should fail to create new user with the duplicate email", async () => {
    // given
    const client = ApiClient.newInstance();
    const { email, nickname, password } = fakeSignupBody();
    console.log(
      `email: ${email}, nickname: ${nickname}, password: ${password}`
    );
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

  it("should fail to create new user with the duplicate nickname", async () => {
    // given
    const client = ApiClient.newInstance();
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

  it("should success to log in with signed up user", async () => {
    // given
    const client = ApiClient.newInstance();
    const { email, nickname, password } = fakeSignupBody();
    console.log(
      `email: ${email}, nickname: ${nickname}, password: ${password}`
    );

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
  it("should fail to log in with not registered email", async () => {
    // given
    const client = ApiClient.newInstance();
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

  it("should fail to log in with invalid password", async () => {
    // given
    const client = ApiClient.newInstance();
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
});
