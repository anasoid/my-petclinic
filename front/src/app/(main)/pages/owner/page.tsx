/*  @next/next/no-img-element */
'use client';
import { OwnerService } from '@/business/service/OwnerService';
import CmpConfirmationDialog from '@/components/crud/CmpConfirmationDialog';
import { OwnerDto } from '@gensrc/petclinic';
import { Button } from 'primereact/button';
import { FileUpload } from 'primereact/fileupload';
import { Toast } from 'primereact/toast';
import { Toolbar } from 'primereact/toolbar';
import React, { useEffect, useRef, useState } from 'react';
import OwnerEditDialog from './(cmp)/OwnerEditDialog';
import OwnerGrid from './(cmp)/OwnerGrid';

/* @todo Used 'as any' for types here. Will fix in next version due to onSelectionChange event type issue. */
const PageOwner = () => {
    const [items, setItems] = useState<OwnerDto[]>([]);
    const [selectedItems, setSelectedItems] = useState(null);
    const toast = useRef<Toast>(null);
    const dtRef = useRef(null);
    const diagRef = useRef(null);
    const deleteRef = useRef(null);
    const deletesRef = useRef(null);
    const ownerService: OwnerService = new OwnerService();
    useEffect(() => {
        ownerService.list().then((data) => setItems(data as OwnerDto[]));
    }, []);

    const openNew = () => {
        // @ts-ignore
        diagRef.current?.openNew();
    };

    const saveItem = (item: OwnerDto): boolean => {
        if (item.firstName.trim()) {
            if (item.id) {
                ownerService
                    .save(item)
                    .then(
                        () =>
                            toast.current?.show({
                                severity: 'success',
                                summary: 'Successful',
                                detail: 'Item Updated',
                                life: 3000
                            }),
                        () =>
                            toast.current?.show({
                                severity: 'error',
                                summary: 'Error',
                                detail: 'Item updating error',
                                life: 3000
                            })
                    )
                    .then(() => {
                        let _item = { ...item };
                        //@ts-ignore
                        const index = findIndexById(item.id);
                        let _items = [...(items as any)];
                        _items[index] = _item;
                        setItems(_items as any);
                    })
                    .catch(() =>
                        toast.current?.show({
                            severity: 'error',
                            summary: 'Error',
                            detail: 'Item updating error',
                            life: 3000
                        })
                    );
            } else {
                ownerService
                    .save(item)
                    .then(
                        () =>
                            toast.current?.show({
                                severity: 'success',
                                summary: 'Successful',
                                detail: 'Item Created',
                                life: 3000
                            }),
                        () =>
                            toast.current?.show({
                                severity: 'error',
                                summary: 'Error',
                                detail: 'Item creation error',
                                life: 3000
                            })
                    )
                    .then(() => ownerService.list().then((data) => setItems(data as OwnerDto[])));
            }

            return true;
        } else {
            return false;
        }
    };

    const editItem = (item: OwnerDto) => {
        // @ts-ignore
        diagRef.current?.editItem(item);
    };

    const findIndexById = (id: number) => {
        let index = -1;
        for (let i = 0; i < (items as any)?.length; i++) {
            if ((items as any)[i].id === id) {
                index = i;
                break;
            }
        }

        return index;
    };

    const exportCSV = () => {
        // @ts-ignore
        dtRef.current?.exportCSV();
    };
    const confirmDelete = (owner: OwnerDto) => {
        // @ts-ignore
        deleteRef.current?.displayDialog(owner);
    };
    const confirmDeleteSelected = () => {
        // @ts-ignore
        deletesRef.current?.displayDialog(selectedItems);
    };

    const deleteItem = (item?: OwnerDto) => {
        let _items = (items as any)?.filter((val: any) => val.id !== item?.id);
        setItems(_items);
        ownerService.delete(item?.id).then(() => {
            toast.current?.show({
                severity: 'success',
                summary: 'Successful',
                detail: 'Item Deleted',
                life: 3000
            });
        });
    };

    const deleteSelectedItems = (itemToremove?: OwnerDto[]) => {
        let _items = (items as any)?.filter((val: any) => !(itemToremove as any)?.includes(val));
        setItems(_items);
        setSelectedItems(null);
        toast.current?.show({
            severity: 'success',
            summary: 'Successful',
            detail: 'Items Deleted',
            life: 3000
        });
    };

    const leftToolbarTemplate = () => {
        return (
            <React.Fragment>
                <div className="my-2">
                    <Button label="New" icon="pi pi-plus" severity="success" className=" mr-2" onClick={openNew} />
                    <Button label="Delete" icon="pi pi-trash" severity="danger" onClick={confirmDeleteSelected} disabled={!selectedItems || !(selectedItems as any).length} />
                </div>
            </React.Fragment>
        );
    };

    const rightToolbarTemplate = () => {
        return (
            <React.Fragment>
                <FileUpload mode="basic" accept="image/*" maxFileSize={1000000} chooseLabel="Import" className="mr-2 inline-block" />
                <Button label="Export" icon="pi pi-upload" severity="help" onClick={exportCSV} />
            </React.Fragment>
        );
    };

    const actionBodyTemplate = (rowData: OwnerDto) => {
        return (
            <>
                <Button icon="pi pi-pencil" rounded severity="success" className="mr-2" onClick={() => editItem(rowData)} />
                <Button icon="pi pi-trash" rounded severity="warning" onClick={() => confirmDelete(rowData)} />
            </>
        );
    };

    return (
        <div className="grid crud-demo">
            <div className="col-12">
                <div className="card">
                    <Toast ref={toast} />
                    <Toolbar className="mb-4" start={leftToolbarTemplate} end={rightToolbarTemplate}></Toolbar>
                    <OwnerGrid ref={dtRef} data={items} title="Manage Owner" actions={actionBodyTemplate} selectedItems={selectedItems} setselectedItems={setSelectedItems} />

                    <OwnerEditDialog ref={diagRef} saveAction={saveItem} />

                    <CmpConfirmationDialog<OwnerDto> ref={deleteRef} message={'Are you sure you want to delete '} confirmationAction={deleteItem} formatItem={(p: OwnerDto) => p.firstName} />

                    <CmpConfirmationDialog<undefined> ref={deletesRef} message="Are you sure you want to delete the selected items" confirmationAction={deleteSelectedItems} />
                </div>
            </div>
        </div>
    );
};

export default PageOwner;
