"use client";

import type { AuthProvider } from "@refinedev/core";

import { authManager } from "./AuthManager";

export const authProvider: AuthProvider = {
  login: async ({ email, username, password, remember }) => {
    // Suppose we actually send a request to the back end here.
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
    authManager.logout();
    return {
      success: true,
      redirectTo: "/login",
    };
  },
  check: async () => {
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
    return authManager.getCurrentRoles();
  },
  getIdentity: async () => {
    return authManager.getCurrentUser();
  },
  onError: async (error) => {
    if (error.response?.status === 401) {
      return {
        logout: true,
      };
    }

    return { error };
  },
};
