package li.cil.manual.api.util;

import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;

public final class PathUtils {
    @SuppressWarnings("UnstableApiUsage")
    public static String resolve(final String base, final String path) {
        final String absolutePath;
        if (path.startsWith("/")) {
            absolutePath = StringUtils.stripStart(path, "/");
        } else {
            final int lastSlash = base.lastIndexOf('/');
            if (lastSlash >= 0) {
                absolutePath = base.substring(0, lastSlash + 1) + path;
            } else {
                absolutePath = path;
            }
        }

        return Files.simplifyPath(absolutePath);
    }
}
