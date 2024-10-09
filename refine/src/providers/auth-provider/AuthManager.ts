import Cookies from "js-cookie";
import { authClient, AuthClient } from "./AuthClient";

export interface AccountInfo {
  id: number;
  login: string;
  firstName: string;
  lastName: string;
  email: string;
  imageUrl: string;
  activated: boolean;
  langKey: string;
  createdBy: string;
  createdDate: string;
  lastModifiedBy: string;
  lastModifiedDate: string;
  authorities: string[];
}
const USER_KEY: string = "AuthManager.currentUser";
const TOKEN_KEY: string = "AuthManager.token";
export const COOKIE_AUTH_KEY: string = "AuthManager.auth";
export class AuthManager {
  async login(username: string, password: string): Promise<boolean> {
    let token!: string;
    let result: boolean = false;
    await authClient.fetchLogin(username, password).then(async (resLogin) => {
      token = resLogin.id_token;
      await authClient
        .fetchAccount(token)
        .then((accountInfo) => {
          this.setCurrentUser(accountInfo);
          this.setCurrentToken(token);
        })
        .then(() => {
          result = true;
        });
    });

    if (!result) {
      this.logout();
    }
    return result;
  }

  logout() {
    localStorage.removeItem(USER_KEY);
    localStorage.removeItem(TOKEN_KEY);
    Cookies.remove(COOKIE_AUTH_KEY);
  }
  setCurrentUser(account: AccountInfo) {
    localStorage.setItem(USER_KEY, JSON.stringify(account));
    Cookies.set(COOKIE_AUTH_KEY, "true", {
      path: "/",
    });
  }
  setCurrentToken(token: string) {
    localStorage.setItem(TOKEN_KEY, token);
  }
  getCurrentUser(): AccountInfo | undefined {
    let strAccount = localStorage.getItem(USER_KEY);
    if (strAccount == null) {
      return undefined;
    } else {
      return JSON.parse(strAccount);
    }
  }
  getCurrentRoles(): string[] | undefined {
    let account = this.getCurrentUser();
    if (account) {
      return account.authorities;
    } else {
      return undefined;
    }
  }

  isAuthenticated(): boolean {
    let token = this.getCurrentToken();
    if (token) {
      return true;
    } else {
      return false;
    }
  }
  isServerAuthenticated(): boolean {
    return Cookies.get(COOKIE_KEY) == "true";
  }

  getCurrentToken(): string | undefined {
    let token = localStorage.getItem(TOKEN_KEY);
    return token == null ? undefined : token;
  }
}

export const authManager: AuthManager = new AuthManager();
