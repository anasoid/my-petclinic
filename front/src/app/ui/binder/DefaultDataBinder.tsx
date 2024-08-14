import { InputNumberValueChangeEvent } from 'primereact/inputnumber';
import { Dispatch, SetStateAction } from 'react';
import { ObjectType } from 'typescript';

export class DefaultDataBinder<T extends ObjectType> {
    setItem: Dispatch<SetStateAction<T>>;
    item: T;

    constructor(item: T, setItem: Dispatch<SetStateAction<T>>) {
        this.setItem = setItem;
        this.item = item;
    }
    onInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>, name: string) => {
        const val = (e.target && e.target.value) || '';
        this.updateValue(val, name);
    };

    onInputNumberChange = (e: InputNumberValueChangeEvent, name: string) => {
        const val = e.value || 0;
        this.updateValue(val, name);
    };

    updateValue(val: any, name: string) {
        let _item = { ...this.item };
        _item[`${name}`] = val;
        this.setItem(_item);
    }
}
