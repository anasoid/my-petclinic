"use client";

import dataProviderSimpleRest from "@refinedev/simple-rest";

class ApiConfig {
  private apiUrl = "https://api.fake-rest.refine.dev";

  private apiPetClinicUrl = "http://localhost:8080/api";

  private authUrl = "http://localhost:8080/api";

  getApiUrl(): string {
    return this.apiUrl;
  }
  getApiPetClinicUrl(): string {
    return this.apiPetClinicUrl;
  }
  getAuthUrl(): string {
    return this.authUrl;
  }
}

export const apiConfig = new ApiConfig();
