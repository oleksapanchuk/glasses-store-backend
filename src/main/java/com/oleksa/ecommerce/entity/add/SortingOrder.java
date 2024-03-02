package com.oleksa.ecommerce.entity.add;

public enum SortingOrder {
    ASC("asc"),
    DESC("desc");

    public final String label;

    private SortingOrder(String label) {
        this.label = label;
    }
}
