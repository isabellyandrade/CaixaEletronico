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
            this.adicionarTransacao("DEPOSITO INICIAL", saldoInicial, "Carga inicial da conta");
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
            this.adicionarTransacao("DEPOSITO", valor, "Deposito em conta");
            System.out.println("Deposito de R$ " + String.format("%.2f", valor) + " realizado com sucesso.");
        } else {
            System.out.println("Valor de deposito invalido.");
        }
    }

    @Override
    public void sacar(double valor) {
        if (valor > 0 && valor <= this.saldo) {
            this.saldo -= valor;
            this.adicionarTransacao("SAQUE", valor, "Saque em caixa eletronico");
        }
    }

    @Override
    public String getNumeroConta() {
        return this.numeroConta;
    }

    @Override
    public void transferir(double valor, Conta contaDestino) {
        if (valor <= 0) {
            System.out.println("Valor de transferencia invalido.");
            return;
        }

        // Debita da conta de origem
        this.saldo -= valor;
        this.adicionarTransacao("TRANSFERENCIA ENVIADA", valor, "Para conta: " + contaDestino.getNumeroConta());

        // =================================================================
        // AQUI ESTÁ A CORREÇÃO: Usar o objeto "contaDestino"
        contaDestino.depositar(valor);
        // =================================================================

        System.out.println("\nTransferencia de R$ " + String.format("%.2f", valor) + " realizada com sucesso para a conta " + contaDestino.getNumeroConta() + ".");
    }

    @Override
    public void emitirExtrato() {
        System.out.println("\n==================================================================");
        System.out.println("EXTRATO DA CONTA: " + this.numeroConta);
        System.out.println("------------------------------------------------------------------");
        if (this.historicoTransacoes.isEmpty()) {
            System.out.println("Nenhuma transacao encontrada.");
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