from pathlib import Path
import re, json, hashlib
root = Path(__file__).resolve().parents[1]
p = root / 'winxbank/src/br/winxbank/sistemabancario/CartaoCredito.java'
s = p.read_text(encoding='utf-8')
s = re.sub(r'/\*.*?\*/|//[^\n]*|"(?:\\.|[^"\\])*"', '', s, flags=re.S)
rows = []
for m in re.finditer(r'public\s+(?:static\s+)?(?:[\w<>\[\]]+\s+)?(\w+)\s*\([^)]*\)\s*\{', s):
    depth = 1; i = m.end()
    while depth:
        if s[i] == '{': depth += 1
        elif s[i] == '}': depth -= 1
        i += 1
    decisions = re.findall(r'\b(?:if|for|while|case|catch)\b|&&|\|\||\?', s[m.end():i-1])
    rows.append({'metodo':m.group(1), 'decisoes': decisions, 'cc':1+len(decisions)})
result = {'classe':'CartaoCredito','criterio':'CC por rotina = 1 + decisoes; && e || contam separadamente; apenas rotinas declaradas; construtores incluidos no agregado', 'sha256':hashlib.sha256(p.read_bytes()).hexdigest(), 'rotinas':rows,'agregado_com_construtores':sum(x['cc'] for x in rows),'agregado_sem_construtores':sum(x['cc'] for x in rows if x['metodo']!='CartaoCredito'),'maior_cc_por_rotina':max(x['cc'] for x in rows)}
(root / 'evidencias/complexidade.json').write_text(json.dumps(result, indent=2, ensure_ascii=False), encoding='utf-8')
print(json.dumps(result, indent=2, ensure_ascii=False))
