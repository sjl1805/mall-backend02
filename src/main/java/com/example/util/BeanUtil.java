package com.example.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.*;

public class BeanUtil {

    /**
     * 复制对象属性（支持基础类型自动转换）
     * @param source 源对象
     * @param targetClass 目标类
     * @return 目标对象实例
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        if (source == null) return null;
        
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Bean copy failed", e);
        }
    }

    /**
     * 列表转换（支持集合类型转换）
     * @param sourceList 源列表
     * @param targetClass 目标类
     * @return 转换后的列表
     */
    public static <T, E> List<T> copyList(List<E> sourceList, Class<T> targetClass) {
        if (sourceList == null || sourceList.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<T> result = new ArrayList<>(sourceList.size());
        for (E source : sourceList) {
            result.add(copyProperties(source, targetClass));
        }
        return result;
    }

    /**
     * 自定义字段映射转换
     * @param source 源对象
     * @param targetClass 目标类
     * @param fieldMappings 字段映射关系（key: 源字段名, value: 目标字段名）
     * @return 目标对象实例
     */
    public static <T> T convertWithMapping(Object source, Class<T> targetClass, Map<String, String> fieldMappings) {
        if (source == null) return null;
        
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanWrapper srcWrap = new BeanWrapperImpl(source);
            BeanWrapper destWrap = new BeanWrapperImpl(target);
            
            for (Map.Entry<String, String> entry : fieldMappings.entrySet()) {
                String srcField = entry.getKey();
                String destField = entry.getValue();
                
                if (srcWrap.isReadableProperty(srcField) && destWrap.isWritableProperty(destField)) {
                    Object value = srcWrap.getPropertyValue(srcField);
                    destWrap.setPropertyValue(destField, value);
                }
            }
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Bean conversion with mapping failed", e);
        }
    }

    /**
     * 获取空值属性名数组
     * @param source 源对象
     * @return 空值属性名数组
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        return emptyNames.toArray(new String[0]);
    }

    /**
     * 忽略空值复制属性
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyPropertiesIgnoreNull(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }
} 