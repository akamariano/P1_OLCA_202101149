/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizadores;

/**
 *
 * @author Mariano Rack
 */
public class Nodo_Binario {
    public String dato;
    public Nodo_Binario izq;
    public Nodo_Binario der;
    public Nodo_Binario(String dato){
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
    
}
