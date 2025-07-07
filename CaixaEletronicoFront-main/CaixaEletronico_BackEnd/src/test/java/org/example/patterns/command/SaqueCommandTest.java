package org.example.patterns.command;

import org.example.model.conta.Conta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para o SaqueCommand, demonstrando o uso de Mockito.
 * A anotação abaixo ativa a integração do Mockito com o JUnit.
 */
@ExtendWith(MockitoExtension.class)
class SaqueCommandTest {

    /**
     * @Mock: Esta anotação do Mockito cria um "boneco de teste" (um mock)
     * da interface Conta. É um objeto falso que podemos controlar e espionar.
     */
    @Mock
    private Conta contaMock;

    @Test
    void chamaOMetodoSacarDaContaQuandoExecutado() {
        // Arrange (Preparação)
        // 1. Criamos o objeto que queremos testar (o comando),
        //    passando o nosso "boneco" (o contaMock) para ele.
        double valorDoSaque = 100.0;
        Command saqueCommand = new SaqueCommand(contaMock, valorDoSaque);

        // 2. (Opcional, mas boa prática) Damos um "roteiro" para o nosso boneco.
        //    "QUANDO (when) o método 'sacar' do seu dublê for chamado com 100.0,
        //     ENTÃO (thenReturn) responda 'true'."
        Mockito.when(contaMock.sacar(valorDoSaque)).thenReturn(true);

        // Act (Ação)
        // Executamos o comando. Ele vai, por dentro, chamar o método sacar() do nosso contaMock.
        boolean sucesso = saqueCommand.execute();

        // Assert (Verificação)
        // 1. Verificação de Comportamento com Mockito:
        //    Esta é a parte mais importante. Estamos perguntando ao Mockito:
        //    "Ei, o método 'sacar' do seu boneco foi realmente chamado,
        //     exatamente uma vez, e com o valor 100.0?"
        Mockito.verify(contaMock).sacar(valorDoSaque);

        // 2. Verificação de Resultado com JUnit:
        //    Também podemos verificar se o resultado retornado foi o esperado.
        assertTrue(sucesso);
    }

    @Test
    void retornaFalseSeOSaqueNaContaFalhar() {
        // Arrange
        double valorDoSaque = 50.0;
        Command saqueCommand = new SaqueCommand(contaMock, valorDoSaque);
        // Desta vez, programamos nosso "boneco" para simular uma falha
        Mockito.when(contaMock.sacar(valorDoSaque)).thenReturn(false);

        // Act
        boolean sucesso = saqueCommand.execute();

        // Assert
        // Verificamos que o método sacar foi chamado, mesmo que tenha falhado
        Mockito.verify(contaMock).sacar(valorDoSaque);
        // E verificamos que o comando repassou o resultado 'false' corretamente
        assertFalse(sucesso);
    }
}