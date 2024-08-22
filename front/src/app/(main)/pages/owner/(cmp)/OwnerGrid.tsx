import CmpGrid from '@/components/crud/CmpGrid';

import { Column, ColumnBodyOptions } from 'primereact/column';
import { Dispatch, forwardRef, SetStateAction, useImperativeHandle, useRef } from 'react';

interface OwnerGridProps {
    dataKey?: string;
    title: string;
    data: any;
    selectedItems: any;
    setselectedItems: Dispatch<SetStateAction<null>>;
    actions?: (data: any, options: ColumnBodyOptions) => React.ReactNode;
}
const OwnerGrid = forwardRef(function OwnerGrid(props: OwnerGridProps, ref) {
    const dt = useRef(null);

    const exportCSV = () => {
        //@ts-ignore
        dt.current?.exportCSV();
    };
    useImperativeHandle(ref, () => ({
        exportCSV
    }));
    return (
        <>
            <CmpGrid ref={dt} data={props.data} dataKey={props.dataKey} title={props.title} actions={props.actions} selectedItems={props.selectedItems} setselectedItems={props.setselectedItems}>
                <Column selectionMode="multiple" headerStyle={{ width: '4rem' }}></Column>
                <Column field="id" header="Id" sortable headerStyle={{ minWidth: '15rem' }}></Column>

                <Column field="firstName" header="FirstName" sortable headerStyle={{ minWidth: '15rem' }}></Column>
                <Column field="lastName" header="lastName" sortable headerStyle={{ minWidth: '15rem' }}></Column>

                <Column field="address" header="Address"></Column>
                <Column field="city" header="City" sortable headerStyle={{ minWidth: '10rem' }}></Column>
            </CmpGrid>
        </>
    );
});

export default OwnerGrid;
