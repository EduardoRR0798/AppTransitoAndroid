package modelo.dao;

import modelo.MyBatisUtils;
import modelo.pojos.Conductor;
import org.apache.ibatis.session.SqlSession;

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
    
    public static Conductor registrarConductor(Integer id) {
        SqlSession conn = null;
        Conductor c = null;
        try {
            conn = MyBatisUtils.getSession();
            c = (Conductor) conn.selectList("Conductor.findById", id).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return c;
    }
}
