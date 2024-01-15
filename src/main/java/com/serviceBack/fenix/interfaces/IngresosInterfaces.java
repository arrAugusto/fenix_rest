package com.serviceBack.fenix.interfaces;

import com.serviceBack.fenix.controllers.Code_OTP;
import com.serviceBack.fenix.models.Ingresos;
import com.serviceBack.fenix.Util.ResponseService;

public interface IngresosInterfaces {

	public ResponseService createIngresos(Ingresos ingresos);
        
        public String createOTPIng(Code_OTP code_OTP);
}
