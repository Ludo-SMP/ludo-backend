import { ApiClient } from "../config/api-client";
import {
  LoginBody,
  LoginResponse,
  SignupBody,
  SignupResponse,
} from "../types/auth-types";

export async function signup(
  apiClient: ApiClient,
  { nickname, email, password }: SignupBody
) {
  return apiClient.post<SignupResponse>("/auth/signup", {
    nickname,
    email,
    password,
  });
}

export async function login(
  apiClient: ApiClient,
  { email, password }: LoginBody
) {
  return apiClient.post<LoginResponse>("/auth/login", {
    email,
    password,
  });
}

// not typed!!!!!!!!!!!!!!! for temporal test
export async function myPage(
    apiClient: ApiClient,
) {
 return apiClient.get('/users/mypage');
}
