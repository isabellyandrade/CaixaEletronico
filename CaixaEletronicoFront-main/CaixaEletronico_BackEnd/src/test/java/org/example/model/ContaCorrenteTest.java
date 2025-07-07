package org.example.model.conta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Classe de Teste Unitário para a ContaCorrente.
 * O nome da classe geralmente termina com "Test".
 */
class ContaCorrenteTest {

    private ContaCorrente conta;

    // Este método com @BeforeEach roda ANTES de cada teste, garantindo um objeto limpo.
    @BeforeEach
    void setUp() {
        // Arrange (Preparação): Cria uma nova conta com saldo inicial de 1000.
        conta = new ContaCorrente("CC-123", 1000.0);
    }

    // A anotação @Test marca este método como um caso de teste executável.
    @Test
    void aumentaOSaldoAoDepositarValor() {
        // Act (Ação): Executa o método que queremos testar.
        boolean sucesso = conta.depositar(200.0);

        // Assert (Verificação): Verifica se o resultado foi o esperado.
        Assertions.assertTrue(sucesso); // Verifica se a operação retornou 'true'
        Assertions.assertEquals(1200.0, conta.getSaldo()); // Verifica se o saldo é exatamente 1200
    }

    @Test
    void naoAlteraOSaldoAoDepositarValorNegativo() {
        // Act
        boolean sucesso = conta.depositar(-50.0);

        // Assert
        Assertions.assertFalse(sucesso); 
        Assertions.assertEquals(1000.0, conta.getSaldo());
    }

    @Test
    void diminuiOSaldoAoSacarValorValido() {
        // Act
        boolean sucesso = conta.sacar(300.0);

        // Assert
        Assertions.assertTrue(sucesso);
        Assertions.assertEquals(700.0, conta.getSaldo());
    }

    @Test
    void naoAlteraOSaldoAoSacarValorMaior() {
        // Act
        boolean sucesso = conta.sacar(1500.0);

        // Assert
        Assertions.assertFalse(sucesso); 
        Assertions.assertEquals(1000.0, conta.getSaldo()); 
    }
}