package org.example.patterns.command;

import org.example.model.conta.Conta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Classe de teste para o DepositoCommand, utilizando Mockito
 * para verificar se a interação com a Conta ocorre corretamente.
 */
@ExtendWith(MockitoExtension.class)
class DepositoCommandTest {

    // Criamos um "dublê" da interface Conta.
    // Não nos importamos com o saldo real, apenas se o método certo é chamado.
    @Mock
    private Conta contaMock;

    @Test
    void chamaOMetodoDepositarDaContaQuandoExecutado() {
        // Arrange (Preparação)
        // 1. Definimos os dados do nosso cenário.
        double valorDoDeposito = 250.0;

        // 2. Criamos o objeto que queremos testar (o comando),
        //    passando o nosso "dublê" (o contaMock) para ele.
        Command depositoCommand = new DepositoCommand(contaMock, valorDoDeposito);

        // 3. Instruímos o nosso dublê sobre como ele deve se comportar.
        //    "QUANDO o método 'depositar' do seu dublê for chamado com 250.0,
        //     ENTÃO retorne 'true'."
        Mockito.when(contaMock.depositar(valorDoDeposito)).thenReturn(true);

        // Act (Ação)
        // Executamos o comando. Ele vai chamar o método depositar() do nosso contaMock.
        boolean sucesso = depositoCommand.execute();

        // Assert (Verificação)
        // 1. Verificação de Comportamento com Mockito:
        //    Esta é a verificação principal. Perguntamos ao Mockito:
        //    "O método 'depositar' do dublê 'contaMock' foi realmente chamado,
        //     exatamente uma vez, e com o valor 250.0?"
        Mockito.verify(contaMock).depositar(valorDoDeposito);

        // 2. Verificação de Resultado com JUnit:
        //    Confirmamos que o comando repassou o 'true' que o mock retornou.
        assertTrue(sucesso);
    }
}