import axios, {
  AxiosError,
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from "axios";
import jwt from "jsonwebtoken";
import { v4 as uuid } from "uuid";
import {
  transformFromIsoStringToNaiveDateTime,
  transformFromNaiveTimeToIsoString as transformFromNaiveDateTimeToIsoString,
} from "../helpers/transformers/date-transformer";
import {
  Transformer,
  transformObj,
} from "../helpers/transformers/transformers";
import { LoginResponse, SignupResponse } from "../types/auth-types";
import { BaseResponse } from "../types/base-types";
const { SCHEME, HOST, PORT } = process.env;

export class ApiClient {
  constructor(
    private clientId: string,
    private axiosClient: AxiosInstance,
    private resquestDataTransformers: Transformer[] = [],
    private responseDataTransformers: Transformer[] = [],
    private accessToken: string | null = null
  ) {
    this.clientId = clientId;
    this.axiosClient = axiosClient;
    this.accessToken = null;
    this.resquestDataTransformers = resquestDataTransformers;
    this.responseDataTransformers = responseDataTransformers;
  }

  static default(): ApiClient {
    const axiosClient = axios.create({
      baseURL: `${SCHEME}://${HOST}:${PORT}/api`,
      withCredentials: true,
    });
    const requestDataTransformers: Transformer[] = [
      transformFromNaiveDateTimeToIsoString,
    ];
    const responseDataTransformers: Transformer[] = [
      transformFromIsoStringToNaiveDateTime,
    ];
    const client = new ApiClient(
      uuid(),
      axiosClient,
      requestDataTransformers,
      responseDataTransformers
    );
    client._setupRequestInterceptor();
    client._setupResponsenterceptor();
    return client;
  }

  async get<T>(url: string, config?: AxiosRequestConfig<any> | undefined) {
    return this.axiosClient.get<BaseResponse<T>>(url, config);
  }

  async post<T>(
    url: string,
    body?: any,
    config?: AxiosRequestConfig<any> | undefined
  ) {
    return await this.axiosClient.post<BaseResponse<T>>(url, body, config);
  }

  async put<T>(
    url: string,
    body?: any,
    config?: AxiosRequestConfig<any> | undefined
  ) {
    return this.axiosClient.put<BaseResponse<T>>(url, body, config);
  }

  async delete<T>(url: string, config?: AxiosRequestConfig<any> | undefined) {
    return this.axiosClient.delete<BaseResponse<T>>(url, config);
  }

  private _setupRequestInterceptor() {
    this.axiosClient.interceptors.request.use(
      (req) => {
        req.data = transformObj(req.data, ...this.resquestDataTransformers);
        _attachAccessToken(this.accessToken, req);
        return req;
      },
      (err) => {
        console.log(err.message);
        return Promise.reject(err);
      }
    );
  }

  private _setupResponsenterceptor() {
    this.axiosClient.interceptors.response.use(
      (res) => {
        res.data = transformObj(res.data, ...this.responseDataTransformers);
        this.accessToken = _extractAccessToken(res) ?? this.accessToken;
        _printDecodedJwt(this.accessToken);
        return res;
      },
      (err) => {
        const e = err as AxiosError;
        // console.error(`statusCode: ${e.response?.status}`);
        // console.error(`data: ${JSON.stringify(e.response?.data)}`);
        // console.error(`stack: ${e.stack}`);
        return Promise.reject(err);
      }
    );
  }
}

function _attachAccessToken(
  accessToken: string | null,
  req: InternalAxiosRequestConfig
) {
  if (accessToken !== null) {
    if (req.headers["Cookie"] !== undefined) {
      if (req.headers["Cookie"]!.length > 0) {
        req.headers["Cookie"] += "; ";
      }
      req.headers["Cookie"] += `Authorization=${accessToken}`;
    } else {
      req.headers["Cookie"] = `Authorization=${accessToken}`;
    }
  }
}

function _extractAccessToken(
  res: AxiosResponse<
    BaseResponse<SignupResponse | LoginResponse>,
    SignupResponse | LoginResponse
  >
): string | undefined {
  const requestUrl = res.config.url;
  if (res.status < 300) {
    if (requestUrl === "/auth/login" || requestUrl === "/auth/signup") {
      if (!res.data.data.accessToken) {
        throw new Error("accessToken not found. something went wrong");
      }
      return res.data.data.accessToken;
    }
  }
  return undefined;
}

function _parseDateToUtcIsoString(dateTime: Date) {
  return dateTime.toISOString().replace("Z", "+00:00");
}

function _printDecodedJwt(token: string | null) {
  let claims;
  if (token !== null) {
    claims = jwt.decode(token, { complete: true });
  }
  // console.log("=============JWT CLAIMS===========");
  // console.log(token);
  // console.dir(claims);
  // console.log("==================================");
}
