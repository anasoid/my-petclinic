"use client";

import { apiConfig } from "@providers/api-provider";
import dataProviderSimpleRest from "@refinedev/simple-rest";
import { DataProviders } from "@refinedev/core";
import { petClinicDataProvider } from "./petclinic-data-provider";

 const defaultDataProvider = dataProviderSimpleRest(apiConfig.getApiUrl());



export const dataProviders = {
  default: defaultDataProvider,
  petclinic: petClinicDataProvider(apiConfig.getApiPetClinicUrl()),
} as DataProviders;
