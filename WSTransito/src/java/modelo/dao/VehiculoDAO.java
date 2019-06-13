package modelo.dao;

import java.util.HashMap;
import java.util.List;
import modelo.MyBatisUtils;
import modelo.pojos.Vehiculo;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Eduardo Rosas Rivera.
 */
public class VehiculoDAO {
    public static int registrar(Vehiculo vehiculo) {
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
    
    public static Vehiculo findByPlaca(String placa) {
        SqlSession conn = null;
        Vehiculo vehiculo = null;
        try {
            conn = MyBatisUtils.getSession();
            vehiculo = conn.selectOne("Vehiculo.findByPlaca", placa);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return vehiculo;
    }
    
    public static int eliminarRelacion(Integer idVehiculoConductor) {
        SqlSession conn = null;
        int eliminacion = 0;
        try {
            conn = MyBatisUtils.getSession();
            eliminacion = conn.delete("Vehiculo.eliminarRelacion", idVehiculoConductor);
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return eliminacion;
    }
    
    public static int agregarALista(Integer idConductor, Integer idVehiculo, String propietario) {
        SqlSession conn = null;
        int fa = 0;
        try {
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("idConductor", idConductor);
            param.put("idVehiculo", idVehiculo);
            param.put("propietario", propietario);
            conn = MyBatisUtils.getSession();
            fa = conn.insert("Vehiculo.agregarALista", param);
            conn.commit();
        } catch (Exception e) {
            
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return fa;
    }
    
    //public static List<>
}