package mx.uv.apptransito.ws;

import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import mx.uv.apptransito.ws.Respuesta;

public class HttpClient {

    private static final Integer CONNECT_TIMEOUT = 4000; //MILISEGUNDOS
    private static final Integer READ_TIMEOUT = 10000; //MILISEGUNDOS
    private static final String POST_METHOD = "POST";
    private static final String PUT_METHOD = "PUT";
    private static final String DELETE_METHOD = "DELETE";

    public static Respuesta getRequest(String resource) {
        Respuesta httpResponse = new Respuesta();

        HttpURLConnection conn = null;
        try {
            URL url = new URL(resource);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-length", "0");
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);

            conn.connect();

            int responseCode = conn.getResponseCode();
            httpResponse.setStatus(responseCode);
            if (responseCode != 200 && responseCode != 201) {
                httpResponse.setError(true);
            }

            if (conn.getInputStream() != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                httpResponse.setResult(sb.toString());
            }

        } catch (IOException e) {
            httpResponse.setError(true);
            httpResponse.setResult(e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return httpResponse;

    }

    public static Respuesta postRequest(String resource, Map<String, Object> postData) {
        return request(POST_METHOD, resource, postData);
    }

    public static Respuesta putRequest(String resource, Map<String, Object> postData) {
        return request(PUT_METHOD, resource, postData);
    }

    public static Respuesta deleteRequest(String resource, Map<String, Object> postData) {
        return request(DELETE_METHOD, resource, postData);
    }

    private static Respuesta request(String method, String resource, Map<String, Object> postData) {
        Respuesta httpResponse = new Respuesta();

        HttpURLConnection conn = null;
        try {
            URL url = new URL(resource);

            StringBuilder data = new StringBuilder();
            for (Map.Entry<String, Object> param : postData.entrySet()) {
                if (data.length() != 0) data.append('&');
                data.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                data.append('=');
                data.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = data.toString().getBytes(StandardCharsets.UTF_8);

            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            conn.connect();

            int responseCode = conn.getResponseCode();
            httpResponse.setStatus(responseCode);
            if (responseCode != 200 && responseCode != 201) {
                httpResponse.setError(true);
            }

            if (conn.getInputStream() != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                httpResponse.setResult(sb.toString());
            }

        } catch (IOException e) {
            httpResponse.setError(true);
            httpResponse.setResult(e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return httpResponse;
    }

    public static Respuesta subirFoto(String resource, Bitmap bitmap) {
        Respuesta res = new Respuesta();
        HttpURLConnection c = null;
        DataOutputStream outputStream = null;

        try {
            URL url = new URL(resource);
            c = (HttpURLConnection) url.openConnection();
            c.setDoInput(true);
            c.setDoOutput(true);
            c.setUseCaches(false);

            c.setRequestMethod("POST");
            c.setRequestProperty("Connection", "Keep-Alive");
            c.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            c.setRequestProperty("Content-Type", "application/octet-stream");

            outputStream = new DataOutputStream(c.getOutputStream());

            ByteArrayOutputStream bitmapOutputStream = new ByteArrayOutputStream();
            bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    80,
                    bitmapOutputStream
            );

            byte original[] = bitmapOutputStream.toByteArray();
            int blockbytes, totalbytes, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            int lastbyte = 0;
            totalbytes = original.length;
            bufferSize = Math.min(totalbytes, maxBufferSize);
            buffer = Arrays.copyOfRange(original, lastbyte, bufferSize);
            blockbytes = buffer.length;
            while (totalbytes > 0) {
                outputStream.write(buffer, 0, bufferSize);
                totalbytes = totalbytes - blockbytes;
                lastbyte += blockbytes;
                bufferSize = Math.min(totalbytes, maxBufferSize);
                buffer = Arrays.copyOfRange(original, lastbyte, lastbyte + bufferSize);
                blockbytes = buffer.length;
            }
            bitmapOutputStream.close();
            outputStream.flush();
            outputStream.close();


            res.setStatus(c.getResponseCode());
            if (res.getStatus() != 200 && res.getStatus() != 201) {
                res.setError(Boolean.TRUE);
            }

            if (c.getInputStream() != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                res.setResult(sb.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
            res.setError(Boolean.TRUE);
            res.setResult(e.getMessage());
        } finally {
            if (c != null) {
                c.disconnect();
            }
        }

        return res;
    }
}
