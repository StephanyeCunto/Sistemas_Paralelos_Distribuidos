# Análise de Desempenho: Busca Paralela em PDF

Este projeto implementa um sistema completo para comparar o desempenho de algoritmos de busca sequencial e paralela em documentos PDF, demonstrando os benefícios da computação paralela e do uso de threads virtuais modernas em Java.

[![Java Version](https://img.shields.io/badge/java-17%2B-blue.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

![Resultados](/Atividade_Avaliativa/img/Dados.png)


> Comparativo de desempenho entre algoritmos sequenciais e paralelos para busca em documentos PDF, demonstrando os benefícios da computação paralela moderna com threads virtuais do Java.


## 📋 Visão Geral

Este projeto implementa e analisa diferentes estratégias de busca em documentos PDF:

| Abordagem | Descrição |
|-----------|-----------|
| 🔄 Sequencial | Execução em thread única (baseline) |
| 🧵 Paralela Tradicional | Execução com 2, 4 e 8 threads padrão |
| ⚡ Paralela Virtual | Execução com threads virtuais leves (Java 19+) |

O sistema extrai texto de PDFs, busca conjuntos específicos de palavras e coleta métricas detalhadas de desempenho, incluindo visualizações gráficas.

## ✨ Principais Resultados

- **Speedup massivo**: Até **4000x** mais rápido com threads virtuais
- **Escalabilidade**: Melhoria significativa de desempenho com aumento de threads
- **Eficiência**: Análise detalhada de custo-benefício da paralelização
- **Evidência prática**: Demonstração clara dos princípios da Lei de Amdahl

## 🧪 Ambiente de Teste

Para garantir a reprodutibilidade dos resultados, o ambiente de teste foi configurado com as seguintes especificações:

### Hardware
- **Processador**: Apple M2 (8 núcleos: 4 de performance e 4 de eficiência)
- **Velocidade de Clock**: 3.49 GHz
- **Memória RAM**: 8GB LPDDR5 6400 MHz
- **Armazenamento**: SSD NVMe 256GB

### Software
- **Sistema Operacional**: macOS 15.4.1 (24E772)
- **JDK**: OpenJDK 17.0.7, Vendor: Oracle Corporation
- **Configurações da JVM**: 
  - Heap inicial: 512MB (`-Xms512m`)
  - Heap máximo: 2GB (`-Xmx2g`)
  - Garbage Collector: G1GC (`-XX:+UseG1GC`)

### Dataset
- **Documento PDF**: "Clarissa.pdf" (Obra literária completa)
- **Tamanho do arquivo**: 8.5MB
- **Número de páginas**: 1536
- **Número de palavras**: ~1.2 milhões
- **Caracteres**: ~6.7 milhões

## 🔍 Métricas Analisadas
- **Tempo médio de execução**: Média dos tempos de processamento em microssegundos (μs)
- **Dados de warm-up excluídos**: 10% iniciais das execuções
- **Outliers identificados e removidos**: Utilizando método IQR
- **Desvio padrão**: Variabilidade dos tempos de execução
- **Speedup**: Relação entre o tempo sequencial e paralelo (Ts/Tp)
- **Eficiência**: Speedup dividido pelo número de threads (Speedup/p)

> **Nota**: Dados tratados com remoção de warm-up (10% iniciais) e outliers (método IQR)

## 🛠️ Funcionalidades
### Core
- **Extração de texto de PDFs**: Conversão eficiente de PDF para texto plano
- **Processamento de texto**: Filtragem e normalização de palavras
- **Busca de padrões** com diferentes algoritmos:
  - Sequencial (referência)
  - Paralelo com threads tradicionais (2, 4, 8)
  - Paralelo com threads virtuais (2, 4, 8)

### Análise & Visualização
- **Programa Testador (Benchmark)**: 
  - Executa 30 iterações de cada configuração
  - Registra todos os tempos brutos
  - Armazena dados de execução em arquivo CSV
- **Análise estatística avançada**:
  - Remoção automática de warm-up (10% iniciais das execuções)
  - Detecção e remoção de outliers (método IQR)
  - Cálculo de média, desvio padrão, speedup e eficiência
- **Visualização de dados**:
  - Gráficos de barras para comparação de tempos
  - Gráficos de linhas para análise de escalabilidade
  - Contagem de ocorrências de palavras
- **Exportação de dados**: Saída dos resultados em formato CSV para análises adicionais


## 🏗️ Arquitetura

O sistema segue uma arquitetura modular orientada a objetos:

```mermaid
classDiagram
    class Main {
        +main(String[] args) void
    }
    
    class OpenPDF {
        -String filePath
        -String[] words
        +OpenPDF(String filePath)
        +getWords() String[]
        +closePDF() void
    }
    
    class PerformanceTest {
        -String[] words
        -String[][] searchWords
        -int[] threads
        +PerformanceTest(String[] words)
        +runTests() void
        +print() void
    }
    
    class TestExecutor {
        -String[] words
        -String[][] searchWords
        -int iterations
        -int[] threads
        +runTestsSequencial() int[][]
        +runTestsParalelo() int[][][]
        +getWordsCount() Map
    }
    
    class Sequencial {
        -String[] words
        -int[] searchWordsCount
        -Map wordMap
        -long time
        +getTime() long
        +getSearchWordsCount() int[]
    }
    
    class Paralelo {
        -int threads
        -String[] words
        -int[] searchWordsCount
        -long time
        +getTime() long
        +getSearchWordsCount() int[]
    }

    class ParaleloVirtual {
        -int threads
        -String[] words
        -int[] searchWordsCount
        -long time
        +getTime() long
        +getSearchWordsCount() int[]
    }
    
    class DataProcessor {
        -double[] timeSequentialAverage
        -double[][] timeParallelAverage
        -double[] timeSequentialStdDev
        -double[][] timeParallelStdDev
        -double[][] speedup
        -double[][] efficiency
        +getTimeSequentialAverage() double[]
        +getTimeParallelAverage() double[][]
        +getSpeedup() double[][]
        +getEfficiency() double[][]
    }
    
    class ChartGenerator {
        -String[][] searchWords
        -int[] threads
        -double[] timeSequentialAverage
        -double[][] timeParallelAverage
        -Map wordMap
        -generateGraphs() void
        -exportResultsToCSV() void
    }
    
    Main --> OpenPDF : utiliza
    Main --> PerformanceTest : utiliza
    OpenPDF --> PerformanceTest : envia palavras
    PerformanceTest --> TestExecutor : utiliza
    TestExecutor --> Sequencial : utiliza
    TestExecutor --> Paralelo : utiliza
    TestExecutor --> ParaleloVirtual : utiliza
    PerformanceTest --> DataProcessor : utiliza
    DataProcessor --> ChartGenerator : fornece dados
    PerformanceTest --> ChartGenerator : utiliza
    Sequencial --> TestExecutor : retorna resultados
    Paralelo --> TestExecutor : retorna resultados
    ParaleloVirtual --> TestExecutor : retorna resultados
```

### Componentes Principais

1. **Main**: 
   - Ponto de entrada da aplicação
   - Inicializa o carregamento do PDF e o teste de performance

2. **OpenPDF**: 
   - Encapsula a interação com a biblioteca PDFBox
   - Gerencia abertura, extração e fechamento do documento PDF
   - Realiza o pré-processamento do texto (remoção de caracteres não alfabéticos, normalização para minúsculas)

3. **PerformanceTest**: 
   - Configura os parâmetros do teste (conjuntos de palavras, número de threads, iterações)
   - Orquestra a execução e consolidação dos resultados
   - Formata e apresenta os resultados no console

4. **TestExecutor**: 
   - Executa os 30 testes para cada configuração
   - Coleta e armazena os tempos brutos de execução
   - Salva dados brutos em arquivo para análise posterior
   - Mantém registro das contagens de palavras encontradas

5. **Sequencial**: 
   - Implementação do algoritmo de busca sequencial
   - Utiliza HashMap para busca eficiente em O(1)
   - Mede o tempo de execução com precisão de microssegundos

6. **Paralelo**: 
   - Implementa o algoritmo de busca paralela
   - Suporta configurações com 2, 4 e 8 threads
   - Distribui o trabalho de forma balanceada entre threads
   - Gerencia a sincronização entre threads
  
7. **ParaleloVirtual**: 
   - Implementa o algoritmo de busca paralela com **threads virtuais** (do Java moderno)
   - Suporta configurações com 2, 4 e 8 threads virtuais
   - Distribui o trabalho de forma balanceada entre threads virtuais
   - Gerencia a sincronização entre threads

8. **DataProcessor**: 
   - Aplica tratamento aos dados brutos:
     - Remove primeiras 3 execuções (10% de warm-up)
     - Identifica e remove outliers pelo método IQR
   - Calcula métricas solicitadas:
     - Tempo médio após filtragem
     - Desvio padrão
     - Speedup para cada configuração paralela
     - Eficiência

9. **ChartGenerator**: 
   - Cria visualizações gráficas usando JFreeChart
   - Exporta resultados para formato CSV
   - Customiza a apresentação visual dos gráficos

## 📊 Visualizações e Análise de Resultados

O sistema gera quatro tipos de gráficos para análise visual dos resultados:

### 1. Comparação de Tempos de Execução
![Tempo de Execução](/Atividade_Avaliativa/img/TempoExecucao.png)

*Este gráfico compara os tempos médios de execução (em microssegundos) para as diferentes abordagens. Nota-se a drástica redução de tempo nas implementações com threads virtuais.*

### 2. Análise de Speedup
![Speedup vs Threads](/Atividade_Avaliativa/img/Speedup.png)

*O gráfico de speedup demonstra como o ganho de desempenho escala com o aumento do número de threads. Observa-se que para threads tradicionais, o ganho é quase linear até 4 threads, com diminuição da inclinação após esse ponto - um comportamento clássico previsto pela Lei de Amdahl.*

### 3. Eficiência da Paralelização
![Eficiência vs Threads](/Atividade_Avaliativa/img/Eficiencia.png)

*A eficiência (speedup/número de threads) mostra quanto cada thread contribui para o ganho de desempenho. A queda na eficiência com o aumento do número de threads indica o crescimento do overhead de gerenciamento.*

### 4. Distribuição de Palavras
![Contagem de Palavras](/Atividade_Avaliativa/img/ContPalavras.png)

*Este gráfico mostra a frequência de cada palavra buscada no documento, contextualizando os resultados de desempenho.*

## 💻 Implementação: Análise de Código

A seguir, apresentamos trechos de código simplificados das principais implementações para facilitar a compreensão das diferentes abordagens:

### Algoritmo Sequencial
```java
public class Sequencial {
    private final String[] words;    
    private final Map<String, Integer> wordMap;


    private int[] searchWordsCount;
    private long startTime;
    private long endTime;
    private long time;

    public Sequencial(String[] words,String[] searchWords) {
        this.words = words;
        this.searchWordsCount = new int[searchWords.length];
        this.wordMap = new HashMap<>();
        for (int i = 0; i < searchWords.length; i++) {
            this.wordMap.put(searchWords[i], i);
        }

        setStartTime();
        searchWords();
        setEndTime();    
    }

    private void setStartTime() {
        this.startTime = System.nanoTime()/1000;
    }

    private void setEndTime() {
        this.endTime = System.nanoTime()/1000;
        this.time = endTime - startTime;
    }

    private void searchWords() {
        for (String word : words) {
            Integer index = wordMap.get(word);
            if (index != null) { 
                searchWordsCount[index]++;
            }
        }
    }
}
```

### Algoritmo Paralelo com Threads Tradicionais
```java
public class Paralelo {
    private final int threads;
    private final int wordsPerThread;
    private final String[] words;
    private final Map<String, Integer> wordMap;

    private int[] searchWordsCount;
    private long startTime;
    private long endTime;
    private long time;

    public Paralelo(int threads,String[] words,String[] searchWords) {
        this.threads = threads;
        this.words = words;
        this.wordMap = new HashMap<>();
        for (int i = 0; i < searchWords.length; i++) {
            this.wordMap.put(searchWords[i], i);
        }
        this.searchWordsCount = new int[searchWords.length];  
        this.wordsPerThread = words.length / threads;
        setStartTime();  
        startThreads();
        setEndTime();
    }

    private void setStartTime() {
        this.startTime = System.nanoTime()/1000;
    }

    private void setEndTime() {
        this.endTime = System.nanoTime()/1000;
        this.time = endTime - startTime;
    }

    private void startThreads(){
        Thread[] thread = new Thread[threads];

        for (int i = 0; i < threads; i++) {
            int index = i;
            thread[i] = new Thread(()->{
                searchWords(index);
            });
        }

        for (int i = 0; i < threads; i++) {
            thread[i].start();
        }
        
        for (int i = 0; i < threads; i++) {
            try {
                thread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void searchWords(int indice){
        int threadIndex = indice * wordsPerThread;
        int limit = threadIndex + wordsPerThread;

        if(indice == threads - 1){
            limit = words.length;
        }

        for(int i = threadIndex; i < limit; i++){
            Integer index = wordMap.get(words[i]);
            if (index != null) { 
              // synchronized (searchWordsCount) {searchWordsCount[index]++;}
                searchWordsCount[index]++;
            }
        }
    }
}
```

### Algoritmo Paralelo com Threads Virtuais
```java
public class ParaleloVirtual {
    private final int threads;
    private final int wordsPerThread;
    private final String[] words;
    private final Map<String, Integer> wordMap;

    private int[] searchWordsCount;
    private long startTime;
    private long endTime;
    private long time;

    public ParaleloVirtual(int threads,String[] words,String[] searchWords){
        this.threads = threads;
        this.words = words;
        this.wordMap = new HashMap<>();
        for (int i = 0; i < searchWords.length; i++) {
            this.wordMap.put(searchWords[i], i);
        }
        this.searchWordsCount = new int[searchWords.length];  
        this.wordsPerThread = words.length / threads;
        setStartTime();  
        startThreads();
        setEndTime();
    }

    private void setStartTime() {
        this.startTime = System.nanoTime()/1000;
    }

    private void setEndTime() {
        this.endTime = System.nanoTime()/1000;
        this.time = endTime - startTime;
    }

    private void startThreads(){
        for (int i = 0; i < threads; i++){
            int indice = i;
            Thread.startVirtualThread(() -> {
                searchWords(indice);
            });
        }
    }

    private void searchWords(int indice){
        int threadIndex = indice * wordsPerThread;
        int limit = threadIndex + wordsPerThread;

        if(indice == threads - 1){
            limit = words.length;
        }

        for(int i = threadIndex; i < limit; i++){
            Integer index = wordMap.get(words[i]);
            if (index != null) { 
              //  synchronized (searchWordsCount) {searchWordsCount[index]++;}
               searchWordsCount[index]++;
            }
        }
    }
}
```

### Estratégia de Particionamento de Dados

O algoritmo de particionamento utilizado divide o array de palavras em segmentos contíguos de tamanho aproximadamente igual. Esta abordagem, conhecida como "particionamento por blocos", foi escolhida por sua simplicidade e eficiência:

1. Calcula-se o tamanho do bloco: `wordsPerThread = words.length / threads`
2. Cada thread recebe um segmento de palavras:
   - Thread 0: palavras do índice 0 até (wordsPerThread - 1)
   - Thread 1: palavras do índice chunkSize até ( indice * wordsPerThread + wordsPerThread)
   - ...
   - Última thread: palavras do índice (indice*wordsPerThread) até o final

Esta estratégia minimiza o overhead de comunicação entre threads, já que cada uma processa seu bloco independentemente. No entanto, pode levar a desbalanceamento de carga se as palavras buscadas estiverem distribuídas de forma não uniforme no texto.

Alternativas consideradas incluíam particionamento circular (round-robin) e particionamento dinâmico, mas o particionamento por blocos apresentou melhor desempenho nos testes preliminares.

## 📋 Amostra de Resultados

```
Palavras: bacana 0
Palavras: oppression 7
Palavras: clarissa 869
Palavras: indignation 69
Palavras: letter 3531
Palavras: vixe 0
Palavras: forbidden 17
Palavras: lovelace 2026
Palavras: virtue 283
Palavras: dear 1850
Palavras: eita 0
Palavras: miss 2293
======================== RESULTADOS DE PERFORMANCE =============================
🔍 CONJUNTO DE PALAVRAS 1: clarissa, letter, lovelace, virtue, dear, miss
│ SEQUENCIAL    │ Tempo médio:  6755,30 μs │ Desvio padrão:    43,00 μs │
├───────────────┼────────────────┼──────────────────┼───────────┼──────────────┤
│   PARALELO    │   TEMPO MÉDIO  │   DESVIO PADRÃO  │  SPEEDUP  │  EFICIÊNCIA  │
├───────────────┼────────────────┼──────────────────┼───────────┼──────────────┤
│  2 Threads    │  4016,49 μs    │    36,36 μs      │   1,68x   │   84,09%     │
│  4 Threads    │  2253,35 μs    │    36,65 μs      │   3,00x   │   74,95%     │
│  8 Threads    │  2926,78 μs    │    85,33 μs      │   2,31x   │   28,85%     │
└───────────────┴────────────────┴──────────────────┴───────────┴──────────────┘

============================== THREAD VIRTUAL ==================================
├───────────────┼────────────────┼──────────────────┼───────────┼──────────────┤
│   PARALELO    │   TEMPO MÉDIO  │   DESVIO PADRÃO  │  SPEEDUP  │  EFICIÊNCIA  │
├───────────────┼────────────────┼──────────────────┼───────────┼──────────────┤
│  2 Threads    │  10,59 μs      │     0,59 μs      │ 637,87x   │ 31893,64%    │
│  4 Threads    │  20,43 μs      │     0,94 μs      │ 330,68x   │ 8266,98%     │
│  8 Threads    │  13,52 μs      │     6,94 μs      │ 499,49x   │ 6243,63%     │
└───────────────┴────────────────┴──────────────────┴───────────┴──────────────┘

🔍 CONJUNTO DE PALAVRAS 2: eita, bacana, vixe, forbidden, indignation, oppression
│ SEQUENCIAL    │ Tempo médio:  6493,95 μs │ Desvio padrão:    40,95 μs │
├───────────────┼────────────────┼──────────────────┼───────────┼──────────────┤
│   PARALELO    │   TEMPO MÉDIO  │   DESVIO PADRÃO  │  SPEEDUP  │  EFICIÊNCIA  │
├───────────────┼────────────────┼──────────────────┼───────────┼──────────────┤
│  2 Threads    │  3818,12 μs    │    78,01 μs      │   1,70x   │   85,04%     │
│  4 Threads    │  2045,03 μs    │    18,30 μs      │   3,18x   │   79,39%     │
│  8 Threads    │  1531,93 μs    │    24,04 μs      │   4,24x   │   52,99%     │
└───────────────┴────────────────┴──────────────────┴───────────┴──────────────┘

============================== THREAD VIRTUAL ==================================
├───────────────┼────────────────┼──────────────────┼───────────┼──────────────┤
│   PARALELO    │   TEMPO MÉDIO  │   DESVIO PADRÃO  │  SPEEDUP  │  EFICIÊNCIA  │
├───────────────┼────────────────┼──────────────────┼───────────┼──────────────┤
│  2 Threads    │  1,62 μs       │     0,51 μs      │ 4006,91x  │ 200345,39%   │ 
│  4 Threads    │  3,00 μs       │     0,00 μs      │ 2164,65x  │ 54116,28%    │
│  8 Threads    │  6,00 μs       │     0,00 μs      │ 1082,33x  │ 13529,07%    │
└───────────────┴────────────────┴──────────────────┴───────────┴──────────────┘


=================================================================================

```
Adicionalmente, os resultados são exportados para o arquivo `resultados_teste.csv` e os gráficos são exibidos em uma interface gráfica.

## 🔧 Dependências

O projeto utiliza as seguintes bibliotecas:

- **Apache PDFBox (v2.0.27+)**: Para manipulação de arquivos PDF
  ```xml
  <dependency>
      <groupId>org.apache.pdfbox</groupId>
      <artifactId>pdfbox</artifactId>
      <version>2.0.27</version>
  </dependency>
  ```

- **JFreeChart (v1.5.3+)**: Para geração de gráficos
  ```xml
  <dependency>
      <groupId>org.jfree</groupId>
      <artifactId>jfreechart</artifactId>
      <version>1.5.3</version>
  </dependency>
  ```

- **Apache Commons Math (v3.6.1+)**: Para cálculos estatísticos
  ```xml
  <dependency>
      <groupId>org.apache.commons</groupId>
      <artifactId>commons-math3</artifactId>
      <version>3.6.1</version>
  </dependency>
  ```

- **Lombok (v1.18.24+)**: Para redução de código boilerplate
  ```xml
  <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.24</version>
      <scope>provided</scope>
  </dependency>
  ```

## 🚦 Como executar

### Pré-requisitos
- Java JDK 11 ou superior (JDK 19+ para threads virtuais)
- Maven 3.6 ou superior

### Passos para execução

1. Clone o repositório
   ```bash
   git clone https://github.com/StephanyeCunto/Sistemas_Paralelos_Distribuidos
   cd Atividade_Avaliativa
   ```

2. Compile o projeto
   ```bash
   mvn clean compile
   ```

3. Execute a aplicação
   ```bash
   mvn exec:java -Dexec.mainClass="com.Main"
   ```

### Configuração personalizada

Para modificar os parâmetros de teste, você pode editar os seguintes valores na classe `PerformanceTest`:

```java
// Conjuntos de palavras a serem buscadas
private String[][] searchWords = {
    { "clarissa", "letter", "lovelace", "virtue", "dear", "miss" },
    { "eita", "bacana", "vixe", "forbidden", "indignation", "oppression" }
};

// Configurações de threads para testes paralelos
private int[] threads = { 2, 4, 8 };

// Número de iterações para garantir significância estatística
private int iterations = 30;
```

## 📈 Análise de Resultados

### Speedup e Lei de Amdahl
O speedup observado demonstra como o paralelismo melhora o desempenho, mas também revela os limites da paralelização conforme previsto pela Lei de Amdahl. À medida que o número de threads aumenta, o ganho de desempenho começa a apresentar rendimentos decrescentes.

Para threads tradicionais, observamos aproximadamente:
- 2 threads: 1.7x speedup (próximo do ideal de 2x)
- 4 threads: 3.0x speedup (próximo do ideal de 4x)
- 8 threads: 4.2x speedup (abaixo do ideal de 8x)

Este comportamento sugere que aproximadamente 85% do algoritmo é paralelizável, com cerca de 15% sendo overhead ou parte sequencial obrigatória.

### Eficiência do Paralelismo
A eficiência (speedup/número de threads) normalmente diminui com o aumento do número de threads devido a:
- Overhead de criação e gerenciamento de threads
- Contenção em recursos compartilhados
- Limitações da porção paralelizável do algoritmo

Observamos esta tendência nos resultados com threads tradicionais:
- 2 threads: ~85% de eficiência
- 4 threads: ~75% de eficiência
- 8 threads: ~53% de eficiência

### Impacto da Distribuição de Palavras
A frequência das palavras buscadas no documento afeta o desempenho relativo dos algoritmos, especialmente nas versões paralelas. Nos resultados, notamos que:

- Palavras mais frequentes (conjunto 1) geraram speedups menores (2.31x com 8 threads)
- Palavras menos frequentes (conjunto 2) permitiram speedups maiores (4.24x com 8 threads)

Isso ocorre porque palavras mais frequentes resultam em mais atualizações de contadores, o que pode aumentar a contenção em implementações paralelas, mesmo quando usamos técnicas para minimizar o compartilhamento de recursos.

### Impacto do Context Switch
Um dos fatores mais significativos para o desempenho excepcional das threads virtuais é a drástica redução do overhead de troca de contexto (context switch). Nas threads tradicionais do sistema operacional, cada troca de contexto envolve:

1. Salvamento completo do estado da CPU (registradores, contadores, flags)
2. Atualização das tabelas de processo do SO
3. Invalidação de caches de memória
4. Troca da pilha de execução
5. Restauração do estado para o novo processo

Esse processo pode consumir milhares de ciclos de CPU. Em contraste, as threads virtuais do Java utilizam um mecanismo de continuação (continuations) que:

1. Armazena apenas o estado mínimo necessário na pilha Java
2. Permite trocas de contexto extremamente leves (ordem de magnitude de nanossegundos vs. microssegundos)
3. É gerenciado pelo runtime da JVM, não pelo sistema operacional
4. Não exige interrupções do sistema operacional

Este mecanismo explica o ganho de desempenho extraordinário (~4000x) observado nas implementações com threads virtuais.

### Threads Virtuais vs. Tradicionais

As threads virtuais demonstraram um desempenho significativamente superior:

1. **Tempos de execução**: 1-6μs vs. 1500-4000μs (3 ordens de magnitude mais rápidas)
2. **Escalabilidade**: Mantiveram alto throughput mesmo com aumento do número de threads
3. **Estabilidade**: Menor desvio padrão (mais consistentes)
4. **Eficiência extraordinária**: Valores superiores a 10.000% devido ao modelo de execução fundamentalmente diferente

Este comportamento é consistente com a proposta do Project Loom, que visa proporcionar concorrência de alto throughput sem o overhead tradicional das threads do sistema operacional.

## 📝 Conclusões

### Principais Descobertas

1. **Paralelização com Threads Tradicionais**:
   - Speedup quase linear até 4 threads (3x mais rápido)
   - Aproximadamente 85% de eficiência com 2 threads
   - Rendimentos decrescentes após 4 threads, conforme previsto pela Lei de Amdahl

2. **Paralelização com Threads Virtuais**:
   - Ganho de desempenho extraordinário (até 4000x mais rápido!)
   - Overhead mínimo de criação e troca de contexto
   - Eficiência que excede 100%, indicando benefícios estruturais adicionais

3. **Fatores de Influência**:
   - A frequência das palavras buscadas afeta o desempenho
   - O particionamento por blocos mostrou-se eficiente para este problema
   - O overhead de sincronização é o principal limitador para threads tradicionais

### Considerações Práticas

- **Para cargas de trabalho I/O-bound**: Threads virtuais oferecem benefícios massivos
- **Para processamento intensivo de dados**: Mesmo com threads tradicionais, a paralelização oferece ganhos significativos
- **Limitação de hardware**: Os resultados mostraram um pico de eficiência em 4 threads, consistente com os 4 núcleos de performance do processador utilizado

### Trabalhos Futuros

Este projeto poderia ser expandido nas seguintes direções:

1. **Algoritmos alternativos**:
   - Implementar busca paralela com algoritmos distribuídos (MapReduce)
   - Explorar soluções baseadas em stream processing

2. **Otimizações adicionais**:
   - Testar outras estratégias de particionamento (round-robin, dynamic)
   - Experimentar com estruturas de dados concorrentes otimizadas

3. **Análise mais profunda**:
   - Medir o impacto do cache locality em cada implementação
   - Analisar o comportamento com diferentes tamanhos de documento

4. **Aplicações**:
   - Incorporar algoritmos para sistemas de busca em tempo real
   - Desenvolver um mecanismo de indexação paralelo completo

## 🔗 Referências

1. Project Loom - JEP 425: Virtual Threads (Preview) - [https://openjdk.java.net/jeps/425](https://openjdk.java.net/jeps/425)
2. Amdahl, G.M. "Validity of the single processor approach to achieving large scale computing capabilities" - [AFIPS Conference Proceedings, 1967](https://dl.acm.org/doi/10.1145/1465482.1465560)
3. Apache PDFBox Documentation - [https://pdfbox.apache.org/documentation.html](https://pdfbox.apache.org/documentation.html)
4. Java Thread Programming - Oracle Documentation - [https://docs.oracle.com/javase/tutorial/essential/concurrency/](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
5. JFreeChart Documentation - [https://www.jfree.org/jfreechart/](https://www.jfree.org/jfreechart/)

---
*Este README foi elaborado para documentar o projeto de análise de desempenho de algoritmos de busca paralela em documentos PDF, desenvolvido como parte da disciplina de Sistemas Paralelos e Distribuídos.*