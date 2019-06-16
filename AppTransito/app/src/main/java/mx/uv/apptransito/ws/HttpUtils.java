package mx.uv.apptransito.ws;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import mx.uv.apptransito.beans.Vehiculo;

public class HttpUtils {

    private static final String URL_WS = "http://192.168.1.112:8084/WSTransito/ws/conductor/";
    private static final String URL_WSV = "http://192.168.1.112:8084/WSTransito/ws/vehiculos/";
    private static final Integer CONNECT_TIMEOUT = 4000; //MILISEGUNDOS
    private static final Integer READ_TIMEOUT = 10000; //MILISEGUNDOS

    public static String login(String telefono, String contrasenia) {
        HttpURLConnection c = null;
        String res = "";
        try {
            URL u = new URL(URL_WS+"login");
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("POST");
            c.setDoOutput(true);
            c.setConnectTimeout(CONNECT_TIMEOUT);
            c.setReadTimeout(READ_TIMEOUT);

            DataOutputStream wr = new DataOutputStream(c.getOutputStream());
            String urlParameters = String.format("telefono=%s&contrasenia=%s", telefono, contrasenia);
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int status = c.getResponseCode();
            System.out.println(c);
            if (status == 200 || status == 201) {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine())!= null) {
                    sb.append(line + "\n");
                }
                res = sb.toString();
                br.close();
                return sb.toString();
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
        return res;
    }

