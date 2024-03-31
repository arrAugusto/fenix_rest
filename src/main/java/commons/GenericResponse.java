/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package commons;

import com.serviceBack.fenix.models.ErrorInfo;
import com.serviceBack.fenix.models.ItemsFail;

/**
 *
 * @author agr12
 */
public class GenericResponse {

    public ItemsFail GenericResponsError(String key) {
        ItemsFail itemsResponse = new ItemsFail();
        ErrorInfo errorInfo = JsonReader.findErrorInfo(key);
        itemsResponse.setCodeResponse(errorInfo.getCode());
        itemsResponse.setMessageResponse(errorInfo.getMessage());
        return itemsResponse;

    }
}
