import { Button } from 'primereact/button';

import { Dialog } from 'primereact/dialog';
import { ForwardedRef, forwardRef, useImperativeHandle, useState } from 'react';

interface CmpConfirmationDialogProps<T> {
    message: string;
    confirmationAction: { (param?: T): void };
    formatItem?: (param: T) => string;
}
function CmpConfirmationDialogInner<T>(props: CmpConfirmationDialogProps<T>, ref: ForwardedRef<unknown>) {
    const [confirmationDialog, setConfirmationDialog] = useState(false);
    const [item, setItem] = useState<T | null>(null);

    const hideDialog = () => {
        setConfirmationDialog(false);
        setItem(null);
    };
    const displayDialog = (param?: T) => {
        if (param !== undefined) {
            setItem(param);
        }
        setConfirmationDialog(true);
    };
    const actionDialog = () => {
        console.log('me' + item);
        if (item !== null) {
            props.confirmationAction(item);
        } else {
            props.confirmationAction();
        }

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
}

const CmpConfirmationDialog = forwardRef(CmpConfirmationDialogInner) as <T>(props: CmpConfirmationDialogProps<T> & { ref?: ForwardedRef<unknown> }) => ReturnType<typeof CmpConfirmationDialogInner>;

export default CmpConfirmationDialog;
