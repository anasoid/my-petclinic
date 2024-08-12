import { Button } from 'primereact/button';

import { Dialog } from 'primereact/dialog';
import { forwardRef, useImperativeHandle, useState } from 'react';

interface CmpConfirmationDialogProps {
    message: string;
    confirmationAction: () => void;
}
const CmpConfirmationDialog = forwardRef(function CmpConfirmationDialog(props: CmpConfirmationDialogProps, ref) {
    const [confirmationDialog, setConfirmationDialog] = useState(false);

    const hideDialog = () => {
        setConfirmationDialog(false);
    };
    const displayDialog = () => {
        setConfirmationDialog(true);
    };
    const actionDialog = () => {
        props.confirmationAction();

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

    return (
        <Dialog visible={confirmationDialog} style={{ width: '450px' }} header="Confirm" modal footer={dialogFooter} onHide={hideDialog}>
            <div className="flex align-items-center justify-content-center">
                <i className="pi pi-exclamation-triangle mr-3" style={{ fontSize: '2rem' }} />
                <span>{props.message}</span>
            </div>
        </Dialog>
    );
});

export default CmpConfirmationDialog;
