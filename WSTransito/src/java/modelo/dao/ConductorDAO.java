package modelo.dao;

import java.util.List;
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
    
    public static boolean buscarPorTelefono(String telefono) {
        SqlSession conn = null;
        List<Conductor> conductores = null;
        try {
            conn = MyBatisUtils.getSession();
            conductores = conn.selectList("Conductor.findByTelefono", telefono);
            if (conductores.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public static Conductor login(Conductor c) {
        SqlSession conn = null;
        List<Conductor> conductores = null;
        try {
            conn = MyBatisUtils.getSession();
            conductores = conn.selectList("Conductor.login", c);
            if (conductores.isEmpty()) {
                return null;
            } else {
                return conductores.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean verificarToken(Conductor c) {
        SqlSession conn = null;
        List<Conductor> cs = null;
        try {
            conn = MyBatisUtils.getSession();
            cs = conn.selectList("Conductor.verificarToken", c);
            if (cs.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static int validarConductor(Conductor c) {
        SqlSession conn = null;
        int fa = 0;
        try {
            conn = MyBatisUtils.getSession();
            fa = conn.update("Conductor.validarConductor", c);
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fa;
    }
}
