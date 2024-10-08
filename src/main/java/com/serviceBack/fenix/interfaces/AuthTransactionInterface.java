package com.serviceBack.fenix.interfaces;

import com.serviceBack.fenix.Utils.ResponseService;
import com.serviceBack.fenix.models.AuthTransaction;

public interface AuthTransactionInterface {

    public ResponseService authTransaction(AuthTransaction authTransaction);
    
}
