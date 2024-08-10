import { formatCurrency, formatDate, formatDecimal, formatMoney, formatMoneyM, formatPercent } from '@/app/ui/Format';
import { Demo } from '@/types';

import { Column, ColumnBodyOptions } from 'primereact/column';
import { DataTable } from 'primereact/datatable';
import { InputText } from 'primereact/inputtext';
import { Dispatch, forwardRef, SetStateAction, useImperativeHandle, useRef, useState } from 'react';

const OwnerGrid = forwardRef(function OwnerGrid(
    {
        title,
        data,
        actions,
        selectedItems,
        setselectedItems
    }: {
        title: string;
        data: any;
        selectedItems: any;
        setselectedItems: Dispatch<SetStateAction<null>>;
        actions: (data: any, options: ColumnBodyOptions) => React.ReactNode;
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
            dataKey="id"
            paginator
            rows={10}
            rowsPerPageOptions={[5, 10, 25]}
            className="datatable-responsive"
            paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
            currentPageReportTemplate="Showing {first} to {last} of {totalRecords} products"
            globalFilter={globalFilter}
            emptyMessage="No products found."
            header={header}
            responsiveLayout="scroll"
        >
            <Column selectionMode="multiple" headerStyle={{ width: '4rem' }}></Column>
            <Column field="code" header="Code" sortable headerStyle={{ minWidth: '15rem' }}></Column>
            <Column field="name" header="Name" sortable headerStyle={{ minWidth: '15rem' }}></Column>

            <Column field="price" header="Price" body={(p: Demo.Product) => formatCurrency(p.price as number)} sortable></Column>
            <Column field="category" header="Category" sortable headerStyle={{ minWidth: '10rem' }}></Column>
            <Column body={actions} header="actions" headerStyle={{ minWidth: '10rem' }}></Column>
        </DataTable>
    );
});

export default OwnerGrid;
