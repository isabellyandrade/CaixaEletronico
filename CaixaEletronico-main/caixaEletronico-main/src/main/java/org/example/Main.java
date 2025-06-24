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
        cadastrarUsuariosIniciais();
        System.out.println("BEM-VINDO!");

        while (true) {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("[1] Cadastrar Novo Usuario");
            System.out.println("[2] Efetuar Login");
            System.out.println("[0] Sair do Programa");
            System.out.print("Escolha uma opcao: ");

            int escolha = scanner.nextInt();
            scanner.nextLine(); // << CORREÇÃO: Limpa o \n deixado pelo nextInt() ANTES do switch.

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
                    System.out.println("Opção invalida. Tente novamente.");
            }
        }
    }

    private static void efetuarLogin() {
        System.out.println("\n##### Login de Usuario #####");
        System.out.print("Nome de usuário: ");
        // Agora esta linha vai funcionar, pois o buffer foi limpo no menu principal.
        String loginNome = scanner.nextLine().toLowerCase();

        System.out.print("Senha: ");
        String loginSenha = scanner.nextLine();

        Usuario usuarioLogado = usuariosDb.get(loginNome);

        if (usuarioLogado != null && usuarioLogado.autenticar(loginSenha)) {
            System.out.println("\nLogin realizado com sucesso!");
            Conta contaProxy = new ContaProxy(usuarioLogado.getConta(), usuarioLogado);
            iniciarMenuOperacoes(contaProxy, usuarioLogado);
        } else {
            System.out.println("\nUsuario ou senha invalidos.");
        }
    }

    private static void cadastrarNovoUsuario() {
        System.out.println("\n##### Cadastro de Novo Usuario #####");
        System.out.print("Digite o nome do novo usuário: ");
        String nome = scanner.nextLine().toLowerCase();

        if (usuariosDb.containsKey(nome)) {
            System.out.println("Erro: Já existe um usuário com este nome.");
            return;
        }

        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        System.out.println("Escolha o tipo de conta (1-Corrente, 2-Poupanca, 3-Empresarial):");
        int tipo = scanner.nextInt();
        scanner.nextLine(); // << CORREÇÃO: Limpa o \n deixado pelo nextInt().

        ContaFactory factory;
        switch (tipo) {
            case 1: factory = new ContaCorrenteFactory(); break;
            case 2: factory = new ContaPoupancaFactory(); break;
            case 3: factory = new ContaEmpresarialFactory(); break;
            default:
                System.out.println("Tipo de conta invalido. Cadastro cancelado.");
                return;
        }

        Usuario novoUsuario = new Usuario(nome, senha, factory);
        usuariosDb.put(nome, novoUsuario);
        System.out.println("Usuario '" + nome + "' cadastrado com sucesso!");
    }
    
    private static void iniciarMenuOperacoes(Conta conta, Usuario usuario) {
        // Este método já estava correto na sua versão!
        // O único ponto de atenção é o nextDouble() que também deixa um \n
        while (true) {
            System.out.println("\n--- MENU DE OPERACOES ---");
            System.out.println("1. Depositar\n2. Sacar\n3. Transferir\n4. Emitir Extrato\n5. Ver Saldo\n0. Deslogar");
            System.out.print("Escolha uma opcao: ");

            int escolha = scanner.nextInt();
            scanner.nextLine(); // Limpa o \n do nextInt()

            switch (escolha) {
                case 1:
                    System.out.print("Digite o valor para deposito: R$ ");
                    double valorDeposito = scanner.nextDouble();
                    scanner.nextLine(); // Limpa o \n do nextDouble()
                    conta.depositar(valorDeposito);
                    break;
                case 2:
                    System.out.print("Digite o valor para saque: R$ ");
                    double valorSaque = scanner.nextDouble();
                    scanner.nextLine(); // Limpa o \n do nextDouble()
                    conta.sacar(valorSaque);
                    break;
                case 3:
                    System.out.print("Digite o nome de usuario do destinatario: ");
                    String nomeDestino = scanner.nextLine().toLowerCase();
                    Usuario destino = usuariosDb.get(nomeDestino);
                    if (destino == null) { System.out.println("\n[ERRO] Usuário de destino não encontrado."); break; }
                    if (destino.getNome().equals(usuario.getNome())) { System.out.println("\n[ERRO] Nao eh possível transferir para si mesmo."); break; }
                    System.out.print("Digite o valor a ser transferido: R$ ");
                    double valorTransferencia = scanner.nextDouble();
                    scanner.nextLine(); // Limpa o \n do nextDouble()
                    conta.transferir(valorTransferencia, destino.getConta());
                    break;
                case 4:
                    conta.emitirExtrato(); break;
                case 5:
                    System.out.printf("Seu saldo atual eh: R$ %.2f\n", conta.getSaldo()); break;
                case 0:
                    usuario.deslogar(); System.out.println("Você foi deslogado."); return;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }
        }
    }

    private static void cadastrarUsuariosIniciais() {
        // Usei toLowerCase() para consistência com a lógica de cadastro
        Usuario user1 = new Usuario("joao", "123", new ContaCorrenteFactory());
        //user1.getConta().depositar(1500);
        usuariosDb.put(user1.getNome(), user1);

        Usuario user2 = new Usuario("maria", "456", new ContaPoupancaFactory());
        //user2.getConta().depositar(3250.50);
        usuariosDb.put(user2.getNome(), user2);
    }
}