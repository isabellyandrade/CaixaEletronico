package org.example;

import org.example.controller.CaixaEletronicoController;

/**
 * Ponto de entrada da aplicação.
 * A única responsabilidade é instanciar e iniciar o Controller.
 */
public class Main {
    public static void main(String[] args) {
        new CaixaEletronicoController().iniciar();
    }
}