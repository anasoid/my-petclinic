export const formatDate = (value: Date) => {
    return value.toLocaleDateString('fr-FR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
};

export const formatDecimal = (value: number) => {
    return new Number(value).toFixed(2);
};

export const formatPercent = (value: number) => {
    return formatDecimal(value * 100) + '%';
};

export const formatMoneyK = (value: number) => {
    return Math.round(value / 1000) + 'K';
};

export const formatMoneyM = (value: number) => {
    return Math.round(value / 1000) / 10 + 'm';
};
export const formatMoney = (value: number) => {
    return Math.round(value);
};

export const formatCurrency = (value: number) => {
    return value.toLocaleString('en-US', {
        style: 'currency',
        currency: 'USD'
    });
};
