import { DefaultDataBinder } from '@/app/ui/binder/DefaultDataBinder';
import { Button } from 'primereact/button';

import { OwnerDto } from '@gensrc/petclinic';
import { Dialog } from 'primereact/dialog';
import { InputText } from 'primereact/inputtext';
import { InputTextarea } from 'primereact/inputtextarea';
import { classNames } from 'primereact/utils';
import { forwardRef, useImperativeHandle, useState } from 'react';
import { InputMask } from 'primereact/inputmask';

interface OwnerEditDialogProps {
    saveAction: (item: OwnerDto) => boolean;
}

const OwnerEditDialog = forwardRef(function OwnerEditDialog(props: OwnerEditDialogProps, ref) {
    let emptyItem: OwnerDto = {
        firstName: '',
        lastName: '',
        city: '',
        telephone: '',
        address: '',
        pets: []
    };

    const [item, setItem] = useState<OwnerDto>(emptyItem);
    const [itemDialog, setItemDialog] = useState(false);
    const [submitted, setSubmitted] = useState(false);
    const dataBinder = new DefaultDataBinder<OwnerDto>(item, setItem);
    const hideDialog = () => {
        setSubmitted(false);
        setItemDialog(false);
    };
    const editItem = (item: OwnerDto) => {
        setItem({ ...item });
        setItemDialog(true);
    };
    const openNew = () => {
        setItem(emptyItem);
        setSubmitted(false);
        setItemDialog(true);
    };
    useImperativeHandle(ref, () => ({
        editItem,
        openNew
    }));
    const saveItem = () => {
        setSubmitted(true);

        if (props.saveAction(item)) {
            setItemDialog(false);
            setItem(emptyItem);
        }
    };
    const itemDialogFooter = (
        <>
            <Button label="Cancel" icon="pi pi-times" text onClick={hideDialog} />
            <Button label="Save" icon="pi pi-check" text onClick={saveItem} />
        </>
    );

    return (
        <Dialog visible={itemDialog} style={{ width: '450px' }} header={'Owner Details (' + item.id + ')'} modal className="p-fluid" footer={itemDialogFooter} onHide={hideDialog}>
            <div className="formgrid grid">
                <div className="field col">
                    <label htmlFor="name">firstName</label>
                    <InputText
                        id="firstName"
                        value={item.firstName}
                        onChange={(e) => dataBinder.onInputChange(e, 'firstName')}
                        required
                        autoFocus
                        className={classNames({
                            'p-invalid': submitted && !item.firstName
                        })}
                    />
                    {submitted && !item.firstName && <small className="p-invalid">FirstName is required.</small>}{' '}
                </div>
                <div className="field col">
                    <label htmlFor="lastName">LastName</label>
                    <InputText id="lastName" value={item.lastName} onChange={(e) => dataBinder.onInputChange(e, 'lastName')} />
                </div>
            </div>
            <div className="field">
                <label htmlFor="address">Address</label>
                <InputTextarea id="address" value={item.address} onChange={(e) => dataBinder.onInputChange(e, 'address')} required rows={3} cols={20} />
            </div>

            <div className="formgrid grid">
                <div className="field col">
                    <label htmlFor="price">City</label>
                    <InputText id="city" value={item.city} onChange={(e) => dataBinder.onInputChange(e, 'city')} />
                </div>
                <div className="field col">
                    <label htmlFor="telephone">Telephone</label>
                    <InputMask id="telephone" value={item.telephone} onChange={(e) => dataBinder.onInputMaskChangeEvent(e, 'telephone')} mask="9999999999" placeholder="0670707070"></InputMask>
                </div>
            </div>
        </Dialog>
    );
});

export default OwnerEditDialog;
