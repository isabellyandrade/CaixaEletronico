package org.example.controller;

import org.example.model.Usuario;
import org.example.model.conta.Conta;
import org.example.patterns.command.*;
import org.example.patterns.factory.ContaCorrenteFactory;
import org.example.patterns.factory.ContaEmpresarialFactory;
import org.example.patterns.factory.ContaFactory;
import org.example.patterns.factory.ContaPoupancaFactory;
import org.example.patterns.proxy.ContaProxy;
import org.example.view.CaixaEletronicoView;

import java.util.HashMap;
import java.util.Map;

/**
 * CONTROLLER: O maestro do sistema.
 * Conecta a View (pedidos do usuario) com o Model (lógica de negócio).
 * Usa os Design Patterns para realizar suas tarefas.
 */
public class CaixaEletronicoController {
    private final CaixaEletronicoView view;
    private final Map<String, Usuario> usuariosDb;
    private Usuario usuarioLogado;

    public CaixaEletronicoController() {
        this.view = new CaixaEletronicoView();
        this.usuariosDb = new HashMap<>();
    }

    public void iniciar() {
        cadastrarUsuariosIniciais(); // PATTERN: Factory em acao
        view.exibirMensagem("BEM-VINDO!");

        while (true) {
            int escolha = view.exibirMenuPrincipal();
            switch (escolha) {
                case 1: 
                cadastrarNovoUsuario(); 
                break;
                case 2: 
                efetuarLogin(); 
                break;
                case 0:
                    view.exibirMensagem("\nEncerrando o programa. Obrigado!");
                    return;
                default:
                    view.exibirMensagem("Opcao invalida. Tente novamente.");
            }
        }
    }

    private void efetuarLogin() {
        String[] credenciais = view.pedirCredenciaisLogin();
        String nome = credenciais[0].toLowerCase();
        String senha = credenciais[1];
        this.usuarioLogado = usuariosDb.get(nome);

        if (usuarioLogado != null && usuarioLogado.autenticar(senha)) {
            view.exibirMensagem("\nLogin realizado com sucesso! Ola, " + usuarioLogado.getNome() + ".");
            iniciarMenuOperacoes();
        } else {
            view.exibirMensagem("\nUsuario ou senha invalidos.");
            this.usuarioLogado = null;
        }
    }

    private void cadastrarNovoUsuario() {
        String[] dados = view.pedirDadosCadastro();
        String nome = dados[0].toLowerCase();
        String senha = dados[1];
        int tipoConta = Integer.parseInt(dados[2]);

        ContaFactory factory;
        switch (tipoConta) {
            case 1: 
            factory = new ContaCorrenteFactory(); 
            break;
            case 2: 
            factory = new ContaPoupancaFactory(); 
            break;
            case 3: 
            factory = new ContaEmpresarialFactory(); 
            break;
            default:
                view.exibirMensagem("Tipo de conta invalido. Cadastro cancelado.");
                return;
        }

        Usuario novoUsuario = new Usuario(nome, senha, factory);
        usuariosDb.put(nome, novoUsuario);
        view.exibirMensagem("Usuario '" + nome + "' cadastrado com sucesso!");
    }

    private void iniciarMenuOperacoes() {
        // PATTERN: Proxy. Criamos o proxy que adiciona a camada de seguranca.
        // Todas as operacões passarao por ele.
        Conta contaProxy = new ContaProxy(this.usuarioLogado.getConta(), this.usuarioLogado);

        while (true) {
            int escolha = view.exibirMenuOperacoes();
            Command comando = null; // PATTERN: Command - sera criado a seguir

            switch (escolha) {
                case 1: // Depositar
                    double valorDeposito = view.pedirValor("Digite o valor para depósito: R$ ");
                    comando = new DepositoCommand(contaProxy, valorDeposito);
                    break;
                case 2: // Sacar
                    double valorSaque = view.pedirValor("Digite o valor para saque: R$ ");
                    comando = new SaqueCommand(contaProxy, valorSaque);
                    break;
                case 3: // Transferir
                    String nomeDestino = view.pedirUsername("Digite o nome de usuario do destinatario: ").toLowerCase();
                    Usuario destino = usuariosDb.get(nomeDestino);

                    if (destino == null) { view.exibirMensagem("\n[ERRO] Usuario de destino nao encontrado."); continue; }
                    if (destino.getNome().equals(usuarioLogado.getNome())) { view.exibirMensagem("\n[ERRO] Nao eh possível transferir para si mesmo."); continue; }
                    
                    double valorTransferencia = view.pedirValor("Digite o valor a ser transferido: R$ ");
                    comando = new TransferenciaCommand(contaProxy, destino.getConta(), valorTransferencia);
                    break;
                case 4: // Extrato
                    view.exibirExtrato(contaProxy.getHistoricoTransacoes(), contaProxy.getSaldo());
                    break;
                case 5: // Saldo
                    view.exibirMensagem(String.format("Seu saldo atual eh: R$ %.2f", contaProxy.getSaldo()));
                    break;
                case 0:
                    usuarioLogado.deslogar();
                    view.exibirMensagem("Você foi deslogado.");
                    return;
                default:
                    view.exibirMensagem("Opcao invalida. Tente novamente.");
            }

            // PATTERN: Command - O comando eh executado aqui
            if (comando != null) {
                boolean sucesso = comando.execute();
                if (sucesso) {
                    view.exibirMensagem("Operacao realizada com sucesso!");
                } else {
                    view.exibirMensagem("Falha na operacao. Verifique os dados ou o saldo.");
                }
            }
        }
    }

    private void cadastrarUsuariosIniciais() {
        Usuario user1 = new Usuario("joao", "123", new ContaCorrenteFactory());
        user1.getConta().depositar(1500);
        usuariosDb.put(user1.getNome(), user1);

        Usuario user2 = new Usuario("maria", "456", new ContaPoupancaFactory());
        user2.getConta().depositar(3250.50);
        usuariosDb.put(user2.getNome(), user2);
    }
}