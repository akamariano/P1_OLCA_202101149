/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadores;
import java.util.ArrayList;
/**
 *
 * @author Mariano Rack
 */
public class Conjunto {
    private String nombre;
    private ArrayList<Integer> caracteres;

    public Conjunto(String nombre, ArrayList<Integer> caracteres) {
        this.nombre = nombre;
        this.caracteres = caracteres;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Integer> getCaracteres() {
        return caracteres;
    }
    
}
