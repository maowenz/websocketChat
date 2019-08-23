package com.mwz.wesocket.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


import static com.google.common.base.Preconditions.checkNotNull;

public class JSONUtils {

    private static final Logger logger = LoggerFactory.getLogger(JSONUtils.class);

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setTimeZone(TimeZone.getDefault());

    private static Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static <T> T readValue(String jsonContent, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonContent, clazz);
        } catch (IOException e) {
            logger.error("Fail to convert json[{}] to bean[{}]", jsonContent, clazz, e);
            throw new IllegalStateException("Fail to parse json str");
        }
    }

    public static <T> T readValueForJsonObject(String jsonContent, Class<T> clazz) {
        try {
            return gson.fromJson(jsonContent, clazz);
        } catch (JsonSyntaxException e) {
            logger.error("Fail to convert json[{}] to bean[{}]", jsonContent, clazz, e);
            throw new IllegalStateException("Fail to parse json str");
        }
    }

    public static <T> T readValue(String jsonContent, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(jsonContent, typeReference);
        } catch (IOException e) {
            logger.error("Fail to convert json[{}] to bean[{}]", jsonContent, typeReference, e);
            throw new IllegalStateException("Fail to parse json str");
        }
    }

    public static <T> T readValue(File file, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(file, typeReference);
        } catch (IOException e) {
            logger.error("Fail to convert json[{}] to bean[{}]", file.getName(), typeReference, e);
            throw new IllegalStateException("Fail to parse json str");
        }
    }

    public static <T> T readValueFromRequest(String jsonContent, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(jsonContent, typeReference);
        } catch (IOException e) {
            logger.error("Fail to convert json[{}] to bean[{}]", jsonContent, typeReference, e);
            throw new IllegalArgumentException("Fail to parse json str");
        }
    }

    public static String writeValue(Object obj) {
        if (obj == null) {
            return StringUtils.EMPTY;
        }

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("failed to process json obj", e);
            return StringUtils.EMPTY;
        }
    }

    /**
     * 写入JSON对象到文件
     * @param resultFile 目标文件
     * @param value JSON对象
     */
    public static void writeValueToFile(File resultFile, Object value) {
        if (value == null) {
            return;
        }

        try {
            objectMapper.writeValue(resultFile, value);
        } catch (IOException e) {
            logger.error("failed to process json obj", e);
        }
    }

    public static byte[] writeValueAsBytes(Object obj) {
        if (obj == null) {
            return new byte[0];
        }

        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            logger.error("failed to process json obj", e);
            return new byte[0];
        }
    }

    /**
     * 以特定视图的Object Writer输出Json
     * @param c
     * @param obj
     * @return
     */
    public static String writeValueWithClass(Class c, Object obj) {
        if (obj == null) {
            return StringUtils.EMPTY;
        }

        try {
            return objectMapper.writerWithView(c).writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("failed to process json obj", e);
            return StringUtils.EMPTY;
        }
    }

    public static String writePrettyValue(Object obj) {
        if (obj == null) {
            return StringUtils.EMPTY;
        }

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse object to json str", e);
            return StringUtils.EMPTY;
        }
    }

    public static String toString(Object obj) {
        checkNotNull(obj, "Require non-null param 'obj'");

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse object to json str", e);
            return StringUtils.EMPTY;
        }
    }

    public static String toStringForJsonObject(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (JsonSyntaxException e) {
            logger.error("Failed to parse object to json str", e);
            return StringUtils.EMPTY;
        }
    }

    public static Map<String, String> readStringMap(String content) {
        return gson.fromJson(content, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    public static Map<Object, Object> readObjectMap(String content) {
        return gson.fromJson(content, Map.class);
    }

    /**
     * json字符串转换为指定类型
     * @param content
     * @return
     */
    public static <T> T readValue(String content, TypeToken<T> token) {
        return gson.fromJson(content, token.getType());
    }

    /**
     * map转换为对象。
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T convertMap2Obj(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

    /**
     * json字符串转换为对象集合。
     * @param jsonArrayStr
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> convertArrayStr2ObjList(String jsonArrayStr, Class<T> clazz) throws IOException {
        List<Map<String, Object>> list = objectMapper.readValue(jsonArrayStr, new TypeReference<List<T>>() {
        });
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : list) {
            result.add(convertMap2Obj(map, clazz));
        }
        return result;
    }

    /**
     * 对象转换为json字符串。
     * @param obj
     * @return 对象的json字符串，对象为空或转换失败时返回null
     */
    public static String toJsonStr(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return gson.toJson(obj);
        } catch (JsonSyntaxException e) {
            logger.error("Failed to convert object to json str", e);
            return null;
        }
    }
}
