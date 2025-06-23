package org.example.contas;

import org.example.transacoes.Transacao;
import java.util.ArrayList;
import java.util.List;

public abstract class ContaAbstrata implements Conta {
    protected String numeroConta;
    protected double saldo;
    protected List<Transacao> historicoTransacoes;

    public ContaAbstrata(String numeroConta, double saldoInicial) {
        this.numeroConta = numeroConta;
        this.saldo = saldoInicial;
        this.historicoTransacoes = new ArrayList<>();
        if (saldoInicial > 0) {
            this.adicionarTransacao("DEPÓSITO INICIAL", saldoInicial, "Carga inicial da conta");
        }
    }

    @Override
    public double getSaldo() {
        return this.saldo;
    }

    @Override
    public List<Transacao> getHistoricoTransacoes() {
        return new ArrayList<>(this.historicoTransacoes);
    }
    
    @Override
    public void adicionarTransacao(String tipo, double valor, String descricao) {
        this.historicoTransacoes.add(new Transacao(tipo, valor, descricao));
    }

    @Override
    public void depositar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
            this.adicionarTransacao("DEPÓSITO", valor, "Depósito em conta");
            System.out.println("Depósito de R$ " + String.format("%.2f", valor) + " realizado com sucesso.");
        } else {
            System.out.println("Valor de depósito inválido.");
        }
    }

    @Override
    public void sacar(double valor) {
        if (valor > 0 && valor <= this.saldo) {
            this.saldo -= valor;
            this.adicionarTransacao("SAQUE", valor, "Saque em caixa eletrônico");
        }
    }

    @Override
    public void transferir(double valor, Conta contaDestino) {
        // A validação de saldo e autenticação será feita no Proxy.
        if (valor > 0 && valor <= this.saldo) {
            // Debita da conta de origem
            this.saldo -= valor;
            this.adicionarTransacao("TRANSFERÊNCIA ENVIADA", valor, "Para conta: " + ((ContaAbstrata) contaDestino).numeroConta);
            
            // Credita na conta de destino
            contaDestino.depositar(valor);

            System.out.println("Transferência de R$ " + String.format("%.2f", valor) + " realizada com sucesso.");
        }
    }

    @Override
    public void emitirExtrato() {
        System.out.println("\n==================================================================");
        System.out.println("EXTRATO DA CONTA: " + this.numeroConta);
        System.out.println("------------------------------------------------------------------");
        if (this.historicoTransacoes.isEmpty()) {
            System.out.println("Nenhuma transação encontrada.");
        } else {
            for (Transacao t : this.historicoTransacoes) {
                System.out.println(t);
            }
        }
        System.out.println("------------------------------------------------------------------");
        System.out.printf("SALDO ATUAL: R$ %.2f\n", this.getSaldo());
        System.out.println("==================================================================");
    }

    // Apenas este método precisa ser implementado por cada classe filha.
    @Override
    public abstract void exibirTipoConta();
}