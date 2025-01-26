package com.ziqiang.sushuodorm.utils;

public class SqlUtils {
    public static boolean validSortField(String sortField) {
        if (sortField == null || sortField.isEmpty()) {
            return true;
        }
        for (String field : sortField.split(",")) {
            if (!field.matches("^\\w+\\s+\\w+$")) {
                return false;
            }
        }
        return true;
    }
}