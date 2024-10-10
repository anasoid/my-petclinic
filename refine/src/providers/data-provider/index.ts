"use client";

import { apiConfig } from "@providers/api-provider";
import dataProviderSimpleRest from "@refinedev/simple-rest";
import { DataProviders } from "@refinedev/core";
import { petClinicDataProvider } from "./petclinic-data-provider";

export const dataProviders = {
  default: dataProviderSimpleRest(apiConfig.getApiUrl()),
  petclinic: petClinicDataProvider(apiConfig.getApiPetClinicUrl()),
} as DataProviders;
