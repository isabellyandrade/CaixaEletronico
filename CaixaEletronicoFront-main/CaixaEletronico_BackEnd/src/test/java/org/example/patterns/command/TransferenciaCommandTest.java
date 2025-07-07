package org.example.patterns.command;

import org.example.model.conta.Conta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Classe de teste para o TransferenciaCommand, demonstrando o uso de Mockito
 * para verificar a interação entre a conta de origem e a de destino.
 */
@ExtendWith(MockitoExtension.class)
class TransferenciaCommandTest {

    // Usamos @Mock para criar "dublês" das contas.
    // Precisamos de dois, pois a transferência envolve duas contas.
    @Mock
    private Conta contaOrigemMock;

    @Mock
    private Conta contaDestinoMock;

    @Test
    void chamaOMetodoTransferirComOsParametrosCorretos() {
        // Arrange (Preparação)
        // 1. Definimos os dados do nosso cenário de teste.
        double valorDaTransferencia = 500.0;

        // 2. Criamos o objeto que queremos testar (o comando),
        //    passando nossos "bonecos" (os mocks) para ele.
        Command transferenciaCommand = new TransferenciaCommand(contaOrigemMock, contaDestinoMock, valorDaTransferencia);

        // 3. Damos um "roteiro" ao nosso mock principal (a conta de origem).
        //    "QUANDO o método 'transferir' for chamado com este valor e esta conta de destino,
        //     ENTÃO retorne 'true'."
        Mockito.when(contaOrigemMock.transferir(valorDaTransferencia, contaDestinoMock)).thenReturn(true);

        // Act (Ação)
        // Executamos o comando.
        boolean sucesso = transferenciaCommand.execute();

        // Assert (Verificação)
        // 1. Verificação de Comportamento com Mockito:
        //    Perguntamos ao Mockito: "O método 'transferir' do nosso dublê 'contaOrigemMock'
        //    foi realmente chamado, exatamente uma vez, com o valor 500.0
        //    e com o objeto 'contaDestinoMock'?"
        Mockito.verify(contaOrigemMock).transferir(valorDaTransferencia, contaDestinoMock);

        // 2. Verificação de Resultado com JUnit:
        //    Verificamos se o comando retornou o 'true' que programamos no mock.
        assertTrue(sucesso);
    }
}