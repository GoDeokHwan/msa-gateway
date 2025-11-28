package com.example.servicea.global.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

public class JsonHelper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    /** 일반 Object → JSON 문자열 */
    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 실패", e);
        }
    }

    /** Protobuf Message → JSON 문자열 */
    public static String toJson(Message message) {
        try {
            return JsonFormat.printer()
                    .preservingProtoFieldNames() // 필드 이름을 proto 스타일로 유지
                    .omittingInsignificantWhitespace() // 필요 시 공백 제거
                    .print(message);
        } catch (Exception e) {
            throw new RuntimeException("Protobuf JSON 변환 실패", e);
        }
    }

    public static <T> T fromJson(Message message, Class<T> clazz) {
        String data = toJson(message);
        return fromJson(data, clazz);
    }

    /** JSON 문자열 → 객체 */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }

    /** ObjectMapper 직접 접근 필요 시 */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }
}
