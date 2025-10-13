# Análise Comparativa: Estudos de Paralelização em Java

## 📚 Contexto Acadêmico

Este repositório contém dois projetos desenvolvidos para a disciplina "Sistemas Paralelos e Distribuídos" do Instituto Federal de Educação, Ciência e Tecnologia do Sudeste de Minas Gerais, Campus Rio Pomba. Os projetos exploram diferentes cenários de paralelização, revelando quando e por que a paralelização pode ou não trazer benefícios de desempenho.

## 🎯 Objetivo Geral

Investigar empiricamente os benefícios e limitações da paralelização em Java através de dois estudos de caso com características distintas:

1. **Contagem de Palavras**: Operação computacionalmente leve em texto literário
2. **Processamento de Imagem**: Operação computacionalmente intensiva com filtro de blur

## 📊 Visão Geral dos Projetos

### Projeto 1: Contagem de Palavras
**Caminho**: `Atividade_Avaliativa/`

**Descrição**: Análise de desempenho entre implementações sequencial e paralela para contagem de palavras em texto do Projeto Gutenberg.

**Características**:
- Operação: Contagem de ocorrências de palavras específicas
- Complexidade: Leve (comparação de strings)
- Dataset: "Clarissa Harlowe" de Samuel Richardson
- Implementações: Sequencial vs Paralela (2, 4, 8 threads)

**Resultado Chave**: ⚠️ **Speedup < 1,0x** - A versão paralela foi mais lenta

### Projeto 2: Processamento de Imagem com Blur
**Caminho**: `Tarefa_Final/`

**Descrição**: Comparação entre cinco implementações (1 sequencial + 4 paralelas) aplicando filtro de blur em imagens com OpenCV.

**Características**:
- Operação: Filtro de blur com kernel 111x111
- Complexidade: Alta (operações de convolução)
- Implementações: Sequencial, Threads Simples, Thread Pool, ForkJoin, Virtual Threads
- Configurações: 2, 4, 8 threads

**Resultado Chave**: ⚖️ **Speedup ≈ 1,0x** - Performance equivalente entre sequencial e paralelas

## 📈 Comparação de Resultados

| Aspecto | Contagem de Palavras | Processamento de Imagem |
|---------|---------------------|-------------------------|
| **Operação** | Comparação de strings | Convolução matemática (blur) |
| **Complexidade** | Leve (~64ms) | Média (~2100ms) |
| **Tempo Sequencial** | 64ms | 2122ms |
| **Speedup (2 threads)** | 0,65x (pior) | 1,00x (igual) |
| **Speedup (4 threads)** | 0,62x (pior) | 1,01x (igual) |
| **Speedup (8 threads)** | 0,58x (pior) | 1,01x (igual) |
| **Fator Limitante** | Overhead de threads | I/O domina tempo total |
| **Gargalo Principal** | Sincronização (AtomicInteger) | Carregamento de imagem (~85%) |
| **Lição Aprendida** | Paralelizar tarefas leves piora desempenho | I/O sequencial mascara ganhos paralelos |

## 🔍 Análise Consolidada

### Por Que a Paralelização Não Trouxe Ganhos?

#### Projeto 1: Overhead Supera os Ganhos
- **Problema**: A operação de contagem é muito rápida (nanossegundos por palavra)
- **Overhead**: Criar threads, sincronizar AtomicInteger, gerenciar memória compartilhada
- **Diferença arquitetural**: Versão sequencial usa streaming; paralela carrega tudo em memória
- **Resultado**: O custo de paralelização > benefício do processamento paralelo

#### Projeto 2: I/O Domina o Tempo Total
- **Problema**: ~85-90% do tempo é gasto em I/O (carregar/salvar imagem)
- **Lei de Amdahl**: Com apenas 10-15% paralelizável, speedup máximo teórico é ~1,13x
- **Evidência**: Todas as 4 implementações paralelas têm desempenho similar (~2100-2170ms)
- **Resultado**: A paralelização funciona corretamente, mas seu impacto é marginal

### Padrões Identificados

