import type { AuthProvider } from "@refinedev/core";
import { cookies } from "next/headers";
import { COOKIE_AUTH_KEY } from "./AuthManager";

export const authProviderServer: Pick<AuthProvider, "check"> = {
  check: async () => {
    const cookieStore = cookies();
    const auth = cookieStore.get(COOKIE_AUTH_KEY);

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
};
