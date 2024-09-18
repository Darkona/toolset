package com.darkona.toolset.logging;

import jakarta.annotation.Nullable;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.util.List;

@SuppressWarnings("unused")
public class LogStrings {

    private static final String u_d_top_l = "\u2554";//Character.toString(201);
    private static final String u_d_top_r = "\u2557";//Character.toString(187);
    private static final String u_d_bot_l = "\u255A";//Character.toString(200);
    private static final String u_d_bot_r = "\u255D";//Character.toString(188);
    private static final String u_d_hor = "\u2550";//Character.toString(205);
    private static final String u_d_ver = "\u2551";//Character.toString(186);
    private static final String u_d_top_t = "\u2566";//Character.toString(203);
    private static final String u_d_bot_t = "\u2569";//Character.toString(202);
    private static final String u_d_left_t = "\u2560";//Character.toString(204);
    private static final String u_d_right_t = "\u2563";//Character.toString(185);
    private static final String u_d_cross = "\u256C";//Character.toString(206);

    private static final String d_top_l = "&#9556";//Character.toString(201);
    private static final String d_top_r = "\187";//Character.toString(187);
    private static final String d_bot_l = "\200";//Character.toString(200);
    private static final String d_bot_r = "\188";//Character.toString(188);
    private static final String d_hor = "\205";//Character.toString(205);
    private static final String d_ver = "\186";//Character.toString(186);
    private static final String d_top_t = "\203";//Character.toString(203);
    private static final String d_bot_t = "\202";//Character.toString(202);
    private static final String d_left_t = "\204";//Character.toString(204);
    private static final String d_right_t = "\185";//Character.toString(185);
    private static final String d_cross = "\206";

    public static final String ORNAMENT = "||=============||";
    public static boolean isUtf = true;
    static boolean enabled = true;

    private LogStrings() {}

    private static String bannerizeInternal(String s, int width, String v, String h, String topL, String topR, String botL, String botR) {
        StringBuilder sb = new StringBuilder();
        var lines = s.split(System.lineSeparator());
        var top = topL + fill(h, width - 2) + topR;
        var bottom = botL + fill(h, width - 2) + botR;
        sb.append(top).append(System.lineSeparator());

        for (var line : lines) {
            sb.append(v).append(" ");
            sb.append(center(line, width - 2));
            sb.append(v).append(" ");
            sb.append(System.lineSeparator());
        }

        sb.append(bottom);
        return sb.toString();
    }

    public static String bannerize(String s, int width) {
        return isUtf ?
               bannerizeInternal(s, width, u_d_ver, u_d_hor, u_d_top_l, u_d_top_r, u_d_bot_l, u_d_bot_r) :
               bannerizeInternal(s, width, "|", "-", "+", "+", "+", "+");
    }

    public static String center(String s, int width) {
        if (s == null) return s;
        var msg = s.replaceAll("\\u001B\\[[\\d;]*[mK]", "");
        if (width <= msg.length()) return s;

        var half = (width - msg.length()) / 2;
        var space = (msg.length() % 2 == 0) ? (0 != width % 2) ?  half + 1 : half - 1 : half;
        return fill(" ", half) + s + fill(" ", space);
    }

    public static String fill(String s, int length) {
        return s.repeat(Math.max(0, length));
    }

    public static String rainbowColor(int i) {
        List<LogColor> rainbow = List.of(
                LogColor.RED,
                LogColor.ORANGE,
                LogColor.YELLOW,
                LogColor.GREEN,
                LogColor.BLUE,
                LogColor.AQUA,
                LogColor.PURPLE
        );
        return rainbow.get(i).get();
    }

    public static String rainbow(String s) {
        if (!enabled) return s;
        var r = new StringBuilder();
        int x = 0;
        for (int i = 0; i < s.length(); i++) {
            String c = Character.toString(s.charAt(i));
            if (!" ".equals(c) && !System.lineSeparator().equals(c)) {
                r.append(rainbowColor(x));
                x = (x + 1) % 7;
            } else if (!System.lineSeparator().equals(c)) {
                x = 0;
            }
            r.append(c);
        }
        r.append(LogColor.RESET);
        return r.toString();
    }

    public static String green(String s) {
        return enabled ? LogColor.GREEN + s + LogColor.RESET : s;
    }

    public static String red(String s) {
        return enabled ? LogColor.RED + s + LogColor.RESET : s;
    }

    public static String orange(String s) {
        return enabled ? LogColor.ORANGE + s + LogColor.RESET : s;
    }

    public static String blue(String s) {
        return enabled ? LogColor.BLUE + s + LogColor.RESET : s;
    }

    public static String yellow(String s) {
        return enabled ? LogColor.YELLOW + s + LogColor.RESET : s;
    }

    public static String pink(String s) {
        return enabled ? LogColor.PINK + s + LogColor.RESET : s;
    }

    public static String aqua(String s) {
        return enabled ? LogColor.AQUA + s + LogColor.RESET : s;
    }

    public static String purple(String s) {
        return enabled ? LogColor.PURPLE + s + LogColor.RESET : s;
    }

    public static String white(String s) {
        return enabled ? LogColor.WHITE + s + LogColor.RESET : s;
    }

    public static String gray(String s) {
        return enabled ? LogColor.GRAY + s + LogColor.RESET : s;
    }

    public static String darkGray(String s) {
        return enabled ? LogColor.DARK_GRAY + s + LogColor.RESET : s;
    }

    public static String darkRed(String s) {
        return enabled ? LogColor.DARK_RED + s + LogColor.RESET : s;
    }

    public static String darkGreen(String s) {
        return enabled ? LogColor.DARK_GREEN + s + LogColor.RESET : s;
    }

    public static String darkBlue(String s) {
        return enabled ? LogColor.DARK_BLUE + s + LogColor.RESET : s;
    }

    public static String darkAqua(String s) {
        return enabled ? LogColor.DARK_AQUA + s + LogColor.RESET : s;
    }

    public static String darkPurple(String s) {
        return enabled ? LogColor.DARK_PURPLE + s + LogColor.RESET : s;
    }

    public static String gold(String s) {
        return enabled ? LogColor.GOLD + s + LogColor.RESET : s;
    }

    public static String custom(Color c, String s) {
        return enabled ? "\u001B[38;2;" + c.getRed() + ";" + c.getGreen() + ";" + c.getBlue() + "m" + s + reset() : s;
    }

    public static String reset() {
        return enabled ? LogColor.RESET.get() : "";
    }

    public static String getMaskedString(String string, @Nullable Integer unmasked, @Nullable Character maskChar) {
        return getMaskedString(string.toCharArray(), unmasked, maskChar);
    }

    public static String getMaskedString(char[] bytes, @Nullable Integer unmasked, @Nullable Character maskChar) {
        if (unmasked == null || unmasked < 0) {
            unmasked = 0;
        }
        if (maskChar == null) {
            maskChar = '*';
        }
        var builder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            builder.append(i < unmasked ? bytes[i] : maskChar);
        }
        return builder.toString();
    }

    public static String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        return switch (day % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }

    public static String getSubStr(String str, int begin, int end) {
        if (StringUtils.hasLength(str)) {
            return (end <= str.length() && begin < end && begin >= 0) ? str.substring(begin, end) : str;
        }
        return "";
    }

    public static String getSubStr(String str, int begin, String delimiter) {
        if (StringUtils.hasLength(str)) {
            var trimmed = str.trim();
            var firstSpace = str.indexOf(delimiter);
            if (firstSpace == -1) {
                return trimmed;
            }
            return str.substring(begin, firstSpace);
        }
        return "";
    }

    public static String capitalize(String s){
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
