package modelo.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.MyBatisUtils;
import modelo.pojos.Vehiculo;
import modelo.pojos.VehiculoConductor;
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
        List<Vehiculo> vs = null;
        try {
            conn = MyBatisUtils.getSession();
            vs = conn.selectList("Vehiculo.findByPlaca", placa);
            if (vs.isEmpty()) {
                return vehiculo;
            } else {
                return vs.get(0);
            }
        } catch (Exception e) {
            vehiculo = null;
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
    
    public static List<Vehiculo> findByIdConductor(Integer idConductor) {
        SqlSession conn = null;
        List<Integer> idVehiculos = null;
        List<Vehiculo> vehiculos = new ArrayList<>();
        Vehiculo v = null;
        try {
            conn = MyBatisUtils.getSession();
            idVehiculos = conn.selectList("Vehiculo.findByIdConductor", idConductor);
            
            for (int i = 0; i < idVehiculos.size(); i++) {
                v = conn.selectOne("Vehiculo.findByIdVehiculo", idVehiculos.get(i));
                vehiculos.add(v);
            }
        } catch (IOException ex) {
            Logger.getLogger(VehiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vehiculos;
    }
    
    public static int agregarExistente(VehiculoConductor vc) {
        SqlSession conn = null;
        int fa = 0;
        try {
            conn = MyBatisUtils.getSession();
            fa = conn.insert("Vehiculo.agregarExistente", vc);
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fa;
    }
}