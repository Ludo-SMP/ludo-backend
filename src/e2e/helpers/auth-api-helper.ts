import { ApiClient } from "../config/api-client";
import {
  LoginBody,
  LoginResponse,
  SignupBody,
  SignupResponse,
} from "../types/auth-types";
import { BaseResponse } from "../types/base-types";

export async function signup(
  apiClient: ApiClient,
  { nickname, email, password }: SignupBody
) {
  const resp = await apiClient.post<BaseResponse<SignupResponse>>(
    "/auth/signup",
    {
      nickname,
      email,
      password,
    }
  );
  return resp;
}

export async function login(
  apiClient: ApiClient,
  { email, password }: LoginBody
) {
  const resp = await apiClient.post<BaseResponse<LoginResponse>>(
    "/auth/login",
    {
      email,
      password,
    }
  );
  return resp;
}
