package org.example.contas;

public class ContaEmpresarial extends ContaAbstrata {

    public ContaEmpresarial(String numeroConta, double saldoInicial) {
        super(numeroConta, saldoInicial); 
    }
    
    @Override
    public void exibirTipoConta() {
        System.out.println("Conta Empresarial criada.");
    }
}
