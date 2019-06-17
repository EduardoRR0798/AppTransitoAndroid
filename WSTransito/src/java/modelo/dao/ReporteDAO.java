/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.MyBatisUtils;
import modelo.pojos.Reporte;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Razer
 */
public class ReporteDAO {
    
    public static boolean existeReporte(Integer idReporte) {
        boolean existe = false;
        try (SqlSession conn = MyBatisUtils.getSession()) {
            existe = conn.selectOne("reporte.existe", idReporte);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return existe;
    }
    
    public static List<Reporte> historial(Integer idConductor) {
        List<Reporte> reportes = null;
        try (SqlSession conn = MyBatisUtils.getSession()) {
            reportes = conn.selectList("reporte.findByIdConductor", idConductor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reportes;
    }
    
    public static Integer registrar(Float latitud, Float longitud, String nombreConductor, String nombreAseguradora, String numPoliza,
        String marca, String modelo, String color, String placa, Integer idConductor, Integer idVehiculo, Integer idIncidente) {
        Integer idGenerado = null;
        try (SqlSession conn = MyBatisUtils.getSession()) {
            Map<String,Object> map = new HashMap<>();
            map.put("latitud", latitud);
            map.put("longitud", longitud);
            map.put("nombreConductor", nombreConductor);
            map.put("nombreAseguradora", nombreAseguradora);
            map.put("numPoliza", numPoliza);
            map.put("marca", marca);
            map.put("modelo", modelo);
            map.put("color", color);
            map.put("placa", placa);
            map.put("idConductor", idConductor);
            map.put("idVehiculo", idVehiculo);
            map.put("idIncidente", idIncidente);
            
            conn.insert("reporte.registrar", map);
            conn.commit();
            
            idGenerado = ((BigDecimal) map.get("idReporte")).intValue();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return idGenerado;
    }
    
    
}
