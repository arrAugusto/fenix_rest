/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.serviceBack.fenix.models.ingresos;

import com.serviceBack.fenix.Utils.ResponseService;
import com.serviceBack.fenix.models.ingresos.Items;
import java.util.List;

/**
 *
 * @author agr12
 */
public class ItemsFail extends ResponseService {

    private List<Items> itemsFail;


    public List<Items> getItemsFail() {
        return itemsFail;
    }

    public void setItemsFail(List<Items> itemsFail) {
        this.itemsFail = itemsFail;
    }

}
