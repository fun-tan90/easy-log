package com.chj.easy.log.common;

import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;

/**
 * description banner打印
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/6 8:26
 */
public class BannerPrint {

    private static final String PROJECT_NAME = " ~~ easy-log ~~ ";

    private static final String GITEE = "Gitee: git@gitee.com:easy-log/easy-log.git";

    private static final String GITHUB = "Github: git@github.com:easy-log/easy-log.git";

    private static final int STRAP_LINE_SIZE = 50;

    /**
     * Print banner.
     */
    public static void printCollectorBanner() {
        String banner =
                        "                              _                             _ _           _             \n" +
                        "                             | |                           | | |         | |            \n" +
                        "   ___  __ _ ___ _   _ ______| | ___   __ _ ______ ___ ___ | | | ___  ___| |_ ___  _ __ \n" +
                        "  / _ \\/ _` / __| | | |______| |/ _ \\ / _` |______/ __/ _ \\| | |/ _ \\/ __| __/ _ \\| '__|\n" +
                        " |  __/ (_| \\__ \\ |_| |      | | (_) | (_| |     | (_| (_) | | |  __/ (__| || (_) | |   \n" +
                        "  \\___|\\__,_|___/\\__, |      |_|\\___/ \\__, |      \\___\\___/|_|_|\\___|\\___|\\__\\___/|_|   \n" +
                        "                  __/ |                __/ |                                            \n" +
                        "                 |___/                |___/                                             \n";

        String version = getVersion();
        version = (version != null) ? " jdk (v" + version + ")" : "no version.";

        StringBuilder padding = new StringBuilder();
        while (padding.length() < STRAP_LINE_SIZE - (version.length() + PROJECT_NAME.length())) {
            padding.append(" ");
        }
        print(banner, padding, version);
    }

    /**
     * Print banner.
     */
    public static void printComputeBanner() {
        String banner =
                        "                             _                                                   _       \n" +
                        "                            | |                                                 | |      \n" +
                        "  ___  __ _ ___ _   _ ______| | ___   __ _ ______ ___ ___  _ __ ___  _ __  _   _| |_ ___ \n" +
                        " / _ \\/ _` / __| | | |______| |/ _ \\ / _` |______/ __/ _ \\| '_ ` _ \\| '_ \\| | | | __/ _ \\\n" +
                        "|  __/ (_| \\__ \\ |_| |      | | (_) | (_| |     | (_| (_) | | | | | | |_) | |_| | ||  __/\n" +
                        " \\___|\\__,_|___/\\__, |      |_|\\___/ \\__, |      \\___\\___/|_| |_| |_| .__/ \\__,_|\\__\\___|\n" +
                        "                 __/ |                __/ |                         | |                  \n" +
                        "                |___/                |___/                          |_|                  \n";

        String version = getVersion();
        version = (version != null) ? " jdk (v" + version + ")" : "no version.";

        StringBuilder padding = new StringBuilder();
        while (padding.length() < STRAP_LINE_SIZE - (version.length() + PROJECT_NAME.length())) {
            padding.append(" ");
        }
        print(banner, padding, version);
    }

    /**
     * Print banner.
     */
    public static void printAdminBanner() {
        String banner =
                        "                              _                             _           _       \n" +
                        "                             | |                           | |         (_)      \n" +
                        "   ___  __ _ ___ _   _ ______| | ___   __ _ ______ __ _  __| |_ __ ___  _ _ __  \n" +
                        "  / _ \\/ _` / __| | | |______| |/ _ \\ / _` |______/ _` |/ _` | '_ ` _ \\| | '_ \\ \n" +
                        " |  __/ (_| \\__ \\ |_| |      | | (_) | (_| |     | (_| | (_| | | | | | | | | | |\n" +
                        "  \\___|\\__,_|___/\\__, |      |_|\\___/ \\__, |      \\__,_|\\__,_|_| |_| |_|_|_| |_|\n" +
                        "                  __/ |                __/ |                                    \n" +
                        "                 |___/                |___/                                     \n";

        String version = getVersion();
        version = (version != null) ? " jdk (v" + version + ")" : "no version.";

        StringBuilder padding = new StringBuilder();
        while (padding.length() < STRAP_LINE_SIZE - (version.length() + PROJECT_NAME.length())) {
            padding.append(" ");
        }
        print(banner, padding, version);
    }

    /**
     * get current software package version
     *
     * @return the version
     */
    public static String getVersion() {
        Package pkg = Package.getPackage("java.util");
        return pkg != null ? pkg.getImplementationVersion() : "";
    }

    public static void print(String banner, StringBuilder padding, String version) {
        System.out.println(AnsiOutput.toString(banner, AnsiColor.GREEN, PROJECT_NAME, AnsiColor.DEFAULT,
                padding.toString(), AnsiStyle.FAINT, version, "\n", GITEE, "\n", GITHUB, "\n"));
    }
}