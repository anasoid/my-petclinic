"use client";

import { Create, useAutocomplete } from "@refinedev/mui";
import { Box, TextField, Autocomplete } from "@mui/material";
import { useForm } from "@refinedev/react-hook-form";
import { Controller } from "react-hook-form";

export const VetsCreate = () => {
  const {
    saveButtonProps,
    refineCore: { formLoading },
    register,
    control,
    formState: { errors },
  } = useForm();

  const { autocompleteProps: specialtiesAutocompleteProps } = useAutocomplete({
    resource: "specialties",
  });

  return (
    <Create isLoading={formLoading} saveButtonProps={saveButtonProps}>
      <Box
        component="form"
        sx={{ display: "flex", flexDirection: "column" }}
        autoComplete="off"
      >
        <TextField
          {...register("firstName", {
            required: "This field is required",
          })}
          error={!!(errors as any)?.firstName}
          helperText={(errors as any)?.firstName?.message}
          margin="normal"
          fullWidth
          InputLabelProps={{ shrink: true }}
          type="text"
          label="First Name"
          name="firstName"
        />
        <TextField
          {...register("lastName", {
            required: "This field is required",
          })}
          error={!!(errors as any)?.lastName}
          helperText={(errors as any)?.lastName?.message}
          margin="normal"
          fullWidth
          InputLabelProps={{ shrink: true }}
          type="text"
          label="Last Name"
          name="lastName"
        />
        <Controller
          control={control}
          name="specialties"
          rules={{ required: "This field is required" }}
          // eslint-disable-next-line
          defaultValue={[] as any}
          render={({ field }) => (
            <Autocomplete
              {...specialtiesAutocompleteProps}
              {...field}
              multiple
              onChange={(_, value) => {
                field.onChange(
                  value?.map((item: any) => {
                    return { id: item?.id };
                  })
                );
              }}
              getOptionLabel={(item) => {
                return (
                  specialtiesAutocompleteProps?.options?.find(
                    (p) => p?.id?.toString() === (item?.id ?? item)?.toString()
                  )?.name ?? ""
                );
              }}
              isOptionEqualToValue={(option, value) =>
                value === undefined ||
                option?.id?.toString() === (value?.id ?? value)?.toString()
              }
              renderInput={(params) => (
                <TextField
                  {...params}
                  label="Specialties"
                  margin="normal"
                  variant="outlined"
                  error={!!(errors as any)?.specialties}
                  helperText={(errors as any)?.specialties?.message}
                  required
                />
              )}
            />
          )}
        />
      </Box>
    </Create>
  );
};
