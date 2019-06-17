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
import modelo.MyBatisUtils;
import modelo.pojos.Foto;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Razer
 */
public class FotoDAO {
    
    public static Integer guardar(Integer idReporte, byte[] fotoBytes) {
        Integer idGenerado = null;
        try (SqlSession conn = MyBatisUtils.getSession()) {
            HashMap<String,Object> map = new HashMap<>();
            map.put("idReporte", idReporte);
            map.put("foto", fotoBytes);
            
            conn.insert("foto.guardar", map);
            conn.commit();
            
            idGenerado = ((BigDecimal) map.get("idFoto")).intValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return idGenerado;
    }
    
    public static List<Foto> fotosDeReporte(Integer idReporte) {
        List<Foto> fotos = null;
        try (SqlSession conn = MyBatisUtils.getSession()) {
            fotos = conn.selectList("foto.findByIdReporte", idReporte);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fotos;
    }
    
    public static List<Foto> fotosDeIncidente(Integer idIncidente) {
        List<Foto> fotos = null;
        try (SqlSession conn = MyBatisUtils.getSession()) {
            fotos = conn.selectList("foto.findByIdIncidente", idIncidente);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fotos;
    }
    
}
