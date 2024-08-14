import { InputNumberValueChangeEvent } from 'primereact/inputnumber';
import { Dispatch, SetStateAction } from 'react';

export class DefaultDataBinder<T> {
    setItem: Dispatch<SetStateAction<T>>;
    item: T;

    constructor(item: T, setItem: Dispatch<SetStateAction<T>>) {
        this.setItem = setItem;
        this.item = item;
    }
    onInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>, name: keyof T) => {
        const val = (e.target && e.target.value) || '';
        this.updateValue(val, name);
    };

    onInputNumberChange = (e: InputNumberValueChangeEvent, name: keyof T) => {
        const val = e.value || 0;
        this.updateValue(val, name);
    };

    updateValue(val: any, name: keyof T) {
        let _item = { ...this.item };
        _item[name] = val;
        this.setItem(_item);
    }
}
