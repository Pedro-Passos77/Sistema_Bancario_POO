from pathlib import Path
import os, subprocess, shutil, tempfile
root = Path(__file__).resolve().parents[1]
classes = root / 'build/classes'
if not (classes / 'br/winxbank/Main.class').exists():
    raise SystemExit('Execute primeiro python scripts/executar_testes.py para compilar. As seis falhas do original sao esperadas.')
with tempfile.TemporaryDirectory(prefix='winxbank-manual-') as directory:
    for name in ['clientes.json', 'mesAtual.txt', 'banco.txt']:
        shutil.copyfile(root / 'fixtures/compra' / name, Path(directory) / name)
    cp = os.pathsep.join([str(classes)] + [str(p) for p in (root / 'build/lib').glob('*.jar')])
    subprocess.run(['java', '-cp', cp, 'br.winxbank.Main'], cwd=directory, check=True)
