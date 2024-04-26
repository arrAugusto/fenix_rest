/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sub_process.IncomeWithdrawal;

import com.serviceBack.fenix.models.ingresos.Ingresos;
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
    preparedStatement.setInt(1, ingreso.getP_cliente_id());
    preparedStatement.setInt(2, ingreso.getP_usuario_id());
    preparedStatement.setInt(3, ingreso.getP_id_bodega());
    preparedStatement.setInt(4, ingreso.getP_id_transaccion());
    preparedStatement.setString(5, isEmptyOrNull(ingreso.getP_referencia_almacen()) ? null : ingreso.getP_referencia_almacen());
    preparedStatement.setString(6, isEmptyOrNull(ingreso.getP_canal_digital()) ? null : ingreso.getP_canal_digital());
    preparedStatement.setString(7, isEmptyOrNull(ingreso.getP_auth_transaction()) ? null : ingreso.getP_auth_transaction());
    preparedStatement.setString(8, isEmptyOrNull(ingreso.getP_numero_factura()) ? null : ingreso.getP_numero_factura());
    preparedStatement.setString(9, isEmptyOrNull(ingreso.getP_estado()) ? null : ingreso.getP_estado());
    preparedStatement.setString(10, isEmptyOrNull(ingreso.getP_fecha()) ? null : ingreso.getP_fecha());
    preparedStatement.setString(11, isEmptyOrNull(ingreso.getP_fecha_operativa()) ? null : ingreso.getP_fecha_operativa());
    preparedStatement.setDouble(12, ingreso.getP_bultos());
    preparedStatement.setDouble(13, ingreso.getP_valor());
    preparedStatement.setString(14, isEmptyOrNull(ingreso.getP_bl()) ? null : ingreso.getP_bl());
    preparedStatement.setString(15, isEmptyOrNull(ingreso.getP_documento()) ? null : ingreso.getP_documento());
    preparedStatement.setString(16, isEmptyOrNull(ingreso.getP_codigoQR()) ? null : ingreso.getP_codigoQR());
    preparedStatement.setString(17, isEmptyOrNull(ingreso.getP_acta()) ? null : ingreso.getP_acta());
    preparedStatement.setString(18, isEmptyOrNull(ingreso.getP_arribo()) ? null : ingreso.getP_arribo());
    preparedStatement.setString(19, isEmptyOrNull(ingreso.getP_generica_1()) ? null : ingreso.getP_generica_1());
    preparedStatement.setString(20, isEmptyOrNull(ingreso.getP_generica_2()) ? null : ingreso.getP_generica_2());
    preparedStatement.setString(21, isEmptyOrNull(ingreso.getP_generica_3()) ? null : ingreso.getP_generica_3());
    preparedStatement.setString(22, isEmptyOrNull(ingreso.getP_generica_4()) ? null : ingreso.getP_generica_4());
    preparedStatement.setString(23, isEmptyOrNull(ingreso.getP_generica_5()) ? null : ingreso.getP_generica_5());
    preparedStatement.setString(24, isEmptyOrNull(ingreso.getP_generica_6()) ? null : ingreso.getP_generica_6());
    preparedStatement.setString(25, isEmptyOrNull(ingreso.getP_generica_7()) ? null : ingreso.getP_generica_7());
    preparedStatement.setString(26, isEmptyOrNull(ingreso.getP_generica_8()) ? null : ingreso.getP_generica_8());
    preparedStatement.setString(27, isEmptyOrNull(ingreso.getP_codigo_respuesta()) ? null : ingreso.getP_codigo_respuesta());
    preparedStatement.setString(28, isEmptyOrNull(ingreso.getP_mensaje_respuesta()) ? null : ingreso.getP_mensaje_respuesta());
    preparedStatement.setString(29, isEmptyOrNull(ingreso.getP_codigo_transaccion()) ? null : ingreso.getP_codigo_transaccion());
    preparedStatement.setString(30, isEmptyOrNull(ingreso.getP_idImages()) ? null : ingreso.getP_idImages());
    preparedStatement.setString(31, isEmptyOrNull(ingreso.getP_area()) ? null : ingreso.getP_area());
    preparedStatement.setString(32, isEmptyOrNull(ingreso.getP_documento_top_pay()) ? null : ingreso.getP_documento_top_pay());
    preparedStatement.setString(33, isEmptyOrNull(ingreso.getP_p_documento()) ? null : ingreso.getP_p_documento());
    preparedStatement.setString(34, isEmptyOrNull(ingreso.getP_nombre()) ? null : ingreso.getP_nombre());
    preparedStatement.setString(35, isEmptyOrNull(ingreso.getP_boleta_de_pago()) ? null : ingreso.getP_boleta_de_pago());
    preparedStatement.setString(36, isEmptyOrNull(ingreso.getP_comments()) ? null : ingreso.getP_comments());
    return preparedStatement;
}

    private boolean isEmptyOrNull(String str) {
        return str == null || str.isEmpty();
    }
}
