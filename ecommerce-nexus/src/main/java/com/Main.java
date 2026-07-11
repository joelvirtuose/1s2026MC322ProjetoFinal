package com.ecommerce;

import com.google.gson.Gson;
import com.ecommerce.service.Marketplace;
import com.ecommerce.view.NexusCLI;

/**
 * Ponto de entrada (Entry Point) e Raiz de Composição (Composition Root) do sistema.
 * Responsável estritamente por instanciar os componentes isolados e injetar 
 * suas dependências antes de iniciar a aplicação.
 */
public class Main {
    public static void main(String[] args) {
        // 1. Inicializa o motor de serialização JSON (infraestrutura compartilhada)
        Gson gson = new Gson();

        // 2. Inicializa a Fachada (Marketplace). 
        // Ao nascer, ela dispara automaticamente a reidratação do banco log .jsonl
        Marketplace marketplace = new Marketplace(gson);

       NexusCLI cli = new NexusCLI(marketplace);
        cli.run();
    }
}
