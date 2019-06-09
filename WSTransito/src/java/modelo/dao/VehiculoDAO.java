package modelo.dao;

import modelo.MyBatisUtils;
import modelo.pojos.Vehiculo;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Eduardo Rosas Rivera.
 */
public class VehiculoDAO {
    static int registrar(Vehiculo vehiculo) {
        SqlSession conn = null;
        int registro = 0;
        try {
            conn = MyBatisUtils.getSession();
            registro = conn.insert("Vehiculo.registrar", vehiculo);
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return registro;
    }
}
