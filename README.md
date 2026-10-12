# COS482 Entrega 1 WinxBank

Responsável: **Pedro Passos Farias**. Sistema: WinxBank. Classe sob teste: `CartaoCredito`. Aplicação de terminal em Java 17.

Repositório: [Sistema_Bancario_POO](https://github.com/Pedro-Passos77/Sistema_Bancario_POO)

Código de origem: [repo-software-testing-courses/Sistema_Bancario_POO](https://github.com/repo-software-testing-courses/Sistema_Bancario_POO)

## Artefatos

| Artefato | Link direto |
|---|---|
| Código original em ZIP | [original/sistema-original.zip](original/sistema-original.zip) |
| Código-fonte | [winxbank/src](winxbank/src) |
| Classe sob teste | [CartaoCredito.java](winxbank/src/br/winxbank/sistemabancario/CartaoCredito.java) |
| Suíte unitária | [CartaoCreditoTest.java](winxbank/test/br/winxbank/sistemabancario/CartaoCreditoTest.java) |
| Configuração Maven | [pom.xml](winxbank/pom.xml) |
| Plano de teste | [Google Docs](https://docs.google.com/document/d/1ZbUfH4EGb4da2JDa25mZYcJYYoeTr_uW22I-1rRWO7o/edit?usp=sharing) |
| Casos de teste e resultados | [Google Docs](https://docs.google.com/document/d/15Tl3rXBkSUz9PZY-uR96zCSwDr7nNxzXSnIafC8ULx4/edit?usp=sharing) |
| Relatório unitário completo | [testes-unitarios.txt](evidencias/testes-unitarios.txt) |
| Relatório de 13 casos sem tag defeito | [testes-sem-defeitos.txt](evidencias/testes-sem-defeitos.txt) |
| Relatórios XML da suíte completa | [evidencias/junit](evidencias/junit) |
| Relatórios XML dos casos filtrados | [evidencias/junit-sem-defeitos](evidencias/junit-sem-defeitos) |
| Complexidade | [complexidade.json](evidencias/complexidade.json) |
| Integridade dos 27 fontes | [integridade-fontes.json](evidencias/integridade-fontes.json) |
| Ambiente executado | [ambiente.txt](evidencias/ambiente.txt) |
| Log da interface | [manual-compra.txt](evidencias/manual-compra.txt) |
| Estado final da sessão manual | [manual-estado-final.json](evidencias/manual-estado-final.json) |
| Dados de teste fictícios | [fixtures/compra](fixtures/compra) |
| Cenário adicional para TestLink | [testlink-cenario.xml](docs/testlink-cenario.xml) |
| Relatos de incidentes | [issues/README.md](issues/README.md) |
| Roteiro de apresentação | [roteiro-apresentacao.md](docs/roteiro-apresentacao.md) |
| Execução interativa dos casos manuais | [executar_manual.py](scripts/executar_manual.py) |
| Execução reproduzível | [executar_testes.py](scripts/executar_testes.py) |
| Medição reproduzível | [medir_complexidade.py](scripts/medir_complexidade.py) |
| Versões e atribuições dos JARs | [ferramentas/README.md](ferramentas/README.md) |

## TestLink

O cenário WB-1 foi cadastrado na instalação local TestLink 1.9.20 e registrado com resultado Passed. Projeto WinxBank; plano Plano de Teste - Entrega 1; build Execução manual - Entrega 1. Na execução, o sistema recusou o saque de R$ 150,00 e preservou o saldo de R$ 100,00.

- [Registro original exportado do TestLink](evidencias/testlink-WB-1.pdf)
- [Log da execução do cenário WB-1](evidencias/saque/execucao-wb1.txt)
- [Dados anteriores à sessão de saque](evidencias/saque/clientes-antes.json)
- [Dados persistidos após a sessão de saque](evidencias/saque/clientes.json)

## Resultados

19 testes unitários: 13 aprovados e 6 reprovados. Cinco casos manuais: quatro aprovados e um reprovado. As falhas encontradas estão documentadas nos relatos de incidentes. Hipóteses financeiras estão identificadas nos documentos para revisão. Complexidade agregada de CartaoCredito: 12 incluindo construtores e 10 sem construtores.

## Reprodução

Com Java 17 e Python 3, execute na raiz do pacote:

```sh
python scripts/executar_testes.py
```

O retorno 1 corresponde às seis asserções reprovadas no código original. A configuração Maven também está incluída; a execução registrada utilizou ECJ e JUnit Console. Os 27 fontes Java originais estão preservados.
