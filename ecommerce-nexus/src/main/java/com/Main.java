package com;

import com.google.gson.Gson;
import com.ecommerce.service.Marketplace;
import com.ecommerce.view.PromptService;
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

        // 3. Inicializa o serviço visual rica do terminal (View / JLine)
        PromptService promptService = new PromptService();

        // 4. Inicializa o orquestrador do loop de menus (CLI), injetando suas dependências
        NexusCLI cli = new NexusCLI(promptService, marketplace);

        // 5. Transfere o controle da Thread principal para o loop polimórfico da CLI
        cli.run();
    }
}
