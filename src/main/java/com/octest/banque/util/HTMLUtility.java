package com.octest.banque.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.octest.banque.bean.DropdownListBean;



/**
 * HTML Utility class to produce HTML contents like Dropdown List.
 *
 */
public class HTMLUtility {
	/**
	 * Create HTML SELECT list from MAP paramters values
	 *
	 * @param name
	 * @param selectedVal
	 * @param map
	 * @return
	 * 
	 * Whqt is your ,ysal p
	 * qsszord
	 * 
	 * ,pasworld is : azertyui
	 * 
	 */

	public static String getList(String name, String selectedVal,HashMap<String, String> map) {
		
		/*Map<String, String> treeMap = new LinkedHashMap<String, String>(map);*/
	
		StringBuffer sb = new StringBuffer(	"<select class='input-md emailinput form-control' style='margin-bottom: 10px' name='" + name + "'>");

		Set<String> keys = map.keySet();
		String val = null;
		String select = "-----Select-----";
		sb.append("<option selected value='" + select + "'>" + select+ "</option>");
		for (String key : keys) {
			val = map.get(key);
			if (key.trim().equals(selectedVal)) {

				sb.append("<option selected value='" + key + "'>" + val
						+ "</option>");
			} else {
				sb.append("<option value='" + key + "'>" + val + "</option>");
			}
		}
		sb.append("</select>");
		return sb.toString();
	}

	/**
	 * Create HTML SELECT List from List parameter
	 *
	 * @param name
	 * @param selectedVal
	 * @param list
	 * @return
	 */
	public static String getList(String name, String selectedVal, List list) {
		
		Collections.sort(list);

		List<DropdownListBean> dd = (List<DropdownListBean>) list;

		StringBuffer sb = new StringBuffer("<select class='input-md emailinput form-control' style='margin-bottom: 10px' name='" + name + "' id='" + name+ "'>");
		
		String key = null;
		
		String val = null;
		
		String select = "-----Select-----";
		
		sb.append("<option selected value='" + select + "'>" + select+ "</option>");
		
		for (DropdownListBean obj : dd) {
			key = obj.getKey();
			val = obj.getValue();

			if (key.trim().equals(selectedVal)) {
				sb.append("<option selected value='" + key + "'>" + val+ "</option>");
			} else {
				sb.append("<option value='" + key + "'>" + val + "</option>");
			}
		}

		sb.append("</select>");
		return sb.toString();
	}
}
