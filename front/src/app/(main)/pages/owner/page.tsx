/* eslint-disable @next/next/no-img-element */
'use client';
import CmpConfirmationDialog from '@/components/crud/CmpConfirmationDialog';
import { Demo } from '@/types';
import { Button } from 'primereact/button';
import { FileUpload } from 'primereact/fileupload';
import { Toast } from 'primereact/toast';
import { Toolbar } from 'primereact/toolbar';
import React, { useEffect, useRef, useState } from 'react';
import { ProductService } from '../../../../demo/service/ProductService';
import OwnerEditDialog from './(cmp)/OwnerEditDialog';
import OwnerGrid from './(cmp)/OwnerGrid';

/* @todo Used 'as any' for types here. Will fix in next version due to onSelectionChange event type issue. */
const Crud = () => {
    let emptyProduct: Demo.Product = {
        id: '',
        name: '',
        image: '',
        description: '',
        category: '',
        price: 0,
        quantity: 0,
        rating: 0,
        inventoryStatus: 'INSTOCK'
    };

    const [products, setProducts] = useState(null);
    const [product, setProduct] = useState<Demo.Product>(emptyProduct);
    const [selectedProducts, setSelectedProducts] = useState(null);
    const toast = useRef<Toast>(null);
    const dtRef = useRef(null);
    const diagRef = useRef(null);
    const deleteRef = useRef(null);
    const deletesRef = useRef(null);

    useEffect(() => {
        ProductService.getProducts().then((data) => setProducts(data as any));
    }, []);

    const openNew = () => {
        diagRef.current?.openNew();
    };

    const saveProduct = (product: Demo.Product): boolean => {
        if (product.name.trim()) {
            let _products = [...(products as any)];
            let _product = { ...product };
            if (product.id) {
                const index = findIndexById(product.id);

                _products[index] = _product;
                toast.current?.show({
                    severity: 'success',
                    summary: 'Successful',
                    detail: 'Product Updated',
                    life: 3000
                });
            } else {
                let id = '';
                let chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
                for (let i = 0; i < 5; i++) {
                    id += chars.charAt(Math.floor(Math.random() * chars.length));
                }
                _product.id = id;
                _product.image = 'product-placeholder.svg';
                _products.push(_product);
                toast.current?.show({
                    severity: 'success',
                    summary: 'Successful',
                    detail: 'Product Created',
                    life: 3000
                });
            }

            setProducts(_products as any);
            return true;
        } else {
            return false;
        }
    };

    const editProduct = (product: Demo.Product) => {
        diagRef.current?.editProduct(product);
    };

    const findIndexById = (id: string) => {
        let index = -1;
        for (let i = 0; i < (products as any)?.length; i++) {
            if ((products as any)[i].id === id) {
                index = i;
                break;
            }
        }

        return index;
    };

    const exportCSV = () => {
        dtRef.current?.exportCSV();
    };
    const confirmDeleteProduct = (product: Demo.Product) => {
        setProduct(product);
        deleteRef.current?.displayDialog();
    };
    const confirmDeleteSelected = () => {
        deletesRef.current?.displayDialog();
    };

    const deleteProduct = () => {
        let _products = (products as any)?.filter((val: any) => val.id !== product.id);
        setProducts(_products);
        setProduct(emptyProduct);
        toast.current?.show({
            severity: 'success',
            summary: 'Successful',
            detail: 'Product Deleted',
            life: 3000
        });
    };

    const deleteSelectedProducts = () => {
        let _products = (products as any)?.filter((val: any) => !(selectedProducts as any)?.includes(val));
        setProducts(_products);
        setSelectedProducts(null);
        toast.current?.show({
            severity: 'success',
            summary: 'Successful',
            detail: 'Products Deleted',
            life: 3000
        });
        return true;
    };

    const leftToolbarTemplate = () => {
        return (
            <React.Fragment>
                <div className="my-2">
                    <Button label="New" icon="pi pi-plus" severity="success" className=" mr-2" onClick={openNew} />
                    <Button label="Delete" icon="pi pi-trash" severity="danger" onClick={confirmDeleteSelected} disabled={!selectedProducts || !(selectedProducts as any).length} />
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

    const actionBodyTemplate = (rowData: Demo.Product) => {
        return (
            <>
                <Button icon="pi pi-pencil" rounded severity="success" className="mr-2" onClick={() => editProduct(rowData)} />
                <Button icon="pi pi-trash" rounded severity="warning" onClick={() => confirmDeleteProduct(rowData)} />
            </>
        );
    };

    return (
        <div className="grid crud-demo">
            <div className="col-12">
                <div className="card">
                    <Toast ref={toast} />
                    <Toolbar className="mb-4" start={leftToolbarTemplate} end={rightToolbarTemplate}></Toolbar>
                    <OwnerGrid ref={dtRef} data={products} title="Manage Product" actions={actionBodyTemplate} selectedItems={selectedProducts} setselectedItems={setSelectedProducts} />

                    <OwnerEditDialog ref={diagRef} saveAction={saveProduct} />

                    <CmpConfirmationDialog ref={deleteRef} message={'Are you sure you want to delete ( ' + product.name + ' )?'} confirmationAction={deleteProduct} />

                    <CmpConfirmationDialog ref={deletesRef} message="Are you sure you want to delete the selected products?" confirmationAction={deleteSelectedProducts} />
                </div>
            </div>
        </div>
    );
};

export default Crud;
