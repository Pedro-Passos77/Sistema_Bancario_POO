# Plano de teste do WinxBank

## Identificação do plano

PT WINXBANK T1 001 | Versão 1.0 | 12 de outubro de 2026
COS482 Qualidade e Teste de Software | Entrega 1
Responsável: Pedro Passos Farias. Sistema escolhido: WinxBank. Escolha aprovada pelo professor. Repositório de origem: https://github.com/repo-software-testing-courses/Sistema_Bancario_POO. 

## 1 Introdução

Este plano delimita os testes da classe CartaoCredito do WinxBank e dos fluxos de compra no crédito, pagamento de fatura e recusa de saque acima do saldo pela interface de terminal. O objetivo é verificar as regras financeiras e registrar problemas reproduzíveis no código original. O plano segue as seções do template fornecido em aula e acrescenta identificação, critérios de aprovação e reprovação, riscos e aprovações.
A referência de código é o ZIP original recebido, cujo comentário de arquivo identifica o commit 143cf1564bdeb4f27ce862c5e5b5876b1e29d49a. Os 27 arquivos Java da cópia de trabalho permanecem idênticos aos do ZIP. O pom.xml foi ajustado para declarar as raízes src e test e configurar JUnit 5; o original permanece no arquivo original/sistema-original.zip.

## 1 1 Escopo

No escopo: construtores, creditar, ajustarLimite, setFatura, getFatura, cobrarJurus e movimentacaoBancaria da classe CartaoCredito; limite individual e acumulado; redução e quitação da fatura; bloqueio de juros no mês de origem ou em fatura paga; passagem de mês; registro de receitas e entradas inválidas. Na interface, compra no crédito, pagamento parcial, pagamento total e tentativa acima do limite e saque acima do saldo.
Ano e Banco são dependências reais para os testes de CartaoCredito. ContaCorrente, Main e os repositórios são utilizados apenas na execução manual. Não serão apresentados como classes unitárias adicionais.
Fora do escopo: cadastro como objeto principal de teste, PIX, empréstimos, poupança, pontos de fidelidade, documentos financeiros, segurança, desempenho e testes de integração formal. Mockito, cobertura de arestas, mutação, ISO/IEC 25010, E2E e inspeção com SonarQube pertencem à Entrega 2.

## 1 2 Objetivos de qualidade

Verificar que compras válidas respeitam o limite; compras recusadas não alteram dívida ou vencimento; pagamentos reduzem corretamente a fatura; contas quitadas não geram juros; a virada de ano não impede reconhecer atraso; valores financeiros inválidos são recusados; e a interface informa corretamente o resultado da operação.
A complexidade agregada da classe é 12, incluindo dois construtores, e 10 sem construtores. A maior complexidade de uma rotina é 3. A métrica usada é a soma de CC das rotinas declaradas, com decisões de curto circuito contadas separadamente. Isso não equivale a afirmar que um método possui CC 12. Evidência e script: evidencias/complexidade.json e scripts/medir_complexidade.py.

## 1 3 Papéis e responsabilidades

Pedro Passos Farias responde pela revisão do escopo, validação dos requisitos, execução e registro dos testes, criação do fork, publicação de issues e artefatos, importação do cenário no TestLink, manutenção dos links do README e apresentação individual.

## 2 Metodologia de teste

A abordagem é incremental. Primeiro são identificadas regras e fronteiras, depois são construídos testes unitários e casos manuais, e por fim os resultados são confrontados com os critérios esperados e relacionados aos incidentes. O código de produção original é preservado nesta entrega para manter a reprodução dos defeitos.

## 2 1 Visão geral

A técnica funcional usa classes de equivalência e valores de fronteira: compra menor que o limite, igual ao limite e maior que o limite; fatura vazia, parcialmente paga e quitada; mês igual ao de origem, posterior e janeiro após dezembro.
Os testes JUnit executam cada cenário com novas instâncias de cartão. As instâncias únicas de Ano e Banco são reiniciadas por reflexão antes e depois de cada teste, sem alteração do fonte e sem execução paralela. System.in é substituído apenas nos testes de ajuste de limite e restaurado ao final. As dependências são reais nesta etapa; Mockito será considerado na Entrega 2.

