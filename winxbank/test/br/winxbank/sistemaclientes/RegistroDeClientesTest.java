package br.winxbank.sistemaclientes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.winxbank.sistemabancario.Cartao;
import br.winxbank.sistemabancario.CartaoCredito;
import br.winxbank.sistemabancario.Conta;
import br.winxbank.sistemabancario.ContaCorrente;
import br.winxbank.sistemabancario.ContaPoupanca;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class RegistroDeClientesTest {
    RegistroDeClientes reg;
    PrintStream standardOut = System.out;
    ByteArrayOutputStream outputStreamCaptor;

    @BeforeEach 
    void testSetup()
    {
        reg = RegistroDeClientes.getInstancia();
        reg.limparListaDeClientes(); // Inicia o registro vazio para cada teste

        // Usado para capturar os outputs de print
        outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test 
    void testCadastrarCliente_1()
    {

    }

    @Test 
    void testAtualizarCliente_1() throws Exception // Sem clientes 
    {
        // Cliente que deverá ser atualizado
        Cliente clienteAtualizar = mock(Cliente.class);
        clienteAtualizar.cpf = "109.876.543-21";
        clienteAtualizar.nome = "Maria";

        reg.atualizarCliente(clienteAtualizar);

        // Pega a lista de clientes após a atualização
        ArrayList<Cliente> novosClientes = reg.getClientes();

        assertEquals(0, novosClientes.size()); // Verifica que continua sem clientes 
    }   

    @Test 
    void testAtualizarCliente_2() throws Exception // Cliente não encontrado 
    {
        // Cliente que estará no registro
        Cliente clienteAdicionado = mock(Cliente.class);
        clienteAdicionado.cpf = "123.456.789-10";
        clienteAdicionado.nome= "Jose";

        // Cliente que deverá ser atualizado
        Cliente clienteAtualizar = mock(Cliente.class);
        clienteAtualizar.cpf = "109.876.543-21";
        clienteAtualizar.nome = "Maria";

        // Adiciona o cliente base ao registro
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(clienteAdicionado);
        reg.setClientes(clientes);

        reg.atualizarCliente(clienteAtualizar);

        // Pega a lista de clientes após a atualização
        ArrayList<Cliente> novosClientes = reg.getClientes();

        for(int i = 0; i < novosClientes.size(); i++){
            if(novosClientes.get(i).cpf.equals(clienteAdicionado.cpf)){ // Se for o cliente original
                assertEquals(clienteAdicionado.nome, novosClientes.get(i).nome); // Verifica que o nome não foi atualizado
            }
        }
    }   

    @Test 
    void testAtualizarCliente_3() throws Exception // Cliente encontrado 
    {
        // Cliente que estará no registro
        Cliente clienteAdicionado = mock(Cliente.class);
        clienteAdicionado.cpf = "123.456.789-10";
        clienteAdicionado.nome= "Jose";

        // Cliente que deverá ser atualizado
        Cliente clienteAtualizar = mock(Cliente.class);
        clienteAtualizar.cpf = "123.456.789-10";
        clienteAtualizar.nome = "Maria";

        // Adiciona o cliente base ao registro
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(clienteAdicionado);
        reg.setClientes(clientes);

        reg.atualizarCliente(clienteAtualizar);

        // Pega a lista de clientes após a atualização
        ArrayList<Cliente> novosClientes = reg.getClientes();

        for(int i = 0; i < novosClientes.size(); i++){
            if(novosClientes.get(i).cpf.equals(clienteAdicionado.cpf)){ // Se for o cliente que deve ter sido atualizado
                assertEquals(clienteAtualizar.nome, novosClientes.get(i).nome); // Ve se o nome foi atualizado
            }
        }
    }   

    
    @Test 
    void testRemoverCliente_1() // Sem clientes 
    {
        // Cliente que deverá ser apagado
        Cliente clienteApagar = mock(Cliente.class);
        clienteApagar.cpf = "109.876.543-21";
        clienteApagar.nome = "Maria";

        reg.removerCliente(clienteApagar);

        // Pega a lista de clientes após a atualização
        ArrayList<Cliente> novosClientes = reg.getClientes();

        assertEquals(0, novosClientes.size()); // Verifica que continua sem clientes 
    }   

    @Test 
    void testRemoverCliente_2() // Cliente não encontrado 
    {
        // Cliente que estará no registro
        Cliente clienteAdicionado = mock(Cliente.class);
        clienteAdicionado.cpf = "123.456.789-10";
        clienteAdicionado.nome= "Jose";

        // Cliente que deverá ser removido
        Cliente clienteApagar = mock(Cliente.class);
        clienteApagar.cpf = "109.876.543-21";
        clienteApagar.nome = "Maria";

        // Adiciona o cliente base ao registro
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(clienteAdicionado);
        reg.setClientes(clientes);

        reg.removerCliente(clienteApagar);

        // Pega a lista de clientes após a atualização
        ArrayList<Cliente> novosClientes = reg.getClientes();

        assertEquals(clientes, novosClientes); // A lista de clientes não deve ter sido modificada
    }   

    @Test 
    void testRemoverCliente_3() // Cliente encontrado 
    {
        // Cliente que estará no registro
        Cliente clienteAdicionado = mock(Cliente.class);
        clienteAdicionado.cpf = "123.456.789-10";
        clienteAdicionado.nome= "Jose";


        // Adiciona o cliente base ao registro
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(clienteAdicionado);
        reg.setClientes(clientes);

        reg.removerCliente(clienteAdicionado);

        // Pega a lista de clientes após a atualização
        ArrayList<Cliente> novosClientes = reg.getClientes();

        assertEquals(0, novosClientes.size()); // Verifica que não há clientes (o adicionado foi apagado)

    }   

    @Test 
    void testChecarCpf_1() // Sem clientes
    {
        
        boolean resultado = reg.checarCpf("123.456.789-10");

        assertFalse(resultado); // Verifica que o cpf não existe

    }

    @Test 
    void testChecarCpf_2() // Cliente não encontrado
    {
        // Cliente que estará no registro
        Cliente clienteAdicionado = mock(Cliente.class);
        clienteAdicionado.cpf = "123.456.789-10";
        clienteAdicionado.nome= "Jose";


        // Adiciona o cliente base ao registro
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(clienteAdicionado);
        reg.setClientes(clientes);

        boolean resultado = reg.checarCpf("109.876.543-21");

        assertFalse(resultado); // Verifica que o cpf não existe

    }

    @Test 
    void testChecarCpf_3() // Cliente encontrado
    {
        // Cliente que estará no registro
        Cliente clienteAdicionado = mock(Cliente.class);
        clienteAdicionado.cpf = "123.456.789-10";
        clienteAdicionado.nome= "Jose";


        // Adiciona o cliente base ao registro
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        clientes.add(clienteAdicionado);
        reg.setClientes(clientes);

        boolean resultado = reg.checarCpf("123.456.789-10");

        assertTrue(resultado); // Verifica que o cpf existe

    }

    @Test 
    void testVisualizarContas_1() // Cliente sem conta cadastrada
    {
        Cliente cliente = mock(Cliente.class);
        when(cliente.getContas()).thenReturn(new ArrayList<>()); //Lista de contas vazia

        reg.visualizarContas(cliente);

        assertEquals("", outputStreamCaptor.toString());
    }

    @Test 
    void testVisualizarContas_2() // Cliente com conta poupança
    {
        Cliente cliente = mock(Cliente.class);
        Cartao cartao = mock(Cartao.class);

        when(cartao.getNumero()).thenReturn(12345678);
        when(cartao.getCsv()).thenReturn(111);

        //Necessário utilizar a classe original para comparação 'conta.getClass() == ContaPoupanca.class' no código original
        ContaPoupanca poupanca = new ContaPoupanca(123, 1500.50, cartao, 0.0);

        ArrayList<Conta> contas = new ArrayList<>();
        contas.add(poupanca); 

        when(cliente.getContas()).thenReturn(contas);

        reg.visualizarContas(cliente);
        DecimalFormat df = new DecimalFormat("0.00");
        String esperado = "[ Conta" + poupanca.getTipoDaConta() + " no: 123" +
                " | Saldo: " + df.format(1500.50) +
                " | DividaEmprestimo: " + df.format(0.0) +
                " | Cartao Debito no: 12345678| csv: 111 ]";
        assertEquals(esperado, outputStreamCaptor.toString().trim());
    }

    @Test 
    void testVisualizarContas_3() // Cliente com conta corrente
    {
        Cliente cliente = mock(Cliente.class);
        Cartao cartao = mock(Cartao.class);
        CartaoCredito cartaoCredito = mock(CartaoCredito.class);

        when(cartao.getNumero()).thenReturn(12345678);
        when(cartao.getCsv()).thenReturn(111);

        when(cartaoCredito.getNumero()).thenReturn(87654321);
        when(cartaoCredito.getCsv()).thenReturn(222);
        when(cartaoCredito.getFatura()).thenReturn(350.00);

        ContaCorrente corrente = new ContaCorrente(123, 1500.50, cartao, 0.0, cartaoCredito);

        ArrayList<Conta> contas = new ArrayList<>();
        contas.add(corrente);

        when(cliente.getContas()).thenReturn(contas);

        reg.visualizarContas(cliente);
        DecimalFormat df = new DecimalFormat("0.00");
        String esperado = "[ Conta" + corrente.getTipoDaConta() + "no: 123" +
                " | Saldo: " + df.format(1500.50) +
                " | DividaEmprestimo: " + df.format(0.0) +
                " | Cartao Debito no: 12345678| csv: 111" +
                " | Cartao Credito no: 87654321| csv: 222" +
                "| fatura: " + df.format(350.00) + " ]";
        assertEquals(esperado, outputStreamCaptor.toString().trim());
    }
}
