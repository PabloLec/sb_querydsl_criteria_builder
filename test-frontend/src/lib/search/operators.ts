import { Operator } from "@/lib/search/types.ts"

export const operators: Operator[] = [
  { op: "eq", label: "equals to" },
  { op: "ne", label: "not equals to" },
  { op: "like", label: "contains" },
  { op: "notlike", label: "does not contain" },
  { op: "gt", label: "greater than" },
  { op: "lt", label: "less than" },
  { op: "gte", label: "greater than or equal to" },
  { op: "lte", label: "less than or equal to" },
  { op: "in", label: "in" },
  { op: "notin", label: "not in" },
  { op: "exists", label: "exists" },
  { op: "notexists", label: "does not exist" },
]

export const fieldTypeToOperators: Record<string, Operator[]> = {
  string: operators.filter((op) => ["eq", "ne", "like", "notlike", "in", "notin"].includes(op.op)),
  enum: operators.filter((op) => ["eq", "ne"].includes(op.op)),
  number: operators.filter((op) => ["eq", "ne", "gt", "lt", "gte", "lte", "in", "notin"].includes(op.op)),
  boolean: operators.filter((op) => ["eq", "ne"].includes(op.op)),
  date: operators.filter((op) => ["gt", "lt", "gte", "lte"].includes(op.op)),
  datetime: operators.filter((op) => ["gt", "lt", "gte", "lte"].includes(op.op)),
  subquery: operators.filter((op) => ["exists", "notexists"].includes(op.op)),
}
