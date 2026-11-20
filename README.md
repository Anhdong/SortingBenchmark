# Sorting Cutoff Benchmark (Java & Python)

A benchmarking project designed to determine the optimal subarray threshold (**cutoff size**) at which hybrid **QuickSort** and **MergeSort** implementations should switch to **Insertion Sort** to maximize CPU cache locality and minimize recursion overhead.

---

## 📋 Overview

While divide-and-conquer algorithms ($O(N \log N)$) outperform $O(N^2)$ algorithms on large datasets, the overhead of recursive calls and auxiliary memory operations becomes costly on small subarrays. By switching to **Insertion Sort** once a partition falls below a specific threshold, execution stays within L1/L2 CPU caches.

- **Primitive Optimization:** All sorting implementations operate directly on primitive `int[]` arrays rather than boxed `Comparable` objects to eliminate pointer chasing and maximize spatial cache locality.
- **Empirical Sweet Spot:** Based on the included benchmark runs (`mergesort_result.csv` and `quicksort_result.csv` across 300 test iterations), execution time drops sharply once a cutoff is introduced, reaching optimal performance around a **cutoff size of 30–40 elements** before gradually degrading as Insertion Sort's $O(N^2)$ complexity dominates.

---

## 📂 Project Structure

```text
├── flake.nix               # Declarative Nix development environment (JDK 21 + Python)
├── flake.lock              # Locked nixpkgs dependency versions
├── src/
│   ├── Benchmark.java      # CLI runner, JIT warmup harness, and CSV exporter
│   ├── QuickSort.java      # Hybrid QuickSort with randomized shuffle & InsertionSort cutoff
│   ├── MergeSort.java      # Hybrid MergeSort with auxiliary array & InsertionSort cutoff
│   ├── InsertionSort.java  # Full-array and subarray [lo..hi] Insertion Sort
│   ├── HeapSort.java       # Standalone HeapSort implementation (NotUsing)
│   ├── ArrayUtils.java     # Fisher-Yates shuffle, swaps, and test-case generators
│   └── Main.java           # Scratchpad entry point for testing ArrayUtils
├── script/
│   └── visualize.py        # CLI plotting tool using pandas and plotnine
├── mergesort_result.csv    # Sample benchmark output for MergeSort
├── quicksort_result.csv    # Sample benchmark output for QuickSort
└── benchmark_plot.png      # Rendered comparison chart
```

---

## 🚀 Prerequisites & Setup

### Option 1: Using Nix Flakes (NixOS only)
Enter the pre-configured shell containing **OpenJDK 21**, **Python 3**, `pandas`, and `plotnine`:
```bash
nix develop
```

### Option 2: Manual Setup
- **Java:** JDK 17 or higher (uses switch expressions and text blocks)
- **Python:** Python 3.9+ with `pandas` and `plotnine` installed:
  ```bash
  pip install pandas plotnine
  ```

---

## 🛠️ Usage

### 1. Compile the Java Source
Compile all `.java` files into an `out/` directory (ignored by `.gitignore`):
```bash
javac -d out src/*.java
```

### 2. Run the Benchmark
Run `Benchmark` with 6 required arguments:
```bash
java -cp out Benchmark <Algorithm> <NumTests> <ArraySize> <CutoffLo> <CutoffHi> <CutoffStep>
```
- `<Algorithm>`: `Q` for QuickSort or `M` for MergeSort
- `<NumTests>`: Number of randomized test arrays to evaluate per cutoff
- `<ArraySize>`: Number of elements in each test array
- `<CutoffLo> <CutoffHi> <CutoffStep>`: Range and step interval for cutoff sizes

**Example (300 tests on 100,000 elements, testing cutoffs 0 to 100 in steps of 10 [0,10,...,100] ):**
```bash
java -cp out Benchmark Q 300 100000 0 100 10
java -cp out Benchmark M 300 100000 0 100 10
```
*This outputs `quicksort_result.csv` and `mergesort_result.csv` in the project root.*

### 3. Visualize the Results
Pass one or more generated CSV files to `script/visualize.py`:
```bash
python script/visualize.py quicksort_result.csv mergesort_result.csv \
  -o benchmark_plot.png \
  -t "QuickSort vs. MergeSort Performance by Cutoff Size"
```
**CLI Options:**
- `-o`, `--output`: Output image filename (default: `benchmark_plot.png`)
- `-t`, `--title`: Custom chart title (default: `Algorithm Performance vs. Cutout Size`)

---

## 🤝 Contribution

### Human Contributions
- **Core Algorithmic Foundation:** Adapted base QuickSort, MergeSort, InsertionSort, and HeapSort implementations from course/textbook reference slides and refactored them from generic `Comparable[]` to primitive `int[]` arrays for cache accuracy.
- **Architecture & CLI Design:** Modularized sorting utilities into `ArrayUtils`, added `ArgumentParser` and multi-CSV comparison support to `visualize.py`, and built the CLI argument handling and terminal status output in `Benchmark.java`.
- **Environment & Experimentation:** Configured the Nix flake development environment (`flake.nix`) and executed the benchmark runs.

### AI Contributions
- **Benchmarking Rigor & Boilerplate:** Suggested and implemented the 10,000-iteration JVM JIT warmup loop with dead-code elimination prevention (`checksum`), added `CUTOFF` parameterization to the sorting methods, and scaffolded the `PrintWriter` CSV export logic.
- **Initial Visualization & Setup:** Provided initial project structure scaffolding and the baseline `pandas` + `plotnine` data aggregation and plotting pipeline.
- **Writing README:** Rewriting README files base on the initial outline by anhdong and project structure overall.

---

## 📖 Step-by-Step Example: Benchmarking QuickSort vs. MergeSort

This walkthrough demonstrates how to run a full benchmark comparing **QuickSort** and **MergeSort** across **300 randomized arrays** of **10,000 elements**, evaluating cutoff sizes from **0 to 100** (in steps of 10), and plotting the results.

### Step 1: Compile the Java Source Files
Compile all `.java` files from `src/` into the `out/` directory:
```bash
javac -d out src/*.java
```

### Step 2: Run the QuickSort Benchmark
Execute `Benchmark` with `Q` (QuickSort), `300` test iterations, an array size of `10000`, and a cutoff range of `0` to `100` with a step of `1`:
```bash
java -cp out Benchmark Q 300 10000 0 100 1
```
* The program generates 300 shuffled arrays of 10,000 integers, warms up the JVM JIT compiler for 10,000 iterations, and records execution times in nanoseconds.
* **Tip:** For large arrays or wide ranges, start with a coarse step size (e.g., 10) to identify the optimal region, then narrow the range with a smaller step size (e.g., 1 or 2) to find the exact sweet spot. I just too lazy in this example😄."
* When finished, the raw timing data is saved to `quicksort_result.csv` in the project root.

### Step 3: Run the MergeSort Benchmark
Run the exact same experiment for MergeSort using the `M` flag:
```bash
java -cp out Benchmark M 300 10000 0 100 1
```
* When finished, the raw timing data is saved to `mergesort_result.csv`.

### Step 4: Generate the Comparison Plot
Pass both generated CSV files to `script/visualize.py` to aggregate the execution times and render a comparison chart:
```bash
python script/visualize.py quicksort_result.csv mergesort_result.csv \
  -o benchmark_result.png \
  -t "QuickSort vs. MergeSort (N = 10,000, 300 Tests)"
```
* This reads both datasets, computes the average execution time per cutoff size, and saves the resulting chart to `benchmark.png`.

### Output

![QuickSort vs. MergeSort Benchmark Results](./results/benchmark_result.png)