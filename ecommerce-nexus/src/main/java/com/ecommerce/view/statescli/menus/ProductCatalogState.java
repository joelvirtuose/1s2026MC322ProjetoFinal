package com.ecommerce.view.statescli.menus;

import com.ecommerce.view.PromptService;
import com.ecommerce.view.statescli.ViewState;
import com.ecommerce.service.Marketplace;
import com.ecommerce.model.entity.Product;
import com.ecommerce.model.entity.User;
import java.util.List;

public class ProductCatalogState implements ViewState {
    private final Marketplace marketplace;
    private final User loggedUser;

    public ProductCatalogState(Marketplace marketplace, User loggedUser) {
        this.marketplace = marketplace;
        this.loggedUser = loggedUser;
    }

    @Override
    public void renderHeader(PromptService prompt) {
        prompt.clearScreen();
        prompt.printHeader("CATÁLOGO DE PRODUTOS // NEXUS E-COMMERCE");
        renderProducts(prompt, marketplace.getAllProducts(), "Produtos disponíveis para compra:");
        prompt.printFooter();
    }

    private void renderProducts(PromptService prompt, List<Product> products, String titulo) {
        if (products.isEmpty()) {
            prompt.printInfo("Nenhum produto encontrado.");
            return;
        }
        prompt.printInfo(titulo + "\n");
        for (Product p : products) {
            prompt.printInfo(String.format("[ID: %s] %s - R$ %.2f (Estoque: %d)",
                p.getId(), p.getName(), p.getPrice(), p.getStockQuantity()));
        }
    }

    @Override
    public ViewState handleInput(PromptService prompt) {
        prompt.printInfo("Opções: [filtrar] por faixa de preço | [voltar] ao menu principal");
        String input = prompt.readMenuOption("Comando > ");

        if (input.equalsIgnoreCase("voltar")) {
            return new MainMenuState(marketplace, loggedUser);
        }

        if (input.equalsIgnoreCase("filtrar")) {
            try {
                double min = Double.parseDouble(prompt.readString("Preço mínimo: "));
                double max = Double.parseDouble(prompt.readString("Preço máximo: "));
                // Delega a consulta ao QueryService através da fachada.
                List<Product> filtrados = marketplace.findProductsByPriceRange(min, max);
                renderProducts(prompt, filtrados,
                    String.format("Produtos entre R$ %.2f e R$ %.2f:", min, max));
            } catch (NumberFormatException e) {
                prompt.printError("Valores inválidos. Introduza números.");
            }
            prompt.readString("Pressione ENTER para continuar...");
            return this;
        }

        prompt.printWarning("Comando não reconhecido.");
        prompt.readString("Pressione ENTER para tentar novamente...");
        return this;
    }

    @Override
    public List<String> getAutocompleteCommands() {
        return List.of("filtrar", "voltar");
    }
}
