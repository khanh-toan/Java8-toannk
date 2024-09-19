package org.shopping.utils;

import org.shopping.common.ApiInputException;

import java.util.List;

public class ValidateUtils {
    public static void checkNullOrEmpty(Object value, String fieldName) {
        if (value == null) {
            throw new ApiInputException(fieldName + " is required!");
        }

        if (value instanceof String strValue) {
            if (strValue.trim().isEmpty()) {
                throw new ApiInputException(fieldName + " cannot be empty!");
            }
        }

        if (value instanceof List<?> listValue) {
            if (listValue.isEmpty()) {
                throw new ApiInputException(fieldName + " cannot be an empty list!");
            }
        }
    }

    //nếu trường đấy có thể null thì để min = 0, max = giới hạn trong database
    public static String checkLength(String value, String fieldName, Integer min, Integer max) {
        if(value == null)
            return null;
        value = value.trim();
        if(value.length() < min || value.length() > max)
            throw new ApiInputException("Length of " + fieldName.toLowerCase() + " must be in range " + min + " to " + max + " characters!");

        return value;
    }

    public static void checkDoubleInRange(Double value, String fieldName, Double min, Double max){
        if(value == null)
            return;
        if(min != null && max != null && (value < min || value > max))
            throw new ApiInputException(fieldName + " must be in range " + min + " to " + max + "!");

        if(min != null && value < min)
            throw new ApiInputException(fieldName + " must be >= " + min + "!");

        if(max != null && value > max)
            throw new ApiInputException(fieldName + " must be <= " + max + "!");
    }
}
