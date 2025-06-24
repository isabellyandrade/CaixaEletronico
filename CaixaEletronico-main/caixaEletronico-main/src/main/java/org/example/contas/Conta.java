package org.example.contas;

import java.util.List; 
import org.example.transacoes.Transacao; 

public interface Conta {
    
    void exibirTipoConta();

    /**
     * Aumenta o saldo da conta.
     * @param valor O valor a ser depositado (deve ser positivo).
     */
    void depositar(double valor);

    /**
     * Diminui o saldo da conta. A lógica de verificar notas FÍSICAS
     * não ficará aqui, mas sim no Proxy.
     * @param valor O valor a ser sacado.
     */
    void sacar(double valor);

    /**
     * Move dinheiro desta conta para outra.
     * @param valor O valor a ser transferido.
     * @param contaDestino A conta que receberá o valor.
     */
    void transferir(double valor, Conta contaDestino);

    /**
     * Retorna o saldo atual da conta.
     * @return O saldo.
     */
    double getSaldo();

    /**
     * Retorna a lista de todas as transações para a emissão do extrato.
     * @return Uma lista de objetos Transacao.
     */
    List<Transacao> getHistoricoTransacoes();

    /**
     * Adiciona uma transação ao histórico da conta.
     * Este método será usado internamente para registrar cada movimento.
     */
    void adicionarTransacao(String tipo, double valor, String descricao);

    /**
     * Emite o extrato da conta, mostrando todas as transações.
     * Este método deve formatar e exibir as transações de forma legível.
     */
    void emitirExtrato();
    
    String getNumeroConta(); 
}