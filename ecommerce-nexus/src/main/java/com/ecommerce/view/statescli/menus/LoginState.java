package com.ecommerce.view.statescli.menus;

import com.ecommerce.view.PromptService;
import com.ecommerce.view.statescli.ViewState;
import com.ecommerce.service.Marketplace;
import com.ecommerce.model.entity.User;
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
        prompt.printInfo("Introduza o e-mail cadastrado para aceder (ex: joelson@nexus.com).");
        prompt.printFooter();
    }

    @Override
    public ViewState handleInput(PromptService prompt) {
        String input = prompt.readMenuOption("E-mail: ");

        if (input.equalsIgnoreCase("sair")) return null;

        if (input.isBlank()) {
            prompt.printWarning("O e-mail não pode ser vazio.");
            prompt.readString("Pressione ENTER para tentar novamente...");
            return this;
        }

        User user = marketplace.authenticate(input);
        if (user == null) {
            prompt.printError("Credenciais inválidas: e-mail não encontrado.");
            prompt.readString("Pressione ENTER para tentar novamente...");
            return this;
        }

        prompt.printSuccess("Bem-vindo, " + user.getName() + " (" + user.getRole() + ")");
        prompt.readString("Pressione ENTER para entrar no painel principal...");
        return new MainMenuState(marketplace, user);
    }

    @Override
    public List<String> getAutocompleteCommands() {
        return List.of("sair");
    }
}
