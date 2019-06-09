package modelo.dao;

import modelo.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import ws.Conductor;

/**
 *
 * @author Eduardo Rosas Rivera.
 */
public class ConductorDAO {
    public static int registrarConductor(Conductor conductor) {
        int insert = 0;
        SqlSession conn = null;
        try {
            conn = MyBatisUtils.getSession();
            insert = conn.insert("Conductor.registrarse", conductor);
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return insert;
    }
}
