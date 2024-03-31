/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sub_process.IncomeWithdrawal;

import com.serviceBack.fenix.models.DetallesIngreso;
import com.serviceBack.fenix.models.Ingresos;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author agr12
 */
public class PrepareIncomeStatment {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PrepareIncomeStatment(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PreparedStatement IncomeSQLPrepare(String query, Ingresos ingreso) throws SQLException {
        PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection().prepareStatement(query);
        preparedStatement.setString(1, isEmptyOrNull(ingreso.getIdTransaccion()) ? null : ingreso.getIdTransaccion());
        preparedStatement.setString(2, isEmptyOrNull(ingreso.getUsuario()) ? null : ingreso.getUsuario());
        preparedStatement.setString(3, isEmptyOrNull(ingreso.getIdNit()) ? null : ingreso.getIdNit());
        preparedStatement.setString(4, isEmptyOrNull(ingreso.getIdImages()) ? null : ingreso.getIdImages());
        preparedStatement.setString(5, isEmptyOrNull(ingreso.getCanalDigital()) ? null : ingreso.getCanalDigital());
        preparedStatement.setString(6, isEmptyOrNull(ingreso.getFechaGarita()) ? null : ingreso.getFechaGarita());
        preparedStatement.setString(7, isEmptyOrNull(ingreso.getFechaBodega()) ? null : ingreso.getFechaBodega());
        preparedStatement.setString(8, isEmptyOrNull(ingreso.getFechaOperativa()) ? null : ingreso.getFechaOperativa());
        preparedStatement.setString(9, isEmptyOrNull(ingreso.getCodigo_transaccion()) ? null : ingreso.getCodigo_transaccion());
        preparedStatement.setString(10, isEmptyOrNull(ingreso.getDocumento()) ? null : ingreso.getDocumento());
        preparedStatement.setString(11, isEmptyOrNull(ingreso.getCodigoQR()) ? null : ingreso.getCodigoQR());
        preparedStatement.setInt(12, ingreso.getBultos());
        preparedStatement.setDouble(13, ingreso.getMontoTotal());
        preparedStatement.setString(14, isEmptyOrNull(ingreso.getArea()) ? null : ingreso.getArea());
        preparedStatement.setString(15, "1321644131313");
        preparedStatement.setString(16, "13216632322");
        preparedStatement.setString(17, isEmptyOrNull(ingreso.getDocumento_top_pay()) ? null : ingreso.getDocumento_top_pay());
        preparedStatement.setString(18, isEmptyOrNull(ingreso.getDocument()) ? null : ingreso.getDocument());
        preparedStatement.setString(19, isEmptyOrNull(ingreso.getNombre()) ? null : ingreso.getNombre());
        preparedStatement.setString(20, isEmptyOrNull(ingreso.getBoleta_de_pago()) ? null : ingreso.getBoleta_de_pago());
        preparedStatement.setString(21, isEmptyOrNull(ingreso.getComments()) ? null : ingreso.getComments());
        preparedStatement.setString(22, isEmptyOrNull(ingreso.getAuth_transaction()) ? null : ingreso.getAuth_transaction());
        return preparedStatement;

    }
    public void ItemsSQLPrepare(String query, DetallesIngreso detalles){
        
    }
            
            
    private boolean isEmptyOrNull(String str) {
        return str == null || str.isEmpty();
    }
}
