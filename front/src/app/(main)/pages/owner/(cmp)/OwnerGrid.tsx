import { formatCurrency } from '@/app/ui/Format';
import CmpGrid from '@/components/crud/CmpGrid';
import { Demo } from '@/types';

import { Column, ColumnBodyOptions } from 'primereact/column';
import { Dispatch, forwardRef, SetStateAction, useImperativeHandle, useRef } from 'react';

const OwnerGrid = forwardRef(function OwnerGrid(
    {
        title,
        data,
        actions,
        selectedItems,
        setselectedItems,
        dataKey = 'id'
    }: {
        dataKey?: string;
        title: string;
        data: any;
        selectedItems: any;
        setselectedItems: Dispatch<SetStateAction<null>>;
        actions?: (data: any, options: ColumnBodyOptions) => React.ReactNode;
    },
    ref
) {
    const dt = useRef(null);

    const exportCSV = () => {
        dt.current?.exportCSV();
    };
    useImperativeHandle(ref, () => ({
        exportCSV
    }));
    return (
        <>
            <CmpGrid ref={dt} data={data} dataKey={dataKey} title={title} actions={actions} selectedItems={selectedItems} setselectedItems={setselectedItems}>
                <Column selectionMode="multiple" headerStyle={{ width: '4rem' }}></Column>
                <Column field="code" header="Code" sortable headerStyle={{ minWidth: '15rem' }}></Column>
                <Column field="name" header="Name" sortable headerStyle={{ minWidth: '15rem' }}></Column>

                <Column field="price" header="Price" body={(p: Demo.Product) => formatCurrency(p.price as number)} sortable></Column>
                <Column field="category" header="Category" sortable headerStyle={{ minWidth: '10rem' }}></Column>
            </CmpGrid>
        </>
    );
});

export default OwnerGrid;
