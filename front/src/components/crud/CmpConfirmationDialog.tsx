import { Button } from 'primereact/button';

import { Dialog } from 'primereact/dialog';
import { forwardRef, useImperativeHandle, useState } from 'react';

interface CmpConfirmationDialogProps {
    message: string;
    confirmationAction: (param?: any) => void;
    formatItem?: (param: any) => string;
}
const CmpConfirmationDialog = forwardRef(function CmpConfirmationDialog(props: CmpConfirmationDialogProps, ref) {
    const [confirmationDialog, setConfirmationDialog] = useState(false);
    const [item, setItem] = useState(null);

    const hideDialog = () => {
        setConfirmationDialog(false);
        setItem(null);
    };
    const displayDialog = (param?: any) => {
        setItem(param);
        setConfirmationDialog(true);
    };
    const actionDialog = () => {
        props.confirmationAction(item);
        hideDialog();
    };

    useImperativeHandle(ref, () => ({
        hideDialog,
        displayDialog
    }));
    const dialogFooter = (
        <>
            <Button label="No" icon="pi pi-times" text onClick={hideDialog} />
            <Button label="Yes" icon="pi pi-check" text onClick={actionDialog} />
        </>
    );

    function defaultFormat(param: any): string {
        if (props.formatItem === undefined || param === null) {
            return '';
        } else {
            return props.formatItem(param);
        }
    }

    return (
        <Dialog visible={confirmationDialog} style={{ width: '450px' }} header="Confirm" modal footer={dialogFooter} onHide={hideDialog}>
            <div className="flex align-items-center justify-content-center">
                <i className="pi pi-exclamation-triangle mr-3" style={{ fontSize: '2rem' }} />
                <span>
                    {props.message}
                    {item !== undefined ? <b> {defaultFormat(item)} </b> : null}?
                </span>
            </div>
        </Dialog>
    );
});

export default CmpConfirmationDialog;
