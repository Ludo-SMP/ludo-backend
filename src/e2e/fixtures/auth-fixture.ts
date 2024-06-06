import { LoginBody, SignupBody } from "../types/auth-types";
import { randEmail, randNickname, randPassword } from "./rand-fixture";

export type FakeSignupBodyArgs = {
  emailLen?: number;
  passwordLen?: number;
  nicknameLen?: number;
};

export type FakeLoginBodyArgs = Omit<FakeSignupBodyArgs, "nicknameLen">;

export function fakeSignupBody({
  emailLen,
  passwordLen,
  nicknameLen,
}: FakeSignupBodyArgs = {}): SignupBody {
  return {
    email: randEmail(emailLen),
    password: randPassword(passwordLen),
    nickname: randNickname(nicknameLen),
  };
}

export function fakeLoginBody({
  emailLen,
  passwordLen,
}: FakeLoginBodyArgs = {}): LoginBody {
  return {
    email: randEmail(emailLen),
    password: randPassword(passwordLen),
  };
}
