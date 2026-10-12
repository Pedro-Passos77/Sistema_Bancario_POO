# Casos de teste e resultados do WinxBank

## Identificação e escopo

CT WINXBANK T1 001 | Versão 1.0 | 12 de outubro de 2026
Responsável: Pedro Passos Farias. Este documento reúne os casos unitários e os casos manuais de CartaoCredito, compra no crédito e pagamento de fatura. Os resultados correspondem à execução assistida real registrada no pacote. O caso WB-1 está registrado na instalação local do TestLink, com status Passed, conforme PDF incluído no pacote.

## Requisitos e hipóteses de teste

R01: a compra aceita acumula a fatura sem superar o limite. R02: compra acima do limite preserva fatura e mês de origem e informa recusa. R03: pagamento parcial reduz a fatura; pagamento total quita. R04: fatura paga ou mês de origem não deve gerar juros. R05: janeiro após dezembro representa um mês posterior. R06: a receita do banco registra o acréscimo por juros.
H01: taxaJurus de 12.75 representa 12,75% mensais, resultando em fatura × 1,1275. H02: compra e limite negativos devem ser rejeitados por IllegalArgumentException. H03: a mesma fatura não recebe juros duas vezes no mesmo mês. H01 a H03 precisam de validação de contrato; não são especificações já aprovadas.

## Casos unitários

Cada teste reinicia Ano e Banco e estabelece janeiro como mês base, salvo quando indicado. Os testes da interface de ajuste de limite fornecem texto para System.in e restauram a entrada. Comparações financeiras usam tolerância absoluta de 0,00000001. Os casos podem ser executados em qualquer ordem.

## Execução e reprodução

Execução completa: python scripts/executar_testes.py. Retorno 1 é esperado no fonte original, pois há seis asserções reprovadas. Execução apenas dos cenários sem tag defeito: python scripts/executar_testes.py --sem-defeitos, com 13 aprovações. O segundo comando não substitui a execução completa nem deve ser usado para ocultar falhas.
Em ambiente com JDK 17 e Maven: cd winxbank e mvn clean test. A execução registrada usou ECJ e JUnit Console. O projeto também inclui configuração Maven. Evidências: evidencias/testes-unitarios.txt, evidencias/testes-sem-defeitos.txt e relatórios XML de JUnit. Não foram calculados percentuais de cobertura ou escore de mutação nesta entrega.

## Casos manuais e pré condições

Os quatro casos abaixo pertencem à mesma sessão e devem ser executados na ordem M01, M02, M03 e M04. Restaurar os três arquivos de fixtures/compra na pasta de execução antes da sessão. Com JDK 17 e Maven, compilar o projeto e executar Main a partir dessa pasta de dados, com as classes e dependências no classpath.
Selecionar 2 para login e informar CPF 00000000000. A primeira interação muda o mês de Janeiro para Fevereiro e desconta 13 do saldo inicial. Pré-condição de M01 após login: conta 12345, saldo 4987, limite 1000 e fatura zero. Enviar uma resposta por prompt, pois diferentes Scanner podem consumir dados antecipados quando todo o roteiro é colado de uma vez.
Evidência comum: evidencias/manual-compra.txt. Dados são fictícios. Execução: interação assistida no terminal, em 12 de outubro de 2026; os resultados de M01 a M04 estão no log da sessão.

## M01 Compra no crédito abaixo do limite

Passos: selecionar 7; informar conta 12345; informar valor 250; selecionar 2 para crédito; confirmar com 1.
Esperado: mensagem de sucesso, fatura 250 e saldo 4987, pois compra no crédito não debita o saldo. Observado: Valor creditado, fatura 250 e saldo 4987. Resultado: aprovado. Requisitos: R01.

## M02 Pagamento parcial

Pré-condição: resultado de M01, fatura 250 e saldo 4987. Passos: selecionar 10; informar conta 12345; informar pagamento 100.
Esperado: fatura 150 e saldo 4887. Observado: fatura 150 e saldo 4887. Resultado: aprovado. Requisito: R03.

## M03 Pagamento total

Pré-condição: resultado de M02, fatura 150 e saldo 4887. Passos: selecionar 10; informar conta 12345; informar pagamento 150.
Esperado: fatura zero e saldo 4737. Observado: fatura zero e saldo 4737. Resultado: aprovado. Requisito: R03.

## M04 Tentativa de compra acima do limite

