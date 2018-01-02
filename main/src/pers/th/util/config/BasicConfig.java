package pers.th.util.config;

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
				int length = 0;
				byte[] buffer = new byte[512];
				StringBuffer template = new StringBuffer();
				FileInputStream fis = new FileInputStream(file);
				while ((length = fis.read(buffer)) != -1) {
					template.append(new String(buffer, 0, length));
				}
				fis.close();
				String str = template.toString();
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
		Matcher matcher = Pattern.compile("\\$\\{([^\\}]+)\\}").matcher(value);
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			String matcherKey = matcher.group(1);
			String matchervalue = prop.getProperty(matcherKey);
			if (matchervalue != null) {
				matcher.appendReplacement(buffer, matchervalue);
			}
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}

	public String getTemplate(String key) {
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
