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
import modelo.pojos.Incidente;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Razer
 */
public class IncidenteDAO {
    
    public static List<Incidente> incidentesCerca(Float latitud, Float longitud) {
        List<Incidente> incidentes = null;
        try (SqlSession conn = MyBatisUtils.getSession()) {
            Map<String,Object> map = new HashMap<>();
            map.put("latitud", latitud);
            map.put("longitud", longitud);
            incidentes = conn.selectList("incidente.cerca", map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return incidentes;
    }
    
    public static Integer registrar(Float latitud, Float longitud) {
        Integer idIncidente = null;
        try (SqlSession conn = MyBatisUtils.getSession()) {
            Map<String,Object> map = new HashMap<>();
            map.put("latitud", latitud);
            map.put("longitud", longitud);
            map.put("estado", Incidente.ESTADO_PENDIENTE);
            conn.insert("incidente.registrar", map);
            conn.commit();
            idIncidente = ((BigDecimal) map.get("idIncidente")).intValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return idIncidente;
    }
    
    public static boolean existeIncidente(Integer idIncidente) {
        boolean existe = false;
        try (SqlSession conn = MyBatisUtils.getSession()) {
            existe = conn.selectOne("incidente.existe", idIncidente);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return existe;
    }
    
}
