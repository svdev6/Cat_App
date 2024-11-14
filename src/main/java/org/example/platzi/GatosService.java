package org.example.platzi;

import com.google.gson.Gson;
import com.squareup.okhttp.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class GatosService {

    public static void verGatos() throws IOException {
        //1. Vamos a traer los datos de la API
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/images/search")
                .get()
                .build();
        Response response = client.newCall(request).execute();

        String elJson = response.body().string();

        //Cortar los corchetes
        elJson = elJson.substring(1, elJson.length());
        elJson = elJson.substring(0, elJson.length()-1);

        //Crear un objeto de la clase Gson
        Gson gson = new Gson();
        Gatos gatos = gson.fromJson(elJson, Gatos.class);

        //Redimensionar en caso de Necesitar
        Image image = null;
        try {
            URL url = new URL(gatos.getUrl());
            image = ImageIO.read(url);

            ImageIcon fondoGato = new ImageIcon(image);

            if (fondoGato.getIconWidth() > 800){
                //Redimensionamos
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(800,600, java.awt.Image.SCALE_SMOOTH);
                fondoGato = new ImageIcon(modificada);
            }
            String menu = "Opciones: \n"
                    + "1. Ver Otra Imagen\n"
                    + "2. Favoritos\n"
                    + "3. Volver al Menu\n";

            String[] botones = {"Ver Otra Imagen","Favoritos","Volver al Menu"};
            String id_gato = gatos.getId();
            String opcion = (String) JOptionPane.showInputDialog(null,menu,id_gato, JOptionPane.INFORMATION_MESSAGE, fondoGato, botones, botones[0]);

            int selection = -1;
            //Validamos que opción selecciona el usuario
            for (int i = 0; i < botones.length; i++) {
                if (opcion.equals(botones[i])){
                    selection = i;
                }
            }

            switch (selection){
                case 0:
                    verGatos();
                    break;
                case 1:
                    favoritoGato(gatos);
                    break;
                default:
                    break;

            }

        }catch (IOException e){
            System.out.println(e);
        }
    }

    public static void favoritoGato(Gatos gato){

        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"image_id\": \""+gato.getId()+"\"\r\n  }");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", gato.getApikey())
                    .build();
            Response response = client.newCall(request).execute();

        }catch (IOException e){
            System.out.println(e);
        }
    }

    public static void verFavorito(String apikey) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites")
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", apikey)
                .build();
        Response response = client.newCall(request).execute();

        //Guardamos el string con la respuesta
        String elJson = response.body().string();

        //Creamos el Objeto Gson
        Gson gson = new Gson();

        GatosFav[] gatosArray = gson.fromJson(elJson, GatosFav[].class);

        if (gatosArray.length > 0){
            int min = 1;
            int max = gatosArray.length;
            int aleatorio = (int) (Math.random() * ((max - min)) + 1) + min;
            int indice = aleatorio - 1;

            GatosFav gatofav = gatosArray[indice];

            //Redimensionar en caso de Necesitar
            Image image = null;
            try {
                URL url = new URL(gatofav.image.getUrl());
                image = ImageIO.read(url);

                ImageIcon fondoGato = new ImageIcon(image);

                if (fondoGato.getIconWidth() > 800){
                    //Redimencionamos
                    Image fondo = fondoGato.getImage();
                    Image modificada = fondo.getScaledInstance(800,600, java.awt.Image.SCALE_SMOOTH);
                    fondoGato = new ImageIcon(modificada);
                }
                String menu = "Opciones: \n"
                        + "1. Ver Otra Imagen\n"
                        + "2. Eliminar Favoritos\n"
                        + "3. Volver al Menu\n";

                String[] botones = {"Ver Otra Imagen","Eliminar Favoritos","Volver al Menu"};
                String id_gato = gatofav.getId();
                String opcion = (String) JOptionPane.showInputDialog(null,menu,id_gato, JOptionPane.INFORMATION_MESSAGE, fondoGato, botones, botones[0]);

                int selection = -1;
                //Validamos que opción selecciona el usuario
                for (int i = 0; i < botones.length; i++) {
                    if (opcion.equals(botones[i])){
                        selection = i;
                    }
                }

                switch (selection){
                    case 0:
                        verFavorito(apikey);
                        break;
                    case 1:
                        borrarFavorito(gatofav);
                        break;
                    default:
                        break;

                }

            }catch (IOException e){
                System.out.println(e);
            }
        }
    }

    public static void borrarFavorito(GatosFav gatofav){

        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites/"+gatofav.getId()+"")
                    .method("DELETE", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", gatofav.getApikey())
                    .build();
            Response response = client.newCall(request).execute();
        }catch (IOException e){
            System.out.println(e);
        }
    }
}