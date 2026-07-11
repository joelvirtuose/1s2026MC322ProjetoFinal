package com.ecommerce.view.statescli.menus;

import com.ecommerce.view.PromptService;
import com.ecommerce.view.statescli.ViewState;
import com.ecommerce.service.Marketplace;
import java.util.List;

public class MainMenuState implements ViewState {
    private final Marketplace marketplace;
    private final String loggedUser;

    public MainMenuState(Marketplace marketplace, String loggedUser) {
        this.marketplace = marketplace;
        this.loggedUser = loggedUser;
    }

    @Override
    public void renderHeader(PromptService prompt) {
        prompt.clearScreen();
        prompt.printHeader("MENU PRINCIPAL // SESSÃO: " + loggedUser.toUpperCase());
        prompt.printNumberedList("Selecione uma das opções operacionais abaixo:", List.of(
            "Visualizar Catálogo de Produtos",
            "Criar Novo Pedido / Checkout",
            "Terminar Sessão (Logout)"
        ));
        prompt.printFooter();
    }

    @Override
    public ViewState handleInput(PromptService prompt) {
        String choice = prompt.readMenuOption("Opção > ");

        switch (choice) {
            case "1":
                return new ProductCatalogState(marketplace, loggedUser);
            case "2":
                // Nova transição de estado para o Carrinho de Compras
                return new CheckoutState(marketplace, loggedUser);
            case "3":
                prompt.printInfo("A encerrar sessão de " + loggedUser + "...");
                return new LoginState(marketplace); // Retorna com segurança à estaca zero
            default:
                prompt.printError("Opção inválida ou funcionalidade em desenvolvimento.");
                prompt.readString("Pressione ENTER para continuar...");
                return this;
        }
    }

    @Override
    public List<String> getAutocompleteCommands() {
        return List.of("1", "2", "3");
    }
}
