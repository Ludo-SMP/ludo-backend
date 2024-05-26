import axios, { AxiosInstance, AxiosRequestConfig } from "axios";
import { v4 as uuid } from "uuid";
const { SCHEME, HOST, PORT } = process.env;

export class ApiClient {
  constructor(
    private clientId: string,
    private axiosClient: AxiosInstance,
    private accessToken: string | null = null
  ) {
    this.clientId = clientId;
    this.axiosClient = axiosClient;
    this.accessToken = null;
  }

  static newInstance(): ApiClient {
    const axiosClient = axios.create({
      baseURL: `${SCHEME}://${HOST}:${PORT}/api`,
      withCredentials: true,
    });
    const client = new ApiClient(uuid(), axiosClient);
    client._setupRequestInterceptor();
    client._setupResponsenterceptor();
    return client;
  }

  async get<T>(url: string, config?: AxiosRequestConfig<any> | undefined) {
    return this.axiosClient.get<T>(url, config);
  }

  async post<T>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig<any> | undefined
  ) {
    return this.axiosClient.post<T>(url, data, config);
  }

  async put<T>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig<any> | undefined
  ) {
    return this.axiosClient.put<T>(url, data, config);
  }

  _setupRequestInterceptor() {
    this.axiosClient.interceptors.request.use(
      (req) => {
        console.log("before cookies: " + req.headers["Cookie"]);
        console.log(`accessToken: ${this.accessToken}`);
        if (this.accessToken !== null) {
          if (req.headers["Cookie"] !== undefined) {
            if (req.headers["Cookie"]!.length > 0) {
              req.headers["Cookie"] += "; ";
            }
            req.headers["Cookie"] += `Authorization=${this.accessToken}`;
          } else {
            req.headers["Cookie"] = `Authorization=${this.accessToken}`;
          }
        }
        console.log("after cookies: " + req.headers["Cookie"]);
        return req;
      },
      (error) => {
        return Promise.reject(error);
      }
    );
  }

  _setupResponsenterceptor() {
    this.axiosClient.interceptors.response.use(
      (res) => {
        const requestUrl = res.config.url;
        if (res.status < 300) {
          if (requestUrl === "/auth/login" || requestUrl === "/auth/signup") {
            if (res.data?.data?.accessToken === undefined) {
              throw new Error("accessToken not found. something went wrong");
            }
            this.accessToken = res.data.data.accessToken;
          }
        }
        return res;
      },
      (err) => {
        return Promise.reject(err);
      }
    );
  }
}
