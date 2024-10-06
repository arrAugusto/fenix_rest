package com.serviceBack.fenix.services;

import com.serviceBack.fenix.Utils.GenericSQL;
import com.serviceBack.fenix.Utils.SecureUniqueCodeGenerator;
import com.serviceBack.fenix.interfaces.authTransactionInterface;
import com.serviceBack.fenix.models.AuthTransaction;
import commons.StoredProcedures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para manejar las transacciones de autenticación.
 */
@Service
public class AuthTransactionService implements authTransactionInterface {

    private final GenericSQL genericSQL;
    private final StoredProcedures stored;

    @Autowired
    public AuthTransactionService(GenericSQL genericSQL) {
        this.genericSQL = genericSQL;
        this.stored = new StoredProcedures(); // Inicializa la variable stored en el constructor
    }

    @Override
    public void authTransaction(AuthTransaction authTransaction) {
        SecureUniqueCodeGenerator security = new SecureUniqueCodeGenerator();

        authTransaction.setAuth_transaccion(security.generateSecureRandomSignature());
        authTransaction.setUsuario_firma("1");
        System.out.println(authTransaction.toString());
        
        //Creando objeto de inserccion
        Object[] params = {
            authTransaction.getIdTransaction(),
            authTransaction.getAuth_transaccion(),
            authTransaction.getUsuario_firma(),
            authTransaction.getUsuario_firma()
        };        
        
        boolean isInserted = genericSQL.insert(stored.STORE_PROCEDURE_CALL_INSERT_AUTH_TRANSACTION, params);
        if (isInserted) {
            System.out.println("¡Transacción de autenticación insertada con éxito!");
        } else {
            System.out.println("Falló la inserción de la transacción de autenticación.");
        }
    }
}
