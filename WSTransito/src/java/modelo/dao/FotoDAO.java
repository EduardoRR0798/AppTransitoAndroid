/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dao;

import java.io.IOException;
import java.util.HashMap;
import modelo.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Razer
 */
public class FotoDAO {
    
    public static boolean guardar(Integer idReporte, byte[] fotoBytes) {
        int filasAfectadas = 0;
        
        try (SqlSession conn = MyBatisUtils.getSession()) {
            HashMap<String,Object> map = new HashMap<>();
            map.put("idReporte", idReporte);
            map.put("foto", fotoBytes);
            
            filasAfectadas = conn.insert("foto.guardar", map);
            conn.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return filasAfectadas > 0;
    }
    
}
