package com.ecommerce.view;

import com.ecommerce.view.statescli.ViewState;
import com.ecommerce.view.statescli.menus.LoginState;
import com.ecommerce.service.Marketplace;

public class NexusCLI {
    private final PromptService prompt;
    private ViewState currentState;

    public NexusCLI(Marketplace marketplace) {
        this.prompt = new PromptService();
        // O ponto de entrada da aplicação é o Login
        this.currentState = new LoginState(marketplace);
    }

    public void start() {
        // Loop polimórfico de execução da CLI baseada em estados (State Pattern)
        while (currentState != null) {
            // 1. Atualiza dinamicamente o JLine com os comandos aceitos pelo menu atual
            prompt.updateCompleters(currentState.getAutocompleteCommands());
            
            // 2. Renderiza os componentes visuais
            currentState.renderHeader(prompt);
            
            // 3. Captura a entrada, processa regras do domínio e transiciona de estado
            currentState = currentState.handleInput(prompt);
        }
        
        prompt.clearScreen();
        System.out.println("Obrigado por utilizar o Nexus E-Commerce. Aplicação encerrada com sucesso.");
    }
}