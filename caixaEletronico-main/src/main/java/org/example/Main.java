package org.example;

import org.example.contas.Conta;
import org.example.contasfactory.ContaCorrenteFactory;
import org.example.contasfactory.ContaEmpresarialFactory;
import org.example.contasfactory.ContaFactory;
import org.example.contasfactory.ContaPoupancaFactory;
import org.example.proxy.ContaProxy;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Map<String, Usuario> usuariosDb = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // O programa já começa com alguns usuários no "banco de dados"
        cadastrarUsuariosIniciais();
        System.out.println("BEM-VINDO!");

        while (true) {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("[1] Cadastrar Novo Usuário");
            System.out.println("[2] Efetuar Login");
            System.out.println("[0] Sair do Programa");
            System.out.print("Escolha uma opção: ");

            int escolha = scanner.nextInt();

            switch (escolha) {
                case 1:
                    cadastrarNovoUsuario();
                    break;
                case 2:
                    efetuarLogin();
                    break;
                case 0:
                    System.out.println("\nEncerrando o programa. Obrigado!");
                    scanner.close();
                    return; 
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void efetuarLogin() {
        System.out.println("\n##### Login de Usuário #####");
        System.out.print("Nome: ");
        String loginNome = scanner.nextLine();
        System.out.print("Senha: ");
        String loginSenha = scanner.nextLine();

        Usuario usuarioLogado = usuariosDb.get(loginNome);

        if (usuarioLogado != null && usuarioLogado.autenticar(loginSenha)) {
            System.out.println("\nLogin realizado com sucesso! Olá, " + usuarioLogado.getNome() + ".");
            // Cria o Proxy para o usuário logado
            Conta contaProxy = new ContaProxy(usuarioLogado.getConta(), usuarioLogado);
            // Inicia o menu de operações bancárias
            iniciarMenuOperacoes(contaProxy, usuarioLogado);
        } else {
            System.out.println("\nUsuário ou senha inválidos.");
        }
    }

    private static void cadastrarNovoUsuario() {
        System.out.println("\n##### Cadastro de Novo Usuário #####");
        System.out.print("Digite o nome: ");
        String nome = scanner.nextLine();

        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();
        System.out.println("Escolha o tipo de conta (1-Corrente, 2-Poupança, 3-Empresarial):");
        int tipo = scanner.nextInt();

        ContaFactory factory;
        switch (tipo) {
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
                System.out.println("Tipo de conta inválido. Cadastro cancelado.");
                return;
        }

        Usuario novoUsuario = new Usuario(nome, senha, factory);
        usuariosDb.put(nome, novoUsuario);
        System.out.println("Usuário '" + nome + "' cadastrado com sucesso!");
    }

    /**
     * Exibe o menu de operações bancárias (sacar, depositar, etc.).
     */
    private static void iniciarMenuOperacoes(Conta conta, Usuario usuario) {
        while (true) {
            System.out.println("\n--- MENU DE OPERAÇÕES ---");
            System.out.println("1. Depositar");
            System.out.println("2. Sacar");
            System.out.println("3. Transferir");
            System.out.println("4. Emitir Extrato");
            System.out.println("5. Ver Saldo");
            System.out.println("0. Deslogar (Voltar ao menu principal)");
            System.out.print("Escolha uma opção: ");

            int escolha = scanner.nextInt();

            switch (escolha) {
                 case 1: // Depositar
                    System.out.print("Digite o valor para depósito: R$ ");
                    double valorDeposito = scanner.nextDouble();
                    conta.depositar(valorDeposito);
                    break;
                case 2: // Sacar
                    System.out.print("Digite o valor para saque: R$ ");
                    double valorSaque = scanner.nextDouble();
                    conta.sacar(valorSaque);
                    break;
                case 3: // Transferir
                    System.out.print("Digite o nome de usuário do destinatário: ");
                    String nomeDestino = scanner.nextLine().toLowerCase();
                    Usuario destino = usuariosDb.get(nomeDestino);

                    if (destino == null) {
                        System.out.println("Usuário de destino não encontrado.");
                        break;
                    }
                    System.out.print("Digite o valor a ser transferido: R$ ");
                    double valorTransferencia = scanner.nextDouble();
                    conta.transferir(valorTransferencia, destino.getConta());
                    break;
                case 4: // Extrato
                    conta.emitirExtrato();
                    break;
                case 5: // Saldo
                    System.out.printf("Seu saldo atual é: R$ %.2f\n", conta.getSaldo());
                    break;
                case 0: // Deslogar
                    usuario.deslogar();
                    System.out.println("Você foi deslogado.");
                    return; // Retorna ao menu principal
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    /**
     * Cria usuários "fake" para que o programa já inicie com dados.
     */
    private static void cadastrarUsuariosIniciais() {
        Usuario user1 = new Usuario("Joao", "123", new ContaCorrenteFactory());
        user1.getConta().depositar(1500);
        usuariosDb.put(user1.getNome(), user1);

        Usuario user2 = new Usuario("Maria", "456", new ContaPoupancaFactory());
        user2.getConta().depositar(3250.50);
        usuariosDb.put(user2.getNome(), user2);
    }
}