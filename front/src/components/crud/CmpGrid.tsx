import { formatCurrency } from '@/app/ui/Format';
import { Demo } from '@/types';

import { Column, ColumnBodyOptions } from 'primereact/column';
import { DataTable } from 'primereact/datatable';
import { InputText } from 'primereact/inputtext';
import { Dispatch, forwardRef, SetStateAction, useImperativeHandle, useRef, useState } from 'react';

const CmpGrid = forwardRef(function CmpGrid(
    {
        children,
        title,
        data,
        actions,
        selectedItems,
        setselectedItems,
        dataKey = 'id'
    }: {
        children: any;
        dataKey?: string;
        title: string;
        data: any;
        selectedItems: any;
        setselectedItems: Dispatch<SetStateAction<null>>;
        actions?: (data: any, options: ColumnBodyOptions) => React.ReactNode;
    },
    ref
) {
    const [globalFilter, setGlobalFilter] = useState('');
    const dt = useRef<DataTable<any>>(null);

    const header = (
        <div className="flex flex-column md:flex-row md:justify-content-between md:align-items-center">
            <h5 className="m-0">{title}</h5>
            <span className="block mt-2 md:mt-0 p-input-icon-left">
                <i className="pi pi-search" />
                <InputText type="search" onInput={(e) => setGlobalFilter(e.currentTarget.value)} placeholder="Search..." />
            </span>
        </div>
    );
    const exportCSV = () => {
        dt.current?.exportCSV();
    };
    useImperativeHandle(ref, () => ({
        exportCSV
    }));
    return (
        <DataTable
            ref={dt}
            value={data}
            selection={selectedItems}
            onSelectionChange={(e) => setselectedItems(e.value as any)}
            dataKey={dataKey}
            paginator
            rows={10}
            rowsPerPageOptions={[5, 10, 25]}
            className="datatable-responsive"
            paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
            currentPageReportTemplate="Showing {first} to {last} of {totalRecords} items"
            globalFilter={globalFilter}
            emptyMessage="No items found."
            header={header}
            responsiveLayout="scroll"
        >
            {children}
            {actions !== undefined ? <Column body={actions} header="actions" headerStyle={{ minWidth: '10rem' }}></Column> : null}
        </DataTable>
    );
});

export default CmpGrid;
