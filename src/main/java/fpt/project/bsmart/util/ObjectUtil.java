package fpt.project.bsmart.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class ObjectUtil {

    private static final Logger logger = LoggerFactory.getLogger(ObjectUtil.class);

    public static <T, K> K copyProperties(T source, K target, Class<K> clazz) {
        if (source == null) {
            return null;
        }
        if (target == null) {
            try {
                target = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error(e.getMessage());
            }
        }
        if (target != null) {
            BeanUtils.copyProperties(source, target);
        }
        return target;
    }

    public static <T, K> void copyProperties(T source, K target) {
        if (source == null || target == null) {
            return;
        }
        BeanUtils.copyProperties(source, target);
    }

    public static <T, K> K copyProperties(T source, K target, Class<K> clazz, boolean isIgnoreNullProperties) {
        if (source == null) {
            return null;
        }
        if (target == null) {
            try {
                target = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error(e.getMessage());
            }
        }
        if (target != null) {
            if (isIgnoreNullProperties) {
                BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
            } else {
                BeanUtils.copyProperties(source, target);
            }
        }
        return target;
    }

    public static <T, K> K copyProperties(T source, K target, Class<K> clazz, String... ignoreProperties) {
        if (source == null) {
            return null;
        }
        if (target == null) {
            try {
                target = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error(e.getMessage());
            }
        }
        if (target != null) {
            BeanUtils.copyProperties(source, target, ignoreProperties);
        }
        return target;
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }


}
