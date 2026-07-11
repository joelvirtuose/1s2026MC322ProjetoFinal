package com.ecommerce.view.statescli.menus;

import com.ecommerce.view.PromptService;
import com.ecommerce.view.statescli.ViewState;
import com.ecommerce.service.Marketplace;
import com.ecommerce.model.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutState implements ViewState {
    private final Marketplace marketplace;
    private final User loggedUser;
    private final Map<String, Integer> cartItems;

    public CheckoutState(Marketplace marketplace, User loggedUser) {
        this.marketplace = marketplace;
        this.loggedUser = loggedUser;
        this.cartItems = new HashMap<>();
    }

    @Override
    public void renderHeader(PromptService prompt) {
        prompt.clearScreen();
        prompt.printHeader("CARRINHO DE COMPRAS // CHECKOUT");
        if (cartItems.isEmpty()) {
            prompt.printInfo("O seu carrinho está vazio no momento.");
        } else {
            prompt.printInfo("Itens atuais no carrinho:");
            cartItems.forEach((id, qtd) ->
                prompt.printInfo(String.format(" - Produto ID: %s | Quantidade: %d", id, qtd)));
        }
        prompt.printFooter();
    }

    @Override
    public ViewState handleInput(PromptService prompt) {
        prompt.printInfo("Opções: [ID do Produto] para adicionar | [finalizar] | [cancelar]");
        String input = prompt.readMenuOption("Comando > ");

        if (input.equalsIgnoreCase("cancelar")) {
            return new MainMenuState(marketplace, loggedUser);
        }

        if (input.equalsIgnoreCase("finalizar")) {
            if (cartItems.isEmpty()) {
                prompt.printWarning("Não é possível finalizar um pedido sem itens.");
                prompt.readString("Pressione ENTER para continuar...");
                return this;
            }
            try {
                marketplace.checkout(loggedUser.getId(), cartItems);
                prompt.printSuccess("Pedido submetido com sucesso!");
                prompt.readString("Pressione ENTER para voltar ao menu principal...");
                return new MainMenuState(marketplace, loggedUser);
            } catch (Exception e) {
                // Barreira de resiliência: captura falhas de negócio subindo da fachada.
                prompt.printError("Falha ao processar pedido: " + e.getMessage());
                prompt.readString("Pressione ENTER para revisar seu carrinho...");
                return this;
            }
        }

        try {
            int qtd = Integer.parseInt(prompt.readString("Quantidade desejada: "));
            if (qtd <= 0) {
                prompt.printWarning("A quantidade deve ser um número positivo.");
            } else if (marketplace.getProduct(input) == null) {
                prompt.printError("Produto com ID '" + input + "' não existe no catálogo.");
            } else {
                cartItems.put(input, cartItems.getOrDefault(input, 0) + qtd);
                prompt.printSuccess("Produto inserido no carrinho!");
            }
        } catch (NumberFormatException e) {
            prompt.printError("Quantidade inválida. Introduza um número inteiro.");
        }

        prompt.readString("Pressione ENTER para continuar...");
        return this;
    }

    @Override
    public List<String> getAutocompleteCommands() {
        return List.of("finalizar", "cancelar");
    }
}
