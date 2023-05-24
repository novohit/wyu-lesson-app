package com.wyu.util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author novo
 * @since 2023-05-24
 */
public class Constant {
    public static final String[] DAY_OF_WEEK_LIST = new String[]{"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    public static final String[] SECTION_LIST = new String[]{"", "01\n02", "03\n04", "05\n06", "07\n08", "09\n10", "11\n12"};

    public static final int WEEK_LIST_LENGTH = 20;

    public static final String[] SELECTED_WEEK_LIST = new String[WEEK_LIST_LENGTH];

    public static final String[] SELECTED_TERM_LIST = {"202001", "202002", "202101", "202102", "202201", "202202"};

    static {

        for (int i = 0; i < WEEK_LIST_LENGTH; i++) {
            SELECTED_WEEK_LIST[i] = "第" + (i + 1) + "周";
        }
    }
}
