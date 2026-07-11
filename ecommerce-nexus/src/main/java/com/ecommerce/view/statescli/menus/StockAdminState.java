package com.ecommerce.view.statescli.menus;

import com.ecommerce.view.PromptService;
import com.ecommerce.view.statescli.ViewState;
import com.ecommerce.service.Marketplace;
import com.ecommerce.model.entity.Product;
import com.ecommerce.model.entity.User;
import com.ecommerce.service.core.DeductStockCommand;

import java.util.List;

/**
 * Estado administrativo que dá baixa em estoque enviando um DeductStockCommand
 * pelo motor unificado de despacho (CommandDispatcher).
 */
public class StockAdminState implements ViewState {
    private final Marketplace marketplace;
    private final User loggedUser;

    public StockAdminState(Marketplace marketplace, User loggedUser) {
        this.marketplace = marketplace;
        this.loggedUser = loggedUser;
    }

    @Override
    public void renderHeader(PromptService prompt) {
        prompt.clearScreen();
        prompt.printHeader("BAIXA DE ESTOQUE // PAINEL ADMIN");
        prompt.printInfo("Sessão: " + loggedUser.getName() + " (" + loggedUser.getRole() + ")");
        prompt.printFooter();
    }

    @Override
    public ViewState handleInput(PromptService prompt) {
        String productId = prompt.readMenuOption("ID do produto (ou 'voltar'): ");
        if (productId.equalsIgnoreCase("voltar")) {
            return new MainMenuState(marketplace, loggedUser);
        }

        Product product = marketplace.getProduct(productId);
        if (product == null) {
            prompt.printError("Produto '" + productId + "' não existe no catálogo.");
            prompt.readString("Pressione ENTER para continuar...");
            return this;
        }

        try {
            int qtd = Integer.parseInt(prompt.readString("Quantidade a dar baixa: "));
            // Envia o comando ao motor único; o dispatcher resolve o UpdateStockHandler.
            marketplace.dispatch(new DeductStockCommand(productId, qtd));
            Product atualizado = marketplace.getProduct(productId);
            prompt.printSuccess(String.format("Baixa efetuada. Estoque de %s agora: %d",
                atualizado.getName(), atualizado.getStockQuantity()));
        } catch (NumberFormatException e) {
            prompt.printError("Quantidade inválida.");
        } catch (Exception e) {
            // Ex.: InsufficientStockException vinda do domínio (Fail-Fast).
            prompt.printError("Falha na baixa: " + e.getMessage());
        }

        prompt.readString("Pressione ENTER para continuar...");
        return this;
    }

    @Override
    public List<String> getAutocompleteCommands() {
        return List.of("voltar");
    }
}