Pré-condição: resultado de M03, fatura zero, saldo 4737 e limite 1000. Passos: selecionar 7; informar conta 12345; informar compra 1100; selecionar 2 para crédito; confirmar com 1.
Esperado: recusa explícita, fatura zero e ausência de movimentação de compra aceita. Observado: mensagem Valor creditado, fatura zero e criação de movimentação de saída de 1100 no extrato. A divergência de mensagem aparece no log; a movimentação é confirmada no JSON persistido após selecionar 0 para sair. Resultado: reprovado. Requisito R02; incidente I07.

## M05 Saque acima do saldo disponível

WB-1 Saque acima do saldo disponível. Conta 12345 com saldo de R$ 100,00 após o login; tentativa de saque de R$ 150,00 pelo menu 9. O sistema exibiu “O valor solicitado e maior que o saldo da conta.” e manteve o saldo de R$ 100,00. Resultado: aprovado. Execução assistida do programa original com Java 17, em 12/10/2026. O registro no TestLink 1.9.20 foi realizado na instalação local do integrante, projeto WinxBank, suíte Casos manuais - Entrega 1, plano Plano de Teste - Entrega 1 e build Execução manual - Entrega 1; usuário admin, status Passed. Evidências: evidencias/testlink-WB-1.pdf e evidencias/saque/execucao-wb1.txt. 

## Cenário para TestLink

O cenário WB-1 foi cadastrado e associado ao plano e ao build no TestLink 1.9.20. O registro salvo apresenta status Passed. O PDF original exportado pelo integrante está em evidencias/testlink-WB-1.pdf. 

## Resumo dos resultados

Unitários: 19 executados, 13 aprovados, 6 reprovados, nenhum bloqueado ou ignorado. Manuais: 5 executados na interface, 4 aprovados e 1 reprovado. Foram preparados sete relatos de incidentes, ainda não publicados. 

## Pendências acadêmicas

Revisar hipóteses H01 a H03; registrar execução própria e contribuição verificável; criar fork do grupo; publicar todos os artefatos em main ou master; importar documentos no Google Docs com conta própria; publicar issues e substituir os links pendentes no README. 

## Matriz de casos unitários

| Caso | Regra | Entrada | Esperado | Observado | Resultado |
|---|---|---|---|---|---|
| U01 | R01 | Novo cartão | Fatura 0 | 0 | Aprovado |
| U02 | R01 | Compra 250 | Fatura 250 | 250 | Aprovado |
| U03 | R01 | Compra 1000 | Fatura 1000 | 1000 | Aprovado |
| U04 | R02 | Compra 1000,01 | Fatura 0 | 0 | Aprovado |
| U05 | R01 | Compras 400 e 600 | Fatura 1000 | 1000 | Aprovado |
| U06 | R02 | Compras 900 e 100,01 | Fatura 900 | 900 | Aprovado |
| U07 | R03 | Fatura 500; pagamento 200 | Fatura 300 | 300 | Aprovado |
| U08 | R03 R04 | Fatura 500; pagar 500; fevereiro | Fatura e receita 0 | 0 e 0 | Aprovado |
| U09 | R04 | Compra 100 em janeiro; juros em janeiro | Fatura 100 | 100 | Aprovado |
| U10 | R04 | Fatura paga; fevereiro | Receita 0 | 0 | Aprovado |
| U11 | R01 | Limite 1500; compra 1500 | Fatura 1500 | 1500 | Aprovado |
| U12 | R06 | Movimentação de receita 25 | Receita 25 | 25 | Aprovado |
| U13 | R01 | Construção com fatura 350 | Fatura 350 | 350 | Aprovado |
| U14 | H01 | Fatura 100; janeiro para fevereiro | 112,75 | 1275 | Reprovado I01 |
| U15 | R05 | Fatura 100; dezembro para janeiro | Maior que 100 | 100 | Reprovado I02 |
| U16 | R02 | Fatura 900 janeiro; fevereiro; recusar 200 | Juros continuam elegíveis | Fatura 900 | Reprovado I03 |
| U17 | H02 | Compra -100 | Exceção; fatura 0 | Sem exceção | Reprovado I04 |
| U18 | H02 | Ajustar limite -1 | Exceção | Sem exceção | Reprovado I05 |
| U19 | H03 | Duas chamadas de juros em fevereiro | Segunda chamada preserva fatura | 1275 para 16256,25 | Reprovado I06 |
