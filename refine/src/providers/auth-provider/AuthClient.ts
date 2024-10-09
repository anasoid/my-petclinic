import { apiConfig } from "@providers/api-provider";
import { AccountInfo } from "./AuthManager";

interface AuthResponse {
  id_token: string;
}

export class AuthClient {
  async fetchAccount(token: String): Promise<AccountInfo> {
    return await fetch(apiConfig.getAuthUrl() + "/account", {
      method: "get",

      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    }).then(async (response) => {
      if (!response.ok) {
        throw response;
      }
      return (await response.json()) as unknown as AccountInfo;
    });
  }

  async fetchLogin(username: String, password: string): Promise<AuthResponse> {
    return await fetch(apiConfig.getAuthUrl() + "/authenticate", {
      method: "post",

      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: username,
        password: password,
        rememberMe: false,
      }),
    }).then(async (response) => {
      if (!response.ok) {
        throw response;
      }
      return await response.json() as unknown as AuthResponse;
    });
  }
}
export const authClient: AuthClient = new AuthClient();
