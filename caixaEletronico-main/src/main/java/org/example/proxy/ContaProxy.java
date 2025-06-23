package org.example.proxy;

import org.example.contas.Conta;
import org.example.Usuario;
import org.example.transacoes.Transacao;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class ContaProxy implements Conta {
    private final Conta contaReal;
    private final Usuario usuario;

    private static final Map<Integer, Integer> slotsDeNotas = new LinkedHashMap<>();

    static {
        slotsDeNotas.put(200, 20);
        slotsDeNotas.put(100, 30);
        slotsDeNotas.put(50, 50);
        slotsDeNotas.put(20, 50);
        slotsDeNotas.put(10, 100);
        slotsDeNotas.put(5, 100);
        slotsDeNotas.put(2, 200);
    }

    public ContaProxy(Conta contaReal, Usuario usuario) {
        this.contaReal = contaReal;
        this.usuario = usuario;
    }

    private boolean verificarAutenticacao() {
        if (usuario != null && usuario.isAutenticado()) {
            return true;
        }
        System.out.println("\n[Proxy] ACESSO NEGADO: Usuário não autenticado.");
        return false;
    }

    // A LÓGICA FINAL DO SAQUE
    @Override
    public void sacar(double valor) {
        if (!verificarAutenticacao()) 
        return;

        // A lógica de saque só funciona com valores inteiros
        if (valor % 1 != 0) {
            System.out.println("\n[Proxy] OPERAÇÃO NEGADA: Valor de saque deve ser um número inteiro.");
            return;
        }

        System.out.println("\n[Proxy] Iniciando verificação de saque para R$ " + String.format("%.2f", valor));

        if (contaReal.getSaldo() < valor) {
            System.out.println("[Proxy] OPERAÇÃO NEGADA: Saldo insuficiente.");
            return;
        }

        // Chama nosso método para verificar a disponibilidade de notas
        Map<Integer, Integer> planoDeSaque = verificarDisponibilidadeNotas((int) valor);

        // Se o plano de saque for nulo, significa que não foi possível compor o valor.
        if (planoDeSaque == null) {
            System.out.println("[Proxy] OPERAÇÃO NEGADA: Não é possível compor o valor de R$ " + String.format("%.2f", valor) + " com as notas disponíveis no caixa.");
            return;
        }

        // Se chegamos aqui, TUDO foi validado com sucesso!
        System.out.println("[Proxy] Validações aprovadas!");

        // 1. Debita o valor da conta (operação contábil)
        contaReal.sacar(valor);

        // 2. Remove as notas do caixa eletrônico (operação física)
        atualizarSlots(planoDeSaque);

        // 3. Informa ao usuário as notas que ele está recebendo
        System.out.println("Saque realizado com sucesso! Retire as notas abaixo:");
        for (Map.Entry<Integer, Integer> entry : planoDeSaque.entrySet()) {
            System.out.printf("   - %d nota(s) de R$ %d\n", entry.getValue(), entry.getKey());
        }
    }

    /**
     * Verifica se é possível formar o valor do saque com as notas disponíveis.
     * @param valor O valor inteiro a ser sacado.
     * @return Um Mapa com o plano de saque (Nota -> Quantidade) ou null se não for possível.
     */
    private Map<Integer, Integer> verificarDisponibilidadeNotas(int valor) {
        Map<Integer, Integer> planoDeSaque = new LinkedHashMap<>();
        int valorRestante = valor;

        for (Map.Entry<Integer, Integer> entry : slotsDeNotas.entrySet()) {
            int nota = entry.getKey();
            int qtdDisponivel = entry.getValue();

            if (valorRestante >= nota && qtdDisponivel > 0) {
                int qtdAUsar = Math.min(valorRestante / nota, qtdDisponivel);
                if (qtdAUsar > 0) {
                    planoDeSaque.put(nota, qtdAUsar);
                    valorRestante -= qtdAUsar * nota;
                }
            }
        }

        // Se ao final o valor restante for 0, o saque é possível.
        return (valorRestante == 0) ? planoDeSaque : null;
    }

    /**
     * Atualiza a contagem de notas nos slots após um saque bem-sucedido.
     * @param planoDeSaque O mapa de notas que foram entregues ao cliente.
     */
    private void atualizarSlots(Map<Integer, Integer> planoDeSaque) {
        for (Map.Entry<Integer, Integer> entry : planoDeSaque.entrySet()) {
            int nota = entry.getKey();
            int qtdUsada = entry.getValue();
            slotsDeNotas.put(nota, slotsDeNotas.get(nota) - qtdUsada);
        }
    }

    // DEMAIS MÉTODOS

    @Override
    public void depositar(double valor) {
        if (verificarAutenticacao()) {
            contaReal.depositar(valor);
        }
    }

    @Override
    public void transferir(double valor, Conta contaDestino) {
        if (verificarAutenticacao()) {
            if (contaReal.getSaldo() < valor) {
                System.out.println("\n[Proxy] OPERAÇÃO NEGADA: Saldo insuficiente para transferência.");
                return;
            }
            contaReal.transferir(valor, contaDestino);
        }
    }

    @Override
    public void emitirExtrato() {
        if (verificarAutenticacao()) {
            contaReal.emitirExtrato();
        }
    }

    @Override
    public double getSaldo() {
        return verificarAutenticacao() ? contaReal.getSaldo() : 0.0;
    }

    @Override
    public void exibirTipoConta() {
        if (verificarAutenticacao()) {
            contaReal.exibirTipoConta();
        }
    }

    @Override
    public List<Transacao> getHistoricoTransacoes() {
        return verificarAutenticacao() ? contaReal.getHistoricoTransacoes() : null;
    }

    @Override
    public void adicionarTransacao(String tipo, double valor, String descricao) {
        if (verificarAutenticacao()) {
            contaReal.adicionarTransacao(tipo, valor, descricao);
        }
    }
}