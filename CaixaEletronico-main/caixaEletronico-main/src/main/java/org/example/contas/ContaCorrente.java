package org.example.contas;

public class ContaCorrente extends ContaAbstrata {

    public ContaCorrente(String numeroConta, double saldoInicial) {
        super(numeroConta, saldoInicial); 
    }

    @Override
    public void exibirTipoConta() {
        System.out.println("Conta Corrente criada.");
    }
}