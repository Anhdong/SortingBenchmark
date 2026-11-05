{
  description = "Sorting Benchmark Environment (Java + Python)";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  };

  outputs = { self, nixpkgs }: 
  let
    system = "x86_64-linux"; 
    pkgs = nixpkgs.legacyPackages.${system};
    
    pythonEnv = pkgs.python3.withPackages (ps: with ps; [
      pandas
      plotnine
    ]);
  in 
  {
    devShells.${system}.default = pkgs.mkShell {
      buildInputs = [
        pkgs.jdk21      # Java Compiler and Runner
        pythonEnv       # Python include libraries
      ];

      shellHook = ''
        echo "Your sorting benchmark environment is ready!"
        java -version
        python --version
      '';
    };
  };
}