    public static String registrar(String nombre, String contrasenia, String telefono, String numLicencia, String fechaNacimiento) {
        HttpURLConnection c = null;
        String res = "";
        try {
            URL u = new URL(URL_WS+"registrar");
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("PUT");
            c.setDoOutput(true);
            c.setConnectTimeout(CONNECT_TIMEOUT);
            c.setReadTimeout(READ_TIMEOUT);

            DataOutputStream wr = new DataOutputStream(c.getOutputStream());
            String urlParameters = String.format("nombre=%s&telefono=%s&contrasenia=%s&fechaNacimiento=%s&numLicencia=%s", nombre, telefono, contrasenia, fechaNacimiento, numLicencia);
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int status = c.getResponseCode();
            if (status == 200 || status == 201) {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine())!= null) {
                    sb.append(line + "\n");
                }
                res = sb.toString();
                br.close();
                return sb.toString();
            }

        } catch (MalformedURLException ex) {

        } catch (IOException e) {

        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
        return res;
    }

    public static String validaregistro(String token, String telefono) {
        HttpURLConnection c = null;
        String res = "";
        try {
            URL u = new URL(URL_WS+"validarsms");
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("POST");
            c.setDoOutput(true);
            c.setConnectTimeout(CONNECT_TIMEOUT);
            c.setReadTimeout(READ_TIMEOUT);

            DataOutputStream wr = new DataOutputStream(c.getOutputStream());
            String urlParameters = String.format("token=%s&telefono=%s", token, telefono);
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int status = c.getResponseCode();
            if (status == 200 || status == 201) {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine())!= null) {
                    sb.append(line + "\n");
                }
                res = sb.toString();
                br.close();
                return sb.toString();
            }

        } catch (MalformedURLException ex) {

        } catch (IOException e) {

        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
        return res;
    }

    public static String registrarVehiculo(String marca, String modelo, String anio, String color, String nombreAseguradora, String numPoliza, String placa, String propietario, Integer idConductor) {
        HttpURLConnection c = null;
        String res = "";
        try {
            URL u = new URL(URL_WSV + "registrar");
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("PUT");
            c.setDoOutput(true);
            c.setConnectTimeout(CONNECT_TIMEOUT);
            c.setReadTimeout(READ_TIMEOUT);

            DataOutputStream wr = new DataOutputStream(c.getOutputStream());
            String urlParameters = String.format("marca=%s&modelo=%s&anio=%s&color=%s&nombreAseguradora=%s&numPoliza=%s&placa=%s&propietario=%s&idConductor=%s"
                    ,marca, modelo, anio, color, nombreAseguradora, numPoliza, placa, propietario, idConductor);
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int status = c.getResponseCode();
            if (status == 200 || status == 201) {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine())!= null) {
                    sb.append(line + "\n");
                }
                res = sb.toString();
                br.close();
                return sb.toString();
            }

        } catch (MalformedURLException ex) {

        } catch (IOException e) {

        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
        return res;
    }



    public static String eliminarVehiculo(Integer idVehiculo, Integer idConductor) {
        HttpURLConnection c = null;
        String res = "";
        try {
            URL u = new URL(URL_WSV + "eliminarrelacion");
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("DELETE");
            c.setDoOutput(true);
            c.setConnectTimeout(CONNECT_TIMEOUT);
            c.setReadTimeout(READ_TIMEOUT);

            DataOutputStream wr = new DataOutputStream(c.getOutputStream());
            String urlParameters = String.format("idConductor=%s&idVehiculo=%s", idConductor, idVehiculo);
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int status = c.getResponseCode();
            if (status == 200 || status == 201) {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine())!= null) {
                    sb.append(line + "\n");
                }
                res = sb.toString();
                br.close();
                return sb.toString();
            }

        } catch (MalformedURLException ex) {

        } catch (IOException e) {

        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
        return res;
    }

    public static String modificarVehiculo(String marca, String modelo, String anio, String color, String nombreAseguradora, String numPoliza, String placa, Integer idVehiculo) {
        HttpURLConnection c = null;
        String res = "";
        try {
            URL u = new URL(URL_WSV + "modificar");
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("POST");
            c.setDoOutput(true);
            c.setConnectTimeout(CONNECT_TIMEOUT);
            c.setReadTimeout(READ_TIMEOUT);

            DataOutputStream wr = new DataOutputStream(c.getOutputStream());
            String urlParameters = String.format("marca=%s&modelo=%s&anio=%s&color=%s&nombreAseguradora=%s&numPoliza=%s&placa=%s&idVehiculo=%s"
                    ,marca, modelo, anio, color, nombreAseguradora, numPoliza, placa, idVehiculo);
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int status = c.getResponseCode();
            if (status == 200 || status == 201) {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine())!= null) {
                    sb.append(line + "\n");
                }
                res = sb.toString();
                br.close();
                return sb.toString();
            }

        } catch (MalformedURLException ex) {

        } catch (IOException e) {

        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
        return res;
    }

    public static String modificarConductor(Integer idConductor, String contrasenia, String numLicencia) {
        HttpURLConnection c = null;
        String res = "";
        try {
            URL u = new URL(URL_WS + "modificar");
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("POST");
            c.setDoOutput(true);
            c.setConnectTimeout(CONNECT_TIMEOUT);
            c.setReadTimeout(READ_TIMEOUT);

            DataOutputStream wr = new DataOutputStream(c.getOutputStream());
            String urlParameters = String.format("idConductor=%s&contrasenia=%s&numLicencia=%s", idConductor, contrasenia, numLicencia);
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int status = c.getResponseCode();
            if (status == 200 || status == 201) {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine())!= null) {
                    sb.append(line + "\n");
                }
                res = sb.toString();
                br.close();
                return sb.toString();
            }

        } catch (MalformedURLException ex) {

        } catch (IOException e) {

        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
        return res;
    }

    public static String recuperarVehiculos(Integer idConductor) {
        HttpURLConnection c = null;
        String res = "";
        try {
            URL u = new URL(URL_WSV + "buscarporidconductor");
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("POST");
            c.setDoOutput(true);
            c.setConnectTimeout(CONNECT_TIMEOUT);
            c.setReadTimeout(READ_TIMEOUT);

            DataOutputStream wr = new DataOutputStream(c.getOutputStream());
            String urlParameters = String.format("idConductor=%s", idConductor);
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int status = c.getResponseCode();
            if (status == 200 || status == 201) {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine())!= null) {
                    sb.append(line + "\n");
                }
                res = sb.toString();
                br.close();
                return sb.toString();
            }

        } catch (MalformedURLException ex) {

        } catch (IOException e) {

        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
        return res;
    }
}