| Padrão | Descrição | Ocorre Quando |
|--------|-----------|---------------|
| **Overhead > Ganho** | Custo de paralelização supera benefícios | Operações muito leves |
| **I/O Dominante** | Tempo em I/O mascara ganhos de CPU | I/O representa >80% do tempo |
| **Contenção de Recursos** | Threads competem por recursos compartilhados | Uso intenso de sincronização |
| **Lei de Amdahl** | Porção sequencial limita speedup máximo | Grande parte do código não paralelizável |

## 🧠 Lições Aprendidas

### 1. Profiling é Essencial
**Contexto**: Ambos os projetos revelaram que as suposições iniciais sobre gargalos estavam erradas.

**Lição Prática**: Sempre meça onde o tempo é realmente gasto antes de otimizar.

### 2. Nem Toda Tarefa Se Beneficia de Paralelização
**Contagem de Palavras**: Operação muito leve - overhead de threads domina  
**Processamento de Imagem**: Operação pesada, mas I/O domina o tempo total

**Lição Prática**: Avalie a natureza completa do problema, não apenas a operação central.

### 3. Lei de Amdahl é Implacável
Em ambos os projetos, porções sequenciais limitaram severamente os ganhos:
- **Projeto 1**: Overhead de criação/sincronização de threads
- **Projeto 2**: I/O sequencial (85-90% do tempo)

**Lição Prática**: Identifique e minimize porções sequenciais antes de paralelizar.

### 4. A Implementação Pode Estar Correta Mesmo Sem Speedup
**Evidências de Correção**:
- Baixa variabilidade nos resultados
- Consistência entre diferentes frameworks paralelos
- Resultados funcionalmente corretos

**Lição Prática**: Diferencie "implementação incorreta" de "paralelização não benéfica".

### 5. Resultados Negativos São Valiosos
Estes projetos demonstram quando **não** usar paralelização, o que é tão valioso quanto saber quando usar.

**Lição Prática**: Documente e aprenda com resultados negativos.

## 🎓 Valor Educacional

### O Que Estes Projetos Ensinam

1. **Teoria vs Prática**: Conceitos teóricos (Lei de Amdahl, overhead de threads) manifestados empiricamente
2. **Pensamento Crítico**: Questionar suposições e interpretar resultados contraintuitivos
3. **Design de Benchmarks**: Importância de isolar componentes e eliminar vieses
4. **Metodologia Científica**: Medição rigorosa, remoção de outliers, análise estatística
5. **Contexto Real**: Sistemas reais têm múltiplos componentes (I/O, processamento, comunicação)

### Cenários Ideais para Paralelização

Com base nos dois projetos, paralelização é efetiva quando:

✅ **A operação é computacionalmente intensiva**  
✅ **Os dados podem ser divididos independentemente**  
✅ **A porção paralelizável domina o tempo total (>50%)**  
✅ **O custo de sincronização é baixo**  
✅ **O hardware tem núcleos suficientes e balanceados**

Evite paralelização quando:

❌ **A operação é muito rápida (microsegundos)**  
❌ **I/O domina o tempo total**  
❌ **Requer sincronização frequente**  
❌ **A complexidade adicional não se justifica**  
❌ **Não há medições confirmando o gargalo**

## 🛠️ Tecnologias Utilizadas

### Comuns aos Dois Projetos
- Java 17+
- Apache Commons Math (estatísticas)
- Lombok (redução de boilerplate)
- Maven (build e dependências)

### Específicas
**Contagem de Palavras**: Apache PDFBox  
**Processamento de Imagem**: OpenCV, JavaCV (Bytedeco)

## 📂 Estrutura do Repositório

```
.
├── Atividade_Avaliativa/          # Projeto 1: Contagem de Palavras
│   ├── initialize/                 # Coordenador do benchmark
│   ├── sequencial/                 # Implementação sequencial
│   ├── paralelo/                   # Implementação paralela
│   └── README.md                   # Documentação detalhada
│
├── Tarefa_Final/                   # Projeto 2: Processamento de Imagem
│   ├── initialize/                 # Coordenador do benchmark
│   ├── sequencial/                 # Implementação sequencial
│   ├── threadsimples/              # Threads básicas
│   ├── threadpool/                 # ExecutorService
│   ├── forkjoin/                   # ForkJoin Framework
│   ├── threadvirtual/              # Virtual Threads (Java 19+)
│   └── README.md                   # Documentação detalhada
│
└── README.md                       # Este arquivo (visão geral)
```

