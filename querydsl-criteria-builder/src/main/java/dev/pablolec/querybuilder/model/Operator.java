package dev.pablolec.querybuilder.model;

public enum Operator {
    EQ(false),
    NE(false),
    LIKE(false),
    NOT_LIKE(false),
    GT(false),
    LT(false),
    GTE(false),
    LTE(false),
    IN(true),
    NOT_IN(true),
    EXISTS(false),
    NOT_EXISTS(false);

    private final boolean isCollectionOperator;

    Operator(boolean isCollectionOperator) {
        this.isCollectionOperator = isCollectionOperator;
    }

    public static Operator fromString(String op) {
        return switch (op.toLowerCase()) {
            case "eq" -> EQ;
            case "ne" -> NE;
            case "like" -> LIKE;
            case "notlike" -> NOT_LIKE;
            case "gt" -> GT;
            case "lt" -> LT;
            case "gte" -> GTE;
            case "lte" -> LTE;
            case "in" -> IN;
            case "notin" -> NOT_IN;
            case "exists" -> EXISTS;
            case "notexists" -> NOT_EXISTS;
            default -> throw new IllegalArgumentException("Unsupported operator: " + op);
        };
    }

    public boolean isCollectionOperator() {
        return isCollectionOperator;
    }
}
