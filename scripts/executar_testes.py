from pathlib import Path
import zipfile, subprocess, os, sys, io
root = Path(__file__).resolve().parents[1]
build = root / 'build'
(build / 'lib').mkdir(parents=True, exist_ok=True)
with zipfile.ZipFile(root / 'original/sistema-original.zip') as original:
    with zipfile.ZipFile(io.BytesIO(original.read('Sistema_Bancario_POO-main/winxbank/target/winxbank-1.0.jar'))) as boot:
        for name in boot.namelist():
            if name.startswith('BOOT-INF/lib/') and name.endswith('.jar'):
                (build / 'lib' / Path(name).name).write_bytes(boot.read(name))
for part in ['classes', 'test-classes']: (build / part).mkdir(exist_ok=True)
ecj = root / 'ferramentas/ecj-3.38.0.jar'
junit = root / 'ferramentas/junit-platform-console-standalone-1.10.2.jar'
cp = os.pathsep.join(str(p) for p in (build / 'lib').glob('*.jar'))
subprocess.run(['java','-jar',str(ecj),'-17','-nowarn','-encoding','UTF-8','-cp',cp,'-d',str(build / 'classes')] + [str(p) for p in (root / 'winxbank/src').rglob('*.java')], check=True)
subprocess.run(['java','-jar',str(ecj),'-17','-nowarn','-encoding','UTF-8','-cp',os.pathsep.join([str(build / 'classes'),str(junit)]),'-d',str(build / 'test-classes')] + [str(p) for p in (root / 'winxbank/test').rglob('*.java')], check=True)
args = ['java','-jar',str(junit),'execute','--class-path',os.pathsep.join([str(build / 'classes'), str(build / 'test-classes')]),'--select-class','br.winxbank.sistemabancario.CartaoCreditoTest','--disable-ansi-colors','--reports-dir',str(root / ('evidencias/junit-sem-defeitos' if '--sem-defeitos' in sys.argv else 'evidencias/junit'))]
if '--sem-defeitos' in sys.argv: args += ['--exclude-tag','defeito']
result = subprocess.run(args, capture_output=True, text=True)
name = 'testes-sem-defeitos.txt' if '--sem-defeitos' in sys.argv else 'testes-unitarios.txt'
(root / 'evidencias' / name).write_text(result.stdout + result.stderr, encoding='utf-8')
print(result.stdout + result.stderr)
sys.exit(result.returncode)
