"use client";

import { apiConfig } from "@providers/api-provider";
import dataProviderSimpleRest from "@refinedev/simple-rest";


export const dataProvider = dataProviderSimpleRest(apiConfig.getApiUrl());
