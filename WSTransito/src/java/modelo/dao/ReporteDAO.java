/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dao;

import java.io.IOException;
import modelo.MyBatisUtils;
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
}
