package com.ecommerce.view;

import org.jline.reader.impl.completer.StringsCompleter;
import com.ecommerce.view.statescli.ViewState;
import com.ecommerce.view.statescli.menus.LoginState;
import com.ecommerce.service.Marketplace;

public class NexusCLI {
    private final PromptService promptService;
    private final Marketplace marketplace;
    private ViewState currentState;

    public NexusCLI(PromptService promptService, Marketplace marketplace) {
        this.promptService = promptService;
        this.marketplace = marketplace;
        this.currentState = new LoginState(marketplace); // Estado inicial obrigatório
    }

    public void run() {
        while (true) {
            try {
                // Sincroniza dinamicamente o autocompletar do JLine com o estado ativo
                StringsCompleter dynamicCompleter = new StringsCompleter(currentState.getAutocompleteCommands());
                promptService.setActiveCompleter(dynamicCompleter);

                // Fluxo de execução polimórfico controlado puramente por retornos do State
                currentState.renderHeader(promptService);
                ViewState nextState = currentState.handleInput(promptService);

                // Se o estado retornar nulo, quebra o laço e encerra a aplicação de forma limpa
                if (nextState == null) {
                    promptService.printSuccess("Ecossistema Nexus E-Commerce finalizado com sucesso.");
                    break;
                }

                currentState = nextState;

            } catch (Exception ex) {
                // BARREIRA DE RESILIÊNCIA: Captura exceções de domínio e impede o crash do sistema
                promptService.printError("Falha de Validação: " + ex.getMessage());
                promptService.printWarning("Retornando em segurança ao menu operacional...");
                promptService.readString("Pressione ENTER para continuar...");
            }
        }
    }
}