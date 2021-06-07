package com.garage.upskills.util;

import com.garage.upskills.domain.Employee;
import com.garage.upskills.domain.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.LinkedHashMap;

public class DomainObjectUtil {

    //convert message data to domain object based on key, currently only Employee and Training are implemented
    //use reflection to set object attributes, and currently only support String and LocalDate datatype
    private static final Logger logger = LoggerFactory.getLogger(DomainObjectUtil.class);

    public static Object convertToClass (String key, Object data) {

        Object returnObject;

        try {
            Class<?> loadClass;

            switch (key) {
                case "employee":
                    loadClass = Class.forName(Employee.class.getName());
                    returnObject = new Employee();
                    break;
                case "training":
                    loadClass = Class.forName(Training.class.getName());
                    returnObject = new Training();
                    break;
                default:
                    logger.warn("invalid key - " + key);
                    return null;
            }

            LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) data;
            for (var field: map.keySet()) {
                String methodName = "set" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
                Method method;
                if (field.contains("Date")) {
                    method = loadClass.getMethod(methodName, LocalDate.class);
                    method.invoke(returnObject, LocalDate.parse(map.get(field), DateConstant.DATE_TIME_FORMATTER));
                }
                else {
                    method = loadClass.getMethod(methodName, String.class);
                    method.invoke(returnObject, map.get(field));
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

        return returnObject;
    }
}
