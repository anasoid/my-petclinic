import { authManager, AuthManager } from "@providers/auth-provider/AuthManager";
import type { HttpError } from "@refinedev/core";
import axios from "axios";

const axiosInstancePetClinic = axios.create();

axiosInstancePetClinic.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    const customError: HttpError = {
      ...error,
      message: error.response?.data?.message,
      statusCode: error.response?.status,
    };

    return Promise.reject(customError);
  }
);

axiosInstancePetClinic.interceptors.request.use(
  (config) => {
    const token = authManager.getCurrentToken();
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    Promise.reject(error);
  }
);
export { axiosInstancePetClinic };
