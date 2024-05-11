import { h } from "vue"
import type { Library } from "@/lib/search/types"
import { ColumnDef } from "@tanstack/vue-table"

const createColumn = (key: keyof Library, headerName: string): ColumnDef<Library> => ({
  accessorKey: key,
  header: () => h("div", { class: "text-left" }, headerName),
  cell: ({ row }) => {
    const formatted = row.getValue(key as string) || "-"
    // @ts-ignore
    return h("div", { class: "text-left font-medium" }, formatted)
  },
})

export const columns: ColumnDef<Library>[] = [
  createColumn("name", "Name"),
  createColumn("location", "Location"),
  createColumn("openingHours", "Opening Hours"),
  createColumn("establishedDate", "Established Date"),
  createColumn("website", "Website"),
  createColumn("email", "Email"),
  createColumn("phoneNumber", "Phone Number"),
  createColumn("isOpen", "Is Open"),
]
