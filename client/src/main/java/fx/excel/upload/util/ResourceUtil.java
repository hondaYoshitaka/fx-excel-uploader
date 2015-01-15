package fx.excel.upload.util;

import java.net.URL;

public class ResourceUtil {
	
	public static URL getResourceNoException(String path, String extension) {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		return getResourceNoException(path, extension, contextClassLoader);
	}
	
	public static URL getResourceNoException(String path, String extension, ClassLoader loader) {
		if (path == null || loader == null) {
			return null;
		}
		path = getResourcePath(path, extension);
		return loader.getResource(path);
	}
	
	public static String getResourcePath(String path, String extension) {
		if (extension == null) {
			return path;
		}
		extension = "." + extension;
		if (path.endsWith(extension)) {
			return path;
		}
		return path.replace('.', '/') + extension;
	}
}
