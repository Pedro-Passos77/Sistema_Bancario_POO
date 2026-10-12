package br.winxbank.sistemabancario;

import br.winxbank.tempo.Ano;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class CartaoCreditoTest {
    private static final double tolerancia = 1e-8;
    private InputStream entrada_original;
    @BeforeEach
    void preparar() throws Exception {
        entrada_original = System.in;
        singleton(Ano.class);
        singleton(Banco.class);
        Ano.getInstancia().setMesAtual("Janeiro");
    }
    @AfterEach
    void restaurar() throws Exception {
        System.setIn(entrada_original);
        singleton(Ano.class);
        singleton(Banco.class);
    }
    private void singleton(Class<?> tipo) throws Exception {
        Field campo = tipo.getDeclaredField("instancia");
        campo.setAccessible(true);
        campo.set(null, null);
    }
    private CartaoCredito cartao() {
        return new CartaoCredito(123456, 123);
    }
    private CartaoCredito fatura(double valor, int mes, boolean paga) {
        return new CartaoCredito(valor, mes, paga, 1000, 123456, 123);
    }
    @Test
    void u01_cartao_novo_tem_fatura_zero() {
        assertEquals(0, cartao().getFatura(), tolerancia);
    }
    @Test
    void u02_compra_abaixo_do_limite() {
        CartaoCredito c = cartao();
        c.creditar(250);
        assertEquals(250, c.getFatura(), tolerancia);
    }
    @Test
    void u03_compra_no_limite() {
        CartaoCredito c = cartao();
        c.creditar(1000);
        assertEquals(1000, c.getFatura(), tolerancia);
    }
    @Test
    void u04_compra_acima_do_limite_nao_altera_fatura() {
        CartaoCredito c = cartao();
        c.creditar(1000.01);
        assertEquals(0, c.getFatura(), tolerancia);
    }
    @Test
    void u05_compras_acumuladas_ate_olimite() {
        CartaoCredito c = cartao();
        c.creditar(400);
        c.creditar(600);
        assertEquals(1000, c.getFatura(), tolerancia);
    }
    @Test
    void u06_compra_que_ultrapassa_limite_preserva_fatura() {
        CartaoCredito c = cartao();
        c.creditar(900);
        c.creditar(100.01);
        assertEquals(900, c.getFatura(), tolerancia);
    }
    @Test
    void u07_pagamento_parcial() {
        CartaoCredito c = fatura(500, 0, false);
        c.setFatura(-200);
        assertEquals(300, c.getFatura(), tolerancia);
    }
    @Test
    void u08_pagamento_total_impede_juros() {
        CartaoCredito c = fatura(500, 0, false);
        c.setFatura(-500);
        Ano.getInstancia().setMesAtual("Fevereiro");
        c.cobrarJurus();
        assertEquals(0, c.getFatura(), tolerancia);
        assertEquals(0, Banco.getInstancia().getReceitas(), tolerancia);
    }
    @Test
    void u09_mes_da_compra_sem_juros() {
        CartaoCredito c = cartao();
        c.creditar(100);
        c.cobrarJurus();
        assertEquals(100, c.getFatura(), tolerancia);
    }
    @Test
    void u10_fatura_paga_nao_gera_receita() {
        CartaoCredito c = fatura(0, 0, true);
        Ano.getInstancia().setMesAtual("Fevereiro");
        c.cobrarJurus();
        assertEquals(0, Banco.getInstancia().getReceitas(), tolerancia);
    }
    @Test
    void u11_ajuste_do_limite_permite_compra_maior() {
        System.setIn(new ByteArrayInputStream("1500\n".getBytes(StandardCharsets.UTF_8)));
        CartaoCredito c = cartao();
        c.ajustarLimite();
        c.creditar(1500);
        assertEquals(1500, c.getFatura(), tolerancia);
    }
    @Test
    void u12_movimentacao_registra_receita() {
        cartao().movimentacaoBancaria(25);
        assertEquals(25, Banco.getInstancia().getReceitas(), tolerancia);
    }
    @Test
    void u13_construtor_de_persistencia_preserva_fatura() {
        assertEquals(350, fatura(350, 0, false).getFatura(), tolerancia);
    }
    @Test
    @Tag("defeito")
    void u14_juros_de1275_por_cento() {
        CartaoCredito c = fatura(100, 0, false);
        Ano.getInstancia().setMesAtual("Fevereiro");
        c.cobrarJurus();
        assertEquals(112.75, c.getFatura(), tolerancia, "Hipotese de requisito: taxaJurus=12.75 representa 12,75% ao mes");
    }
    @Test
    @Tag("defeito")
    void u15_juros_na_virada_do_ano() {
        CartaoCredito c = fatura(100, 11, false);
        Ano.getInstancia().setMesAtual("Janeiro");
        c.cobrarJurus();
        assertTrue(c.getFatura() > 100, "Dezembro para janeiro deve ser um mes posterior");
    }
    @Test
    @Tag("defeito")
    void u16_compra_recusada_nao_adia_vencimento() {
        CartaoCredito c = fatura(900, 0, false);
        Ano.getInstancia().setMesAtual("Fevereiro");
        c.creditar(200);
        c.cobrarJurus();
        assertTrue(c.getFatura() > 900, "Compra recusada deve preservar o mes da fatura existente");
    }
    @Test
    @Tag("defeito")
    void u17_compra_negativa_deve_ser_rejeitada() {
        CartaoCredito c = cartao();
        assertThrows(IllegalArgumentException.class, () -> c.creditar(-100));
        assertEquals(0, c.getFatura(), tolerancia);
    }
    @Test
    @Tag("defeito")
    void u18_limite_negativo_deve_ser_rejeitado() {
        System.setIn(new ByteArrayInputStream("-1\n".getBytes(StandardCharsets.UTF_8)));
        assertThrows(IllegalArgumentException.class, () -> cartao().ajustarLimite());
    }
    @Test
    @Tag("defeito")
    void u19_juros_nao_devem_duplicar_no_mesmo_mes() {
        CartaoCredito c = fatura(100, 0, false);
        Ano.getInstancia().setMesAtual("Fevereiro");
        c.cobrarJurus();
        double primeira = c.getFatura();
        c.cobrarJurus();
        assertEquals(primeira, c.getFatura(), tolerancia);
    }
}
