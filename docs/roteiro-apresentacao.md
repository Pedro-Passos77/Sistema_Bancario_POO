# Roteiro da Entrega 1

Responsável: Pedro Passos Farias. Duração planejada: 15 a 18 minutos.

1. Sistema e escopo (2 min): WinxBank Java de terminal; classe CartaoCredito e fluxos de compra e pagamento.
2. Escolha da classe (2 min): lógica de limite, quitação e juros; complexidade agregada 12 com construtores, 10 sem; máximo por método 3. Explicar a contagem de && e evitar dizer que há método com CC 12.
3. Plano (3 min): regras R01 a R06 e hipóteses H01 a H03, fronteiras e dependências.
4. Unitários (4 min): executar suíte completa; mostrar 19 testes, 13 aprovados e 6 reprovados. Explicar isolamento de singletons e a diferença entre falha do teste e erro do ambiente.
5. Manuais (3 min): mostrar resultados dos cinco casos; compra de 1100 recusada na fatura mas confirmada na interface e lançada no extrato. Mostrar o PDF do TestLink com WB-1 registrado como Passed e a recusa do saque de R$ 150,00 com saldo de R$ 100,00.
6. Problemas e encerramento (2 min): mostrar issues efetivamente publicadas; distinguir requisito inferido de aprovado; confirmar links do Google Docs e a branch principal.

Perguntas para preparar: qual a diferença entre CC por método e agregada? Por que testar exatamente o limite? Por que não trocar asserções para fazer um defeito passar? Como as dependências são restauradas? Qual o oráculo para 12,75%? Como janeiro é posterior a dezembro? Como o caso WB-1 foi registrado no TestLink?
