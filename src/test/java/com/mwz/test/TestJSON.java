package com.mwz.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mwz.wesocket.util.JSONUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author wzm
 * @date 2019年08月23日 16:12
 */
public class TestJSON {


    public static void main(String[] args) {
        directSetUserVirtualIdMapping();
    }

    private static void directSetUserVirtualIdMapping() {
        String fileName = "/user_id-virtual_id-map.json";
        String jsonContent = null;
        try (InputStream stream = TestJSON.class.getResourceAsStream(fileName)) {
            int hasRead;
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream(stream.available());
            while ((hasRead = stream.read(buffer)) != -1) {
                baos.write(buffer, 0, hasRead);
            }
            jsonContent = baos.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            System.out.println("获取文件流失败");
        }

        Map<String, UserVO> map = JSONUtils.readValue(jsonContent, new TypeReference<Map<String, UserVO>>() {});
        map.forEach((virtualId, userInfo) -> System.out.println(virtualId+"-->"+userInfo.toString()));
    }

}
