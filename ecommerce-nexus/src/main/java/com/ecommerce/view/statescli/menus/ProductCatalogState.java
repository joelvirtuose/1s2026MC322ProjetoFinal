package com.ecommerce.view.statescli.menus;

import com.ecommerce.view.PromptService;
import com.ecommerce.view.statescli.ViewState;
import com.ecommerce.service.Marketplace;
import com.ecommerce.model.entity.Product;
import java.util.List;

public class ProductCatalogState implements ViewState {
    private final Marketplace marketplace;
    private final String loggedUser;

    // Construtor que recebe a injeção de dependência da Fachada e o estado da sessão
    public ProductCatalogState(Marketplace marketplace, String loggedUser) {
        this.marketplace = marketplace;
        this.loggedUser = loggedUser;
    }

    @Override
    public void renderHeader(PromptService prompt) {
        prompt.clearScreen();
        prompt.printHeader("CATÁLOGO DE PRODUTOS // NEXUS E-COMMERCE");
        
        // Solicita os dados à Fachada (Desacoplamento: a View não sabe como isso é buscado)
        List<Product> products = marketplace.getAllProducts();
        
        if (products.isEmpty()) {
            prompt.printInfo("O catálogo está vazio no momento.");
        } else {
            prompt.printInfo("Produtos disponíveis para compra:\n");
            for (Product p : products) {
                // Utiliza os getters limpos (Apenas Leitura) da entidade
                String linhaProduto = String.format("[ID: %s] %s - R$ %.2f (Estoque: %d)", 
                    p.getId(), p.getName(), p.getPrice(), p.getStockQuantity());
                prompt.printInfo(linhaProduto);
            }
        }
        prompt.printFooter();
    }

    @Override
    public ViewState handleInput(PromptService prompt) {
        String input = prompt.readMenuOption("\nDigite 'voltar' para retornar ao menu principal: ");
        
        if (input.equalsIgnoreCase("voltar")) {
            // Transição polimórfica de volta para o menu principal
            return new MainMenuState(marketplace, loggedUser);
        }
        
        prompt.printWarning("Comando não reconhecido.");
        prompt.readString("Pressione ENTER para tentar novamente...");
        return this; // Retorna a si mesmo para re-renderizar a tela atual
    }

    @Override
    public List<String> getAutocompleteCommands() {
        // Alimenta o JLine para autocompletar a palavra "voltar"
        return List.of("voltar");
    }
}
