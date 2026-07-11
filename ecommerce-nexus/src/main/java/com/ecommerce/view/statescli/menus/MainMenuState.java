package com.ecommerce.view.statescli.menus;

import com.ecommerce.view.PromptService;
import com.ecommerce.view.statescli.ViewState;
import com.ecommerce.service.Marketplace;
import com.ecommerce.model.entity.User;
import com.ecommerce.model.valueobject.Role;
import com.ecommerce.exception.PermissionDeniedException;

import java.util.ArrayList;
import java.util.List;

public class MainMenuState implements ViewState {
    private final Marketplace marketplace;
    private final User loggedUser;

    public MainMenuState(Marketplace marketplace, User loggedUser) {
        this.marketplace = marketplace;
        this.loggedUser = loggedUser;
    }

    private boolean isAdmin() {
        return loggedUser.getRole() == Role.ADMINISTRADOR;
    }

    @Override
    public void renderHeader(PromptService prompt) {
        prompt.clearScreen();
        prompt.printHeader("MENU PRINCIPAL // SESSÃO: " + loggedUser.getName().toUpperCase());

        List<String> options = new ArrayList<>(List.of(
            "Visualizar Catálogo de Produtos",
            "Criar Novo Pedido / Checkout",
            "Simular Desconto (Strategy)",
            "Terminar Sessão (Logout)"
        ));
        if (isAdmin()) options.add("Baixa de Estoque (ADMIN)");

        prompt.printNumberedList("Selecione uma das opções operacionais abaixo:", options);
        prompt.printFooter();
    }

    @Override
    public ViewState handleInput(PromptService prompt) {
        String choice = prompt.readMenuOption("Opção > ");

        switch (choice) {
            case "1": return new ProductCatalogState(marketplace, loggedUser);
            case "2": return new CheckoutState(marketplace, loggedUser);
            case "3": return new DiscountSimulatorState(marketplace, loggedUser);
            case "4":
                prompt.printInfo("A encerrar sessão de " + loggedUser.getName() + "...");
                return new LoginState(marketplace);
            case "5":
                // Autorização por Role: só ADMINISTRADOR alcança a baixa de estoque.
                try {
                    if (!isAdmin()) {
                        throw new PermissionDeniedException("Ação restrita a administradores.");
                    }
                    return new StockAdminState(marketplace, loggedUser);
                } catch (PermissionDeniedException e) {
                    prompt.printError(e.getMessage());
                    prompt.readString("Pressione ENTER para continuar...");
                    return this;
                }
            default:
                prompt.printError("Opção inválida.");
                prompt.readString("Pressione ENTER para continuar...");
                return this;
        }
    }

    @Override
    public List<String> getAutocompleteCommands() {
        List<String> cmds = new ArrayList<>(List.of("1", "2", "3", "4"));
        if (isAdmin()) cmds.add("5");
        return cmds;
    }
}
