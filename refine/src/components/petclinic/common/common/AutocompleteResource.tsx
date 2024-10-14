"use client";

import { Autocomplete, TextField } from "@mui/material";
import { BaseKey } from "@refinedev/core";
import { useAutocomplete } from "@refinedev/mui";
import { Control, Controller, FieldErrors, FieldValues } from "react-hook-form";

interface AutocompleteResourceProps<
  TFieldValues extends FieldValues = FieldValues
> {
  resource: string;
  control: Control<TFieldValues>;
  errors: FieldErrors<FieldValues>;
  required?: boolean;
  defaultValue?: BaseKey | BaseKey[];
}
export const AutocompleteResource = (props: AutocompleteResourceProps) => {
  const { autocompleteProps: autocompleteProps } = useAutocomplete({
    resource: props.resource,
    defaultValue: props.defaultValue,
  });
  let labelResource: string =
    props.resource.charAt(0).toUpperCase() + props.resource.slice(1);

  return (
    <Controller
      control={props.control}
      name={props.resource}
      rules={{
        required: props.required ? "This field is required" : undefined,
      }}
      // eslint-disable-next-line
      defaultValue={[] as any}
      render={({ field }) => (
        <Autocomplete
          {...autocompleteProps}
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
              autocompleteProps?.options?.find(
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
              label={labelResource}
              margin="normal"
              variant="outlined"
              error={!!(props.errors as any)[props.resource]}
              helperText={
                (props.errors as any)[props.resource] &&
                ((props.errors as any)[props.resource] as any)["message"]
              }
              required={props.required}
            />
          )}
        />
      )}
    />
  );
};
