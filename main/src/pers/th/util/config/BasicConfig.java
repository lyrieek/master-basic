package pers.th.util.config;

import pers.th.util.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicConfig {

	private Properties prop;

	private Map<String, String> templates;

	public void loadProperties() {
		try {
			prop = new Properties();
			prop.load(new FileInputStream(new File("set.properties")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readTemplates() {
		try {
			File files = new File(get("template_path"));
			if (!files.exists() && !files.isDirectory()) {
				System.out.println("not find");
				return;
			}
			for (File file : files.listFiles()) {
				String str = IOUtils.reader(file);
				while (true) {
					// update time,not matcher!
					Matcher matcher = Pattern.compile("\\$\\{[a-zA-Z0-9_-]{1,}\\}").matcher(str);
					if (!matcher.find()) {
						break;
					}
					String switchs = str.substring(matcher.start(), matcher.end());
					String cases = switchs.substring(2, switchs.length() - 1);
					switch (cases) {
					case "demoName":
						str = str.replace(switchs, get("demo_name"));
						break;
					case "DemoName":
						String demoName = get("demo_name");
						demoName = (demoName.charAt(0) + "").toUpperCase() + demoName.substring(1);
						str = str.replace(switchs, demoName);
						break;
					}
				}
				addTemplate(file.getName(), str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String get(String key) {
		String value = prop.getProperty(key);
		Matcher matcher = Pattern.compile("\\$\\{([^}]+)}").matcher(value);
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			String matcherValue = prop.getProperty(matcher.group(1));
			if (matcherValue != null) {
				matcher.appendReplacement(buffer, matcherValue);
			}
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}

	public String template(String key) {
		return templates.get(key);
	}

	public void addTemplate(String key, String value) {
		prop.put(key, value);
		templates.put(key, value);
	}

	public Map<String, String> getTemplates() {
		return templates;
	}

}
