package org.example.contas;

public class ContaPoupanca extends ContaAbstrata {

    public ContaPoupanca(String numeroConta, double saldoInicial) {
        super(numeroConta, saldoInicial); 
    }

    @Override
    public void exibirTipoConta() {
        System.out.println("Conta Poupanca criada.");
    }
}
