"use client";

import type { AuthProvider } from "@refinedev/core";

import { authManager } from "./AuthManager";

export const authProvider: AuthProvider = {
  login: async ({ email, username, password, remember }) => {
    // Suppose we actually send a request to the back end here.
    console.log("login");
    let result: boolean = await authManager.login(email, password);

    if (result) {
      return {
        success: result,
        redirectTo: "/",
      };
    }

    return {
      success: result,
      error: {
        name: "LoginError",
        message: "Invalid username or password",
      },
    };
  },
  logout: async () => {
    console.log("logout");
    authManager.logout();
    return {
      success: true,
      redirectTo: "/login",
    };
  },
  check: async () => {
    console.log("check");
    const auth = authManager.isAuthenticated();
    if (auth) {
      return {
        authenticated: true,
      };
    }

    return {
      authenticated: false,
      logout: true,
      redirectTo: "/login",
    };
  },
  getPermissions: async () => {
    console.log("getPermissions");
    return authManager.getCurrentRoles();
  },
  getIdentity: async () => {
    console.log("getIdentity");
    return authManager.getCurrentUser();
  },
  onError: async (error) => {
    console.log("onError");
    if (error.response?.status === 401) {
      return {
        logout: true,
      };
    }

    return { error };
  },
};
