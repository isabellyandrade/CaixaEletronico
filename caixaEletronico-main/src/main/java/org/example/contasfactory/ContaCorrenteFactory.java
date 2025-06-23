package org.example.contasfactory;

import org.example.contas.Conta;
import org.example.contas.ContaCorrente;

public class ContaCorrenteFactory extends ContaFactory {
    @Override
    public Conta criarConta() {
        String numeroConta = "CC -" + (10000 + new java.util.Random().nextInt(90000));
        
        // Começa com saldo 0.
        return new ContaCorrente(numeroConta, 0.0);
    }
}