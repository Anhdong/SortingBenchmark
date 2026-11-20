import argparse
import pandas as pd
import os
from plotnine import ggplot, aes, geom_line, geom_point, labs, theme_minimal

def main():
    # Setup and define ArgumentParser
    parser = argparse.ArgumentParser(description="Visualize Sorting Benchmark Results")
    parser.add_argument("csv_files", nargs='+', type=str, help="Path to one or more benchmark CSV files")
    parser.add_argument("-o", "--output", type=str, default="benchmark_plot.png", help="Output image filename")
    parser.add_argument("-t", "--title", type=str, default="Algorithm Performance vs. Cutoff Size", help="Chart title")

    args = parser.parse_args()

    # Load and process multiple datasets
    all_data = []
    
    for file_path in args.csv_files:
        print(f"Loading data from {file_path}...")
        df = pd.read_csv(file_path)
        
        # Calculate the mean
        df_avg = df.groupby("CutoffSize")["TimeNs"].mean().reset_index()
        
        # Extract a clean name 
        file_name = os.path.basename(file_path)
        algorithm_name = file_name.replace("_result.csv", "").replace(".csv", "").capitalize()
        
        # Add a new column to identify which algorithm this data belongs to
        df_avg["Algorithm"] = algorithm_name
        
        all_data.append(df_avg)

    # Combine all individual dataframes into one master dataframe
    combined_df = pd.concat(all_data, ignore_index=True)

    print("Generating comparison chart...")

    # Draw the chart
    plot = (
        ggplot(combined_df, aes(x="CutoffSize", y="TimeNs", color="Algorithm"))
        + geom_line(size=1)
        + geom_point(size=2.5)
        + labs(
            title=args.title,
            x="Cutoff Size (Elements)",
            y="Average Execution Time (Nanoseconds)"
        )
        + theme_minimal()
    )
    
    # Save the chart
    plot.save(args.output, width=10, height=6, dpi=300, verbose=False)
    print(f"Chart successfully saved to {args.output}")

if __name__ == "__main__":
    main()
