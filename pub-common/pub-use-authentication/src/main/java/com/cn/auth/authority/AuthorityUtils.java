package com.cn.auth.authority;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

public class AuthorityUtils {


	public static Map<String, StringBuilder[]> getMenuAuthority(final String packageName) {
		//final String packageName = "com.cabinet.eip.controller";
		Set<Class<?>> classes = new LinkedHashSet<>();
		String packageDir = packageName.replace(".", "/");
		final boolean isRecursive = true;
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Enumeration<URL> dirs = loader.getResources(packageDir);
			URL url = dirs.nextElement();
			String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
			FileFilter cff = new FileFilter() {
				@Override
				public boolean accept(File file) {
					// 过滤规则(是否递归查找子目录、class文件)
					return (isRecursive && file.isDirectory())
							|| (file.getName().endsWith(".class") && file.getName().indexOf("$") == -1);
				}
			};
			findAllClass(packageName, filePath, cff, loader, classes);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String, StringBuilder[]> results = new HashMap<>();
		for (Class<?> cls : classes) {
			Method[] methods = cls.getDeclaredMethods();
			for (Method method : methods) {
				Authentication auth = method.getAnnotation(Authentication.class);
				if (auth == null)
					continue;
				String menu = auth.menu();
				if ("".equals(menu))
					continue;
				StringBuilder[] val = results.get(menu);
				if (val == null) {
					val = new StringBuilder[] { new StringBuilder(), new StringBuilder() };
					results.put(menu, val);
				}
				AuthorityType[] type = auth.type();
				for (AuthorityType at : type) {
					String level = String.valueOf(at.getLevel());
					String[] strs = val[0].toString().split(",");
					List<String> jurisdList = Arrays.asList(strs);
					if (at.getLevel() != 0 && !jurisdList.contains(String.valueOf(level))) {
						if (val[0].length() > 0) {
							val[0].append(",");
							val[1].append(",");
						}
						val[0].append(level);
						val[1].append(at.getName());
					}
				}
			}
		}
		return results;
	}


	private static void findAllClass(String packageName, String filePath, FileFilter fileFilter, ClassLoader loader,
			Set<Class<?>> classes) {
		File dir = new File(filePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		File[] dirfiles = dir.listFiles(fileFilter);
		for (File file : dirfiles) {
			if (file.isDirectory()) {
				findAllClass(packageName + "." + file.getName(), file.getAbsolutePath(), fileFilter, loader, classes);
			} else {
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					// 衍生的问题:Class.forName与ClassLoader.loadClass的区别
					classes.add(loader.loadClass(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
				}
			}
		}
	}

}
