import { Column, ColumnBodyOptions } from 'primereact/column';
import { DataTable } from 'primereact/datatable';
import { InputText } from 'primereact/inputtext';
import { Dispatch, forwardRef, SetStateAction, useImperativeHandle, useRef, useState } from 'react';

interface CmpGridProps {
    children: React.ReactNode;
    dataKey?: string;
    title: string;
    data: any;
    selectedItems: any;
    setselectedItems: Dispatch<SetStateAction<null>>;
    actions?: (data: any, options: ColumnBodyOptions) => React.ReactNode;
}
const CmpGrid = forwardRef(function CmpGrid(props: CmpGridProps, ref) {
    const [globalFilter, setGlobalFilter] = useState('');
    const dt = useRef<DataTable<any>>(null);

    const header = (
        <div className="flex flex-column md:flex-row md:justify-content-between md:align-items-center">
            <h5 className="m-0">{props.title}</h5>
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
            value={props.data}
            selection={props.selectedItems}
            onSelectionChange={(e) => props.setselectedItems(e.value as any)}
            dataKey={props.dataKey}
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
            {props.children}
            {props.actions !== undefined ? <Column body={props.actions} header="actions" headerStyle={{ minWidth: '10rem' }}></Column> : null}
        </DataTable>
    );
});

export default CmpGrid;