## 2 2 Fases de teste

Teste unitário: 19 cenários em CartaoCreditoTest, identificados U01 a U19. A execução completa registrou 13 aprovações e 6 reprovações. Os seis cenários que expõem defeitos possuem a tag defeito; permanecem habilitados na execução padrão.
Teste manual: quatro casos de compra e pagamento executados sequencialmente e um caso isolado de saque por interação assistida. Quatro passaram; um falhou na mensagem de confirmação de compra acima do limite. O log está em evidencias/manual-compra.txt. O documento Casos de teste e resultados especifica os passos e a sequência necessária.
WB-1 Saque acima do saldo disponível. Conta 12345 com saldo de R$ 100,00 após o login; tentativa de saque de R$ 150,00 pelo menu 9. O sistema exibiu “O valor solicitado e maior que o saldo da conta.” e manteve o saldo de R$ 100,00. Resultado: aprovado. Execução assistida do programa original com Java 17, em 12/10/2026. O registro no TestLink 1.9.20 foi realizado na instalação local do integrante, projeto WinxBank, suíte Casos manuais - Entrega 1, plano Plano de Teste - Entrega 1 e build Execução manual - Entrega 1; usuário admin, status Passed. Evidências: evidencias/testlink-WB-1.pdf e evidencias/saque/execucao-wb1.txt. 

## 2 3 Triagem de erros

Cada incidente recebe identificador, título, ambiente, pré-condições, passos, resultado esperado, resultado observado, impacto e teste relacionado. A prioridade considera impacto financeiro e possibilidade de repetição. Os sete relatos em issues são textos preparados para publicação no bug tracker do fork; ainda não são issues publicadas.
A interpretação da constante taxaJurus como percentual de 12,75% ao mês é hipótese de requisito que precisa de validação. A política de rejeição por IllegalArgumentException para valores negativos e a cobrança única de juros por mês também são propostas explícitas de contrato. Uma falha de hipótese não será declarada violação de especificação aprovada antes da confirmação do responsável.

## 2 4 Critérios de suspensão e requisitos de retomada

Suspender a execução se a compilação falhar, faltar uma dependência, os arquivos de dados não puderem ser restaurados ou houver mistura de estado entre cenários. Registrar a causa como bloqueio, e não como aprovação ou defeito do cartão. Retomar após corrigir o ambiente e restaurar as pré-condições. Uma falha funcional não suspende os demais casos independentes.

## 2 5 Completude do teste

Um caso passa quando todas as asserções ou verificações manuais correspondem ao resultado esperado. Reprova quando ocorre divergência; é bloqueado quando o ambiente impede sua execução.
A etapa técnica está concluída quando os 19 testes unitários e os cinco casos manuais têm resultados e evidências, todos os incidentes têm relatos reproduzíveis e os fontes originais estão preservados. A entrega acadêmica requer ainda: issues publicadas, fork e artefatos na branch principal, documentos no Google Docs com histórico próprio, links diretos no README e envio pelo Moodle. Encontrar defeitos não significa que todos os testes devam passar; a suíte completa deve revelar as divergências do original.

## 2 6 Atividades estimativas e cronograma

Preparação de ambiente e medição: 1 hora estimada. Projeto e execução unitária: 2 horas. Casos manuais e triagem: 1 hora. Revisão dos documentos e publicação: 1 hora. Preparação da apresentação: 1 hora. Estimativas não representam horas efetivamente trabalhadas.
Entrega 1: 11 de outubro de 2026, às 23h59. Apresentações: 13 e 15 de outubro de 2026.

## 3 Entregáveis de teste

