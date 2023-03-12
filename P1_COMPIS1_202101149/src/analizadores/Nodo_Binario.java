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
public class Nodo_Binario {
    private String dato;
    private boolean hoja = false;
    private boolean anulable;
    private int numero;
    private ArrayList<Integer> primeros = new ArrayList<>();
    private ArrayList<Integer> ultimos = new ArrayList<>();
    private Nodo_Binario izq;
    private Nodo_Binario der;

    public Nodo_Binario(String dato) {
        this.dato = dato;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public Nodo_Binario getIzq() {
        return izq;
    }

    public void setIzq(Nodo_Binario izq) {
        this.izq = izq;
    }

    public Nodo_Binario getDer() {
        return der;
    }

    public void setDer(Nodo_Binario der) {
        this.der = der;
    }

    public boolean isHoja() {
        return hoja;
    }

    public void setHoja(boolean hoja) {
        this.hoja = hoja;
    }

    public boolean isAnulable() {
        return anulable;
    }

    public void setAnulable(boolean anulable) {
        this.anulable = anulable;
    }

    public ArrayList<Integer> getPrimeros() {
        return primeros;
    }

    public ArrayList<Integer> getUltimos() {
        return ultimos;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
    
    
}
