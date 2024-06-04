import { User } from "./users-types";

export type SignupBody = {
  nickname: string;
  password: string;
  email: string;
};

export type LoginBody = {
  email: string;
  password: string;
};

export type SignupResponse = {
  accessToken: string;
  user: User;
};

export type LoginResponse = SignupResponse;
