package com.baidu.iov.dueros.waimai.utils;

import com.baidu.iov.dueros.waimai.net.Config;
import com.baidu.iov.dueros.waimai.net.entity.base.RequestBase;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author pengqm
 * @name film
 * @class name：com.baidu.iov.dueros.film.utils
 * @time 2018/10/11 17:52
 * @change
 * @class describe
 */

public class CommonUtils {

    public static String sign(RequestBase requestBase) {
        Map<String, String> map = getAllFields(requestBase);
        StringBuffer dataStr = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue() != null) {
                try {
					if ("sign".equals(entry.getKey())) {
						continue;
					}
                    dataStr.append(entry.getKey() + "=" + URLEncoder.encode(entry.getValue(),"utf-8"));
                } catch (UnsupportedEncodingException e) {
                    return "sign error!";
                }
            }
        }

        String signStr = Config.PREFIX_SIGN + dataStr.toString() + Config.KEY_SIGN;

        return MD5Util.getMD5String(signStr);
    }

    private static String getFieldValueByFieldName(String fieldName, Object object, Class clazz) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object).toString();
        } catch (Exception e) {
            return null;
        }
    }


    public static <T extends RequestBase> Map<String, String> getAllFields(T request) {
        TreeMap<String, String> map = new TreeMap();
        Class clazz = request.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            ArrayList<Field> list = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
            for (Field field : list) {
                String fieldName = field.getName();
                if (fieldName.startsWith("shadow$")) {
                    continue;
                }
                if (field.isSynthetic()) {
                    continue;
                }
                if (field.getName().equals("serialVersionUID")) {
                    continue;
                }
                String attr = getFieldValueByFieldName(fieldName, request, clazz);
                if (attr == null) {
                    continue;
                }
                    map.put(fieldName, attr);
            }
            fieldList.addAll(list);
            clazz = clazz.getSuperclass();

        }
        return map;
    }
}
