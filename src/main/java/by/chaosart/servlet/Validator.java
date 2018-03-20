package by.chaosart.servlet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Validator {

	public Map<String, String> validate(Map<String, String> paramMap,
			boolean defaultNotEmpty) {
		Map<String, String> messageMap = new HashMap<String, String>();
		for (Iterator<String> i = paramMap.keySet().iterator(); i.hasNext();) {
			String param = i.next();
			String value = paramMap.get(param);
			if (value.isEmpty()) {
				if (defaultNotEmpty) {
					messageMap.put(param, "Поле \"" + param
							+ "\" <br>является обязательным к заполнению!");
				}
			} else {
				String[] patterns = getPattern(param);
				if (!value.matches(patterns[0])) {
					messageMap.put(param, patterns[1]);
				}
			}
		}
		return messageMap;
	}

	public String validate(String param, String value, boolean defaultNotEmpty) {
		String message = null;
		if (value.isEmpty()) {
			if (defaultNotEmpty) {
				message = "Поле \"" + param
						+ "\" <br>является обязательным к заполнению!";
			}
		} else {
			String[] patterns = getPattern(param);
			if (!value.matches(patterns[0])) {
				message = patterns[1];
			}
		}
		return message;
	}

	private String[] getPattern(String param) {
		String patternValue = null;
		String patternMessage = null;
		if (param.equals("Имя") || param.equals("Фамилия")
				|| param.equals("Название категории")) {
			patternValue = "(^[A-Z]{1}[a-z]{0,20}$)|(^[А-Я]{1}[а-я]{0,20}$)";
			patternMessage = "Проверьте данные в поле  " + param + ".<br>"
					+ "(формат: Иван либо Ivan)";
		} else if (param.equals("Логин") || param.equals("Пароль")
				|| param.equals("Повторите пароль")
				|| param.equals("Имя художника")) {
			patternValue = "\\w*";
			patternMessage = "Проверьте данные в поле  " + param + ".<br>"
					+ "(допустимы лишь цифры и буквы)";
		} else if (param.equals("Название арта")) {
			patternValue = ".+\\.(jpg|png|jpeg|bmp|tif|gif)";
			patternMessage = "Проверьте данные в поле " + param + ".<br>"
					+ "(формат: название файла. Пример: арт1.jpg)";
		} else if (param.equals("Ссылка на оригинал")) {
			patternValue = "^(?i)http[s]{0,1}://.*$";
			patternMessage = "Проверьте данные в поле " + param + ".<br>"
					+ "(формат: http://... либо https://...)";
		}
		String[] patterns = { patternValue, patternMessage };
		return patterns;
	}
}