Plano de teste editável; casos e resultados editáveis; código original em ZIP e fontes na cópia de trabalho; configuração Maven; suíte JUnit; relatório de execução em texto e XML; medição de complexidade e script; verificação de integridade dos fontes; dados sintéticos para os casos manuais; log da interface; registro PDF de execução no TestLink e cenário XML opcional; sete relatos de incidentes; README com índice dos artefatos e roteiro de apresentação.
Documentos de texto devem ser importados para Google Docs pelo integrante e seus links incluídos no README. As cópias DOCX e Markdown deste pacote são materiais para revisão e importação; não substituem a exigência de histórico no Google Docs.

## 4 Necessidades de recursos e ambiente

O projeto é uma aplicação de terminal em Java 17, sem interface web. O ambiente utilizado para a execução foi Linux com OpenJDK Runtime 17.0.20. Os fontes foram compilados pelo Eclipse Compiler for Java 3.38.0, porque o ambiente disponibiliza java sem javac; JUnit Platform Console 1.10.2 executou JUnit Jupiter.
O script scripts/executar_testes.py reproduz a execução com Java 17 e Python 3, usando os JARs de compilador e JUnit incluídos e as dependências do JAR original. Maven é a opção de desenvolvimento com JDK 17 completo. A execução Maven não foi validada neste ambiente. Não se requer GPU ou serviço de banco de dados.

## 4 1 Ferramentas de teste

JUnit Jupiter 5.10.2 para asserções e casos unitários; JUnit Platform Console 1.10.2 para relatórios; ECJ 3.38.0 para compilação local; Maven Compiler 3.11.0 e Surefire 3.2.5 na configuração de desenvolvimento; script de contagem para complexidade agregada; GitHub Issues como tracker planejado; TestLink para ao menos um cenário; Google Docs para os documentos acadêmicos.

## 4 2 Ambiente de teste

Cada execução manual deve usar uma pasta de dados isolada. O conjunto fixtures/compra contém CPF fictício 00000000000, conta 12345, saldo inicial 5000, fatura zero e limite 1000. O arquivo do mês inicia em Janeiro; ao selecionar login, a primeira chamada automática do programa avança para Fevereiro e desconta taxa de manutenção de 13. Por isso, o saldo observado antes da compra é 4987.
O banco.txt binário foi reaproveitado do original para compatibilidade de serialização; seus totais não são usados como oráculo dos casos manuais. As receitas dos testes unitários começam em zero após reiniciar Banco. Não utilizar dados pessoais reais.

## 5 Termos e acrônimos

SUT: software sob teste. CC: complexidade ciclomática de uma rotina. Complexidade agregada: soma das CC das rotinas declaradas na classe. Oráculo: critério usado para decidir o resultado esperado. Caso aprovado: resultado correspondente ao esperado. Incidente: ocorrência que exige investigação. T1: Entrega 1.

## 6 Riscos e contingências

Estado global de singletons pode contaminar testes: reiniciar Ano e Banco por caso e impedir paralelismo. Múltiplos Scanner sobre System.in podem consumir entradas antecipadamente: executar manualmente uma resposta por prompt. Avanço automático de mês pode alterar saldo e juros: registrar a sequência e restaurar fixtures.
Divergência de interpretação da complexidade: declarar CC agregada 12 e CC máxima 3, sem confundir com complexidade de um método. Requisitos financeiros incompletos: registrar hipóteses e confirmar contratos antes de classificar como violações definitivas. 

## 7 Aprovações

Responsável pela revisão e execução acadêmica: Pedro Passos Farias. Aprovação do plano: pendente de revisão pelo integrante. Aprovação de hipóteses financeiras: pendente. Aprovação de escolha do sistema: informada pelo integrante. 

## Referências

TemplatePlanoDeTeste.doc fornecido em aula, com atribuição ao Guru99. IEEE Std 829-1998, seção 4, como referência complementar de conteúdo. Enunciado COS482 2026.2 fornecido pelo integrante. Código-fonte original WinxBank recebido em ZIP. Documentação das versões de ferramentas declaradas na seção 4.
