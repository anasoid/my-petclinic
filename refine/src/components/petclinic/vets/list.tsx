"use client";

import { MuiInferencer } from "@refinedev/inferencer/mui";
import React from "react";
import {
    useDataGrid,
    EditButton,
    ShowButton,
    DeleteButton,
    List,
    TagField,
} from "@refinedev/mui";
import { DataGrid, GridColDef } from "@mui/x-data-grid";
import { useMany } from "@refinedev/core";

export const VetsList = () => {
    const { dataGridProps } = useDataGrid();

    const { data: specialtiesData, isLoading: specialtiesIsLoading } = useMany({
        resource: "specialties",
        ids: [].concat(
            ...(dataGridProps?.rows?.map((item: any) => item?.specialties?.map((specialty: any) => specialty?.id)) ??
                []),
        ),
        queryOptions: {
            enabled: !!dataGridProps?.rows,
        },
    });

    const columns = React.useMemo<GridColDef[]>(
        () => [
            {
                field: "id",
                headerName: "Id",
                type: "number",
                minWidth: 50,
            },
            {
                field: "firstName",
                flex: 1,
                headerName: "First Name",
                minWidth: 200,
            },
            {
                field: "lastName",
                flex: 1,
                headerName: "Last Name",
                minWidth: 200,
            },
            {
                field: "specialties",
                flex: 1,
                headerName: "Specialties",
                minWidth: 300,
                renderCell: function render({ value }) {
                    return specialtiesIsLoading ? (
                        <>Loading...</>
                    ) : (
                        <>
                            {value?.map((item: any, index: number) => (
                                <TagField key={index} value={item?.name} />
                            ))}
                        </>
                    );
                },
            },
            {
                field: "actions",
                headerName: "Actions",
                sortable: false,
                renderCell: function render({ row }) {
                    return (
                        <>
                            <EditButton hideText recordItemId={row.id} />
                            <ShowButton hideText recordItemId={row.id} />
                        </>
                    );
                },
                align: "center",
                headerAlign: "center",
                minWidth: 80,
            },
        ],
        [specialtiesData?.data],
    );

    return (
        <List>
            <DataGrid {...dataGridProps} columns={columns} autoHeight />
        </List>
    );
};
