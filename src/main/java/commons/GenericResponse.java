/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package commons;

import com.serviceBack.fenix.models.ErrorInfo;
import com.serviceBack.fenix.models.ingresos.ItemsFail;

/**
 *
 * @author agr12
 */
public class GenericResponse {

    public ItemsFail GenericResponsError(String key, String messageDefault) {
        ItemsFail itemsResponse = new ItemsFail();
        ErrorInfo errorInfo = JsonReader.findErrorInfo(key);
        itemsResponse.setCodeResponse(errorInfo.getCode());
        if (messageDefault.equals("DEFAULT")) {
            itemsResponse.setMessageResponse(errorInfo.getMessage());
        } else {
            itemsResponse.setMessageResponse(messageDefault);
        }
        return itemsResponse;

    }
}
