package org.example.platzi;

import javax.swing.*;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        int opcion_menu = -1;
        String[] botones = {"1. Ver Gatos","2. Ver Favoritos","3.Salir"};

        do {
            //Menu Principal
            String opcion = (String) JOptionPane.showInputDialog(null, "Gatitos Java","Menu Principal", JOptionPane.INFORMATION_MESSAGE,
                    null, botones, botones[0]);

            //Validamos que opción selecciona el usuario
            for (int i = 0; i < botones.length; i++) {
                if (opcion.equals(botones[i])){
                    opcion_menu = i;
                }
            }

            switch (opcion_menu){
                case 0:
                    GatosService.verGatos();
                    break;
                case 1:
                    Gatos gato = new Gatos();
                    GatosService.verFavorito(gato.getApikey());
                    break;
                default:
                    break;
            }
        }while (opcion_menu != 1);
    }
}