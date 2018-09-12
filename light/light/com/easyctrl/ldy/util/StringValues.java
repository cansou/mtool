package com.easyctrl.ldy.util;

public class StringValues {
    public static String[] air = new String[]{"\u7a7a\u8c03"};
    public static String[] curtain = new String[]{"\u7a97\u5e18"};
    public static String[] electrical = new String[]{"\u7535\u5668"};
    public static String[] moduleLamp = new String[]{"\u706f", "\u540a\u706f", "\u53f0\u706f", "\u7b52\u706f", "\u58c1\u706f", "\u69fd\u706f", "\u5c04\u706f", "\u8d70\u9a6c\u706f", "\u5730\u811a\u706f", "\u65e5\u5149\u706f", "\u8fc7\u9053\u706f", "\u955c\u706f", "\u5438\u9876\u706f", "\u6295\u5149\u706f", "\u5e73\u677f\u706f", "\u843d\u5730\u706f"};
    public static String[] moduleType = new String[]{"\u7167\u660e", "\u7a97\u5e18", "\u7535\u5668", "\u7a7a\u8c03"};
    public static String[] week = new String[]{"\u661f\u671f\u65e5", "\u661f\u671f\u4e00", "\u661f\u671f\u4e8c", "\u661f\u671f\u4e09", "\u661f\u671f\u56db", "\u661f\u671f\u4e94", "\u661f\u671f\u516d"};

    public static String[] getModuleName() {
        return new String[]{"\u706f", "\u7a97\u5e18", "\u95e8\u7a97", "\u7535\u5668", "\u540a\u706f", "\u53f0\u706f", "\u7b52\u706f", "\u58c1\u706f", "\u69fd\u706f", "\u5c04\u706f", "\u8d70\u9a6c\u706f", "\u5730\u811a\u706f", "\u65e5\u5149\u706f", "\u8fc7\u9053\u706f", "\u955c\u706f", "\u7a97\u5e18", "\u5408\u5e18", "\u767e\u53f6\u5e18", "\u5377\u5e18", "\u98ce\u7434\u5e18", "\u7535\u52a8\u95e8", "\u7535\u52a8\u7a97", "\u7535\u89c6\u673a", "\u98ce\u6247", "\u7a7a\u8c03", "\u97f3\u54cd"};
    }

    public static int getWeekNum(String value) {
        for (int i = 0; i < week.length; i++) {
            if (value.equals(week[i])) {
                return i;
            }
        }
        return -1;
    }
}
