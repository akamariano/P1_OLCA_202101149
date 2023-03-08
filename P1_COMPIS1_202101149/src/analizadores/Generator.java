/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadores;

/**
 *
 * @author Mariano Rack
 */
public class Generator {
    public static void main(String[] args){
        try{
        String ruta = "src/analizadores/";
        String opcFlex[] = {ruta+"Lexico","-d",ruta};
        jflex.Main.generate(opcFlex);
        String opcCUP[] = {"-destdir",ruta,"-parser","Analizador_sintactico",ruta+"Sintactico"};
        java_cup.Main.main(opcCUP);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
