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
export class AuthManager {
  async login(username: string, password: string): Promise<boolean> {
    let token!: string;
    let accountInfo!: AccountInfo;
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
  }
  setCurrentUser(account: AccountInfo) {
    localStorage.setItem(USER_KEY, JSON.stringify(account));
  }
  setCurrentToken(token: string) {
    localStorage.setItem(TOKEN_KEY, token);
  }
  getCurrentUser(): AccountInfo | null {
    let strAccount = localStorage.getItem(USER_KEY);
    if (strAccount == null) {
      return null;
    } else {
      return JSON.parse(strAccount);
    }
  }

  getCurrentToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }
}

export const authManager: AuthManager = new AuthManager();