## 🚀 Como Executar

### Pré-requisitos
- Java JDK 17+ (Java 19+ para Virtual Threads no Projeto 2)
- Maven 3.6+

### Executando Projeto 1 (Contagem de Palavras)
```bash
cd Atividade_Avaliativa
cd initialize
mvn clean package
java -jar target/initialize-1.0-SNAPSHOT.jar
```

### Executando Projeto 2 (Processamento de Imagem)
```bash
cd Tarefa_Final
cd initialize

# IMPORTANTE: Ajuste o caminho base em Programas.java antes!
mvn clean package
java -jar target/initialize-1.0-SNAPSHOT-jar-with-dependencies.jar
```

**Consulte os READMEs individuais** de cada projeto para instruções detalhadas.

## 📊 Interpretação dos Resultados

### Métricas Utilizadas

**Speedup**: `Tempo Sequencial / Tempo Paralelo`
- \> 1: Paralelização benéfica
- = 1: Performance equivalente
- < 1: Paralelização prejudicial

**Eficiência**: `Speedup / Número de Threads × 100%`
- 100%: Ideal (speedup linear)
- 50-100%: Boa utilização
- < 50%: Utilização subótima

### O Que Observamos

**Projeto 1**: Speedup 0,55-0,65x → Overhead domina  
**Projeto 2**: Speedup 0,97-1,01x → I/O mascara ganhos

Ambos demonstram limitações reais da paralelização em contextos diferentes.

## 📝 Conclusões Finais

### Síntese dos Aprendizados

1. **Paralelização não é solução universal**: Contexto e natureza do problema determinam sua eficácia

2. **Medição é fundamental**: Profiling revela gargalos reais, não supostos

3. **Lei de Amdahl governa ganhos**: Porções sequenciais impõem limites teóricos rígidos

4. **I/O frequentemente domina**: Em aplicações reais, otimizar I/O pode ser mais valioso que CPU

5. **Complexidade tem custo**: Só adicione paralelização quando ganhos justificam a complexidade

### Quando Usar Paralelização?

Com base na experiência destes projetos:

**✅ USE quando:**
- A operação é computacionalmente intensiva (>100ms por item)
- Os dados são independentes (pouca ou nenhuma sincronização)
- A porção paralelizável é significativa (>50% do tempo)
- O profiling confirma que processamento é o gargalo
- O hardware disponível suporta paralelismo efetivo

**❌ EVITE quando:**
- A operação é muito rápida (<1ms por item)
- I/O ou comunicação dominam o tempo total
- Requer sincronização frequente (locks, atomic operations)
- A implementação sequencial já é suficientemente rápida
- Não há medições confirmando necessidade

### Valor Pedagógico

Estes projetos demonstram que **falhar em obter speedup é tão educacional quanto obter sucesso**. Os resultados:
- Validam teoria (Lei de Amdahl, overhead)
- Ensinam sobre metodologia científica
- Desenvolvem pensamento crítico
- Preparam para desafios reais de otimização

## 📚 Referências Principais

1. Amdahl, G. M. (1967). *Validity of the single processor approach to achieving large scale computing capabilities*. AFIPS Conference Proceedings.

2. Goetz, B., et al. (2006). *Java Concurrency in Practice*. Addison-Wesley Professional.

3. Herlihy, M., & Shavit, N. (2012). *The Art of Multiprocessor Programming*. Morgan Kaufmann.

4. McCool, M. D., Robison, A. D., & Reinders, J. (2012). *Structured Parallel Programming: Patterns for Efficient Computation*. Morgan Kaufmann.

## 📄 Licença

Este projeto está licenciado sob a [Licença MIT](LICENSE).

## 👥 Autor

Desenvolvido para a disciplina de Sistemas Paralelos e Distribuídos  
**Instituto Federal de Educação, Ciência e Tecnologia do Sudeste de Minas Gerais**  
**Campus Rio Pomba**

---

<div align="center">
  <p><strong>Dois Projetos. Duas Lições. Uma Verdade:</strong></p>
  <p><em>Nem sempre mais threads significam mais velocidade</em></p>
  <p>⚡ Meça. Analise. Otimize com Sabedoria. ⚡</p>
</div>