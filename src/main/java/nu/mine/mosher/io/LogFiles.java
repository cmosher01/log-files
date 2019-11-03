package nu.mine.mosher.io;

import com.sun.jna.platform.win32.*;

import java.io.File;
import java.nio.file.*;
import java.util.Optional;
import java.util.regex.*;

/**
 * Static utility to get the log {@link File} for an (application) {@link Class}
 */
public class LogFiles {
    /**
     * Gets the (OS specific) log file for the given (application) class.
     *
     * @param cls class, generally the main application class
     * @return log File (with mkdirs already called)
     */
    public static File getLogFileOf(final Class cls) {
        final String app = cls.getName();

        final Path dirLogs;
        final Optional<String> freedesktopCache = Optional.ofNullable(System.getenv("XDG_CACHE_HOME"));
        if (freedesktopCache.isPresent()) {
            dirLogs = Paths.get(freedesktopCache.get()).resolve(app).resolve("logs");
        } else if (os().startsWith("win")) {
            dirLogs = localAppData().resolve(app).resolve("Logs");
        } else if (os().startsWith("mac")) {
            dirLogs = home().resolve("Library").resolve("Logs").resolve(app);
        } else {
            dirLogs = home().resolve(".cache").resolve(app).resolve("logs");
        }

        final String nameLog = low(cls.getSimpleName())+".log";
        final File fileLog = dirLogs.resolve(nameLog).toFile();
        fileLog.getParentFile().mkdirs();
        return fileLog;
    }

    /**
     * Returns Windows local application data folder.
     * Defaults to %UserProfile%\AppData\Local if any error happens.
     * Intended to be called only when running under Windows OS.
     * @return LocalAppData folder
     */
    private static Path localAppData() {
        try {
            return Paths.get(Shell32Util.getKnownFolderPath(KnownFolders.FOLDERID_LocalAppData));
        } catch (final Throwable e) {
            // continue
        }
        try {
            return Paths.get(Shell32Util.getFolderPath(ShlObj.CSIDL_LOCAL_APPDATA));
        } catch (final Throwable e) {
            // continue
        }
        return home().resolve("AppData").resolve("Local");
    }

    /**
     * Current user's "home" directory, or "./" if any error happens
     * @return user home directory
     */
    private static Path home() {
        final String def = "./";
        try {
            return Paths.get(System.getProperty("user.home", def));
        } catch (final Throwable e) {
            // continue
        }
        return Paths.get(def);
    }

    /**
     * Returns OS name, in lowercase, or "*ux" if any error happens
     * @return lowercase OS name
     */
    private static String os() {
        final String def = "*ux";
        try {
            return System.getProperty("os.name", def).toLowerCase();
        } catch (final Throwable e) {
            // continue
        }
        return def;
    }

    private static final Pattern CAMEL = Pattern.compile("(?=[A-Z0-9])");

    /**
     * Converts CamelCaseName to lower-case-hypen-name
     * @param s CamelCaseName
     * @return lower-case-hypen-name
     */
    private static String low(String s) {
        final Matcher matcher = CAMEL.matcher(s);
        s = matcher.replaceAll("-");
        s = s.toLowerCase();
        if (s.startsWith("-")) {
            s = s.substring(1);
        }
        return s;
    }
}
