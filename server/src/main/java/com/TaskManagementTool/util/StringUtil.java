package com.TaskManagementTool.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class StringUtil {

    public static boolean isNullOrWhiteSpace(String inputString){
        return inputString == null || inputString.trim().isEmpty();
    }
}
