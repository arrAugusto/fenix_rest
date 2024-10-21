package com.serviceBack.fenix.interfaces;

import com.serviceBack.fenix.Utils.ResponseService;
import com.serviceBack.fenix.models.AuthTransaction;

public interface AuthTransactionInterfaceSing {
    public ResponseService authTransaction(AuthTransaction authTransaction);
}
