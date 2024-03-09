package top.kongsheng.common.expression.convert;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * LocalDateTime转换器
 *
 * @author 孔胜
 * @version 版权 Copyright(c)2024 KONG SHENG
 * @date 2023/9/8 15:22
 */
public class LocalDateTimeConvert {

    public static LocalDateTime convert(Object obj) {
        if (obj instanceof LocalDateTime) {
            return ((LocalDateTime) obj);
        }
        if (obj instanceof Date) {
            return LocalDateTimeUtil.of(((Date) obj));
        }
        if (obj instanceof CharSequence) {
            CharSequence objStr = (CharSequence) obj;
            try {
                return LocalDateTimeUtil.parse(objStr);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.NORM_YEAR_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.NORM_MONTH_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.SIMPLE_MONTH_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.NORM_DATE_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.NORM_TIME_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.NORM_DATETIME_MINUTE_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.NORM_DATETIME_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.NORM_DATETIME_MS_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.ISO8601_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.CHINESE_DATE_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.CHINESE_DATE_TIME_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.PURE_DATE_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.PURE_TIME_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.PURE_DATETIME_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.PURE_DATETIME_MS_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.HTTP_DATETIME_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.JDK_DATETIME_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.UTC_SIMPLE_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.UTC_SIMPLE_MS_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.UTC_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.UTC_WITH_ZONE_OFFSET_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.UTC_WITH_XXX_OFFSET_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.UTC_MS_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.UTC_MS_WITH_ZONE_OFFSET_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
            try {
                return LocalDateTimeUtil.parse(objStr, DatePattern.UTC_MS_WITH_XXX_OFFSET_PATTERN);
            } catch (DateTimeParseException ignored) {
            }
        }
        return null;
    }
}
