/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package commons;

import com.serviceBack.fenix.models.ErrorInfo;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import org.json.simple.parser.ParseException;

@Component
public class JsonReader {

    public static ErrorInfo findErrorInfo(String key) {
        try {
            // Cargar el archivo JSON desde el classpath
            Resource resource = new ClassPathResource("codes_and_messages.json");
            try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
                // Parsear el archivo JSON
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(reader);
                // Buscar el objeto correspondiente a la clave
                JSONObject currentObject = (JSONObject) jsonObject.get(key);
                if (currentObject != null) {
                    String code = (String) currentObject.get("code");
                    String message = (String) currentObject.get("message");
                    ErrorInfo errorInfo = new ErrorInfo();
                    errorInfo.setKey(key);
                    errorInfo.setCode(code);
                    errorInfo.setMessage(message);
                    return errorInfo;
                }

            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
