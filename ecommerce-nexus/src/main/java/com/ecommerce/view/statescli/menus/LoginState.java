package com.ecommerce.view.statescli.menus;

import com.ecommerce.view.PromptService;
import com.ecommerce.view.statescli.ViewState;
import com.ecommerce.service.Marketplace;
import java.util.List;

public class LoginState implements ViewState {
    private final Marketplace marketplace;

    public LoginState(Marketplace marketplace) {
        this.marketplace = marketplace;
    }

    @Override
    public void renderHeader(PromptService prompt) {
        prompt.clearScreen();
        prompt.printHeader("NEXUS E-COMMERCE // AUTENTICAÇÃO");
        prompt.printInfo("Por favor, introduza o seu identificador ou e-mail cadastrado para aceder.");
        prompt.printFooter();
    }

    @Override
    public ViewState handleInput(PromptService prompt) {
        String input = prompt.readMenuOption("Utilizador/E-mail: ");

        if (input.equalsIgnoreCase("sair")) {
            return null; // Encerra graciosamente a aplicação
        }

        if (input.isBlank()) {
            prompt.printWarning("O identificador não pode ser vazio.");
            prompt.readString("Pressione ENTER para tentar novamente...");
            return this; // Permanece no mesmo estado de login
        }

        // Simulação de autenticação baseada nas diretrizes do laboratório
        // Num fluxo real, consultaria o UserService do Marketplace pelo e-mail
        prompt.printSuccess("Autenticação efetuada com sucesso! Bem-vindo, " + input);
        prompt.readString("Pressione ENTER para entrar no painel principal...");

        // Transiciona de forma polimórfica para o Menu Principal passando a Fachada
        return new MainMenuState(marketplace, input);
    }

    @Override
    public List<String> getAutocompleteCommands() {
        return List.of("sair");
    }
}