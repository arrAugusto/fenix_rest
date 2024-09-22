/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sub_process.IncomeWithdrawal;

import com.serviceBack.fenix.models.ingresos.IncomeAndWithDrawal;
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

public PreparedStatement IncomeSQLPrepare(String query, IncomeAndWithDrawal ingreso) throws SQLException {
    PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection().prepareStatement(query);
    preparedStatement.setInt(1, ingreso.getCliente_id());
    preparedStatement.setInt(2, ingreso.getUsuario_id());
    preparedStatement.setInt(3, ingreso.getId_bodega());
    preparedStatement.setString(4, ingreso.getId_transaccion());
    preparedStatement.setString(5, isEmptyOrNull(ingreso.getReferencia_almacen()) ? null : ingreso.getReferencia_almacen());
    preparedStatement.setString(6, isEmptyOrNull(ingreso.getCanal_digital()) ? null : ingreso.getCanal_digital());
    preparedStatement.setString(7, isEmptyOrNull(ingreso.getAuth_transaction()) ? null : ingreso.getAuth_transaction());
    preparedStatement.setString(8, isEmptyOrNull(ingreso.getNumero_factura()) ? null : ingreso.getNumero_factura());
    preparedStatement.setString(9, isEmptyOrNull(ingreso.getEstado()) ? null : ingreso.getEstado());
    preparedStatement.setString(10, isEmptyOrNull(ingreso.getFecha()) ? null : ingreso.getFecha());
    preparedStatement.setString(11, isEmptyOrNull(ingreso.getFecha_operativa()) ? null : ingreso.getFecha_operativa());
    preparedStatement.setDouble(12, ingreso.getBultos());
    preparedStatement.setDouble(13, ingreso.getValor());
    preparedStatement.setString(14, isEmptyOrNull(ingreso.getBl()) ? null : ingreso.getBl());
    preparedStatement.setString(15, isEmptyOrNull(ingreso.getDocumento()) ? null : ingreso.getDocumento());
    preparedStatement.setString(16, isEmptyOrNull(ingreso.getCodigoQR()) ? null : ingreso.getCodigoQR());
    preparedStatement.setString(17, isEmptyOrNull(ingreso.getActa()) ? null : ingreso.getActa());
    preparedStatement.setString(18, isEmptyOrNull(ingreso.getArribo()) ? null : ingreso.getArribo());
    preparedStatement.setString(19, isEmptyOrNull(ingreso.getGenerica_1()) ? null : ingreso.getGenerica_1());
    preparedStatement.setString(20, isEmptyOrNull(ingreso.getGenerica_2()) ? null : ingreso.getGenerica_2());
    preparedStatement.setString(21, isEmptyOrNull(ingreso.getGenerica_3()) ? null : ingreso.getGenerica_3());
    preparedStatement.setString(22, isEmptyOrNull(ingreso.getGenerica_4()) ? null : ingreso.getGenerica_4());
    preparedStatement.setString(23, isEmptyOrNull(ingreso.getGenerica_5()) ? null : ingreso.getGenerica_5());
    preparedStatement.setString(24, isEmptyOrNull(ingreso.getGenerica_6()) ? null : ingreso.getGenerica_6());
    preparedStatement.setString(25, isEmptyOrNull(ingreso.getGenerica_7()) ? null : ingreso.getGenerica_7());
    preparedStatement.setString(26, isEmptyOrNull(ingreso.getGenerica_8()) ? null : ingreso.getGenerica_8());
    preparedStatement.setString(27, isEmptyOrNull(ingreso.getCodigo_transaccion()) ? null : ingreso.getCodigo_transaccion());
    preparedStatement.setString(28, isEmptyOrNull(ingreso.getIdImages()) ? null : ingreso.getIdImages());
    preparedStatement.setString(29, isEmptyOrNull(ingreso.getArea()) ? null : ingreso.getArea());
    preparedStatement.setString(30, isEmptyOrNull(ingreso.getDocumento_topay()) ? null : ingreso.getDocumento_topay());
    preparedStatement.setString(31, isEmptyOrNull(ingreso.getDocumento()) ? null : ingreso.getDocumento());
    preparedStatement.setString(32, isEmptyOrNull(ingreso.getNombre()) ? null : ingreso.getNombre());
    preparedStatement.setString(33, isEmptyOrNull(ingreso.getBoleta_de_pago()) ? null : ingreso.getBoleta_de_pago());
    preparedStatement.setString(34, isEmptyOrNull(ingreso.getComments()) ? null : ingreso.getComments());
    preparedStatement.setString(35, isEmptyOrNull(ingreso.getId_transaccion_foreing()) ? null : ingreso.getId_transaccion_foreing());
   
    return preparedStatement;
}

    private boolean isEmptyOrNull(String str) {
        return str == null || str.isEmpty();
    }
}
