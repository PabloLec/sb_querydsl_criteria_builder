import { h } from 'vue'
import type { components } from '../api-schema';

const createColumn = (key: keyof components['schemas']['Library'], headerName: string): ColumnDef<components['schemas']['Library']> => ({
    accessorKey: key,
    header: () => h('div', { class: 'text-left' }, headerName),
    cell: ({ row }) => {
        const formatted = row.getValue(key as string) || '-';
        return h('div', { class: 'text-left font-medium' }, formatted);
    },
});

export const columns: ColumnDef<components['schemas']['Library']>[] = [
    createColumn('name', 'Name'),
    createColumn('location', 'Location'),
    createColumn('openingHours', 'Opening Hours'),
    createColumn('establishedDate', 'Established Date'),
    createColumn('website', 'Website'),
    createColumn('email', 'Email'),
    createColumn('phoneNumber', 'Phone Number'),
    createColumn('isOpen', 'Is Open')
];