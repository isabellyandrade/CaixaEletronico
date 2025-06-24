package org.example.contasfactory;

import org.example.contas.Conta;
import org.example.contas.ContaPoupanca;

public class ContaPoupancaFactory extends ContaFactory {
    public Conta criarConta() {
        String numeroConta = "CP -" + (10000 + new java.util.Random().nextInt(90000));
        
        // Começa com saldo 0.
        return new ContaPoupanca(numeroConta, 0.0);
    }
}
