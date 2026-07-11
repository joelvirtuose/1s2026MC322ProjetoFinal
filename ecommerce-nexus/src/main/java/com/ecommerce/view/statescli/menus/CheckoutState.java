package com.ecommerce.view.statescli.menus;

import com.ecommerce.view.PromptService;
import com.ecommerce.view.statescli.ViewState;
import com.ecommerce.service.Marketplace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutState implements ViewState {
    private final Marketplace marketplace;
    private final String loggedUser;
    // Estado interno temporário do carrinho
    private final Map<String, Integer> cartItems;

    public CheckoutState(Marketplace marketplace, String loggedUser) {
        this.marketplace = marketplace;
        this.loggedUser = loggedUser;
        this.cartItems = new HashMap<>(); // Inicia o carrinho vazio
    }

    @Override
    public void renderHeader(PromptService prompt) {
        prompt.clearScreen();
        prompt.printHeader("CARRINHO DE COMPRAS // CHECKOUT");
        
        if (cartItems.isEmpty()) {
            prompt.printInfo("O seu carrinho está vazio no momento.");
        } else {
            prompt.printInfo("Itens atuais no carrinho:");
            // Exibe o que já foi adicionado na memória temporária do estado
            cartItems.forEach((id, qtd) -> 
                prompt.printInfo(String.format(" - Produto ID: %s | Quantidade: %d", id, qtd))
            );
        }
        prompt.printFooter();
    }

    @Override
    public ViewState handleInput(PromptService prompt) {
        prompt.printInfo("Opções: [ID do Produto] para adicionar | [finalizar] para fechar pedido | [cancelar] para sair");
        String input = prompt.readString("Comando > ");

        // 1. Fluxo de Saída/Cancelamento
        if (input.equalsIgnoreCase("cancelar")) {
            return new MainMenuState(marketplace, loggedUser);
        }

        // 2. Fluxo de Finalização
        if (input.equalsIgnoreCase("finalizar")) {
            if (cartItems.isEmpty()) {
                prompt.printWarning("Não é possível finalizar um pedido sem itens.");
                prompt.readString("Pressione ENTER para continuar...");
                return this; // Re-renderiza o carrinho
            }
            
            try {
                // Delega a intenção de compra para a Fachada
                marketplace.checkout(loggedUser, cartItems);
                prompt.printSuccess("Pedido submetido com sucesso!");
                prompt.readString("Pressione ENTER para voltar ao menu principal...");
                return new MainMenuState(marketplace, loggedUser);
                
            } catch (Exception e) {
                // Barreira de Resiliência: Captura falhas de negócio (ex: estoque insuficiente)
                prompt.printError("Falha ao processar pedido: " + e.getMessage());
                prompt.readString("Pressione ENTER para revisar seu carrinho...");
                return this;
            }
        }

        // 3. Fluxo de Adição de Itens (Se não for 'finalizar' nem 'cancelar', assumimos que é um ID)
        try {
            String qtdStr = prompt.readString("Quantidade desejada: ");
            int qtd = Integer.parseInt(qtdStr);
            
            if (qtd <= 0) {
                prompt.printWarning("A quantidade deve ser um número positivo.");
            } else {
                if (marketplace.getProduct(input) == null) {
                    prompt.printError("Produto com ID '" + input + "' não existe no catálogo.");
                } else {
                    cartItems.put(input, cartItems.getOrDefault(input, 0) + qtd);
                    prompt.printSuccess("Produto inserido no carrinho!");
                }
            }
        } catch (NumberFormatException e) {
            prompt.printError("Quantidade inválida. Por favor, introduza um número inteiro.");
        }

        prompt.readString("Pressione ENTER para continuar...");
        return this; // Mantém o usuário no mesmo estado para adicionar mais itens
    }

    @Override
    public List<String> getAutocompleteCommands() {
        return List.of("finalizar", "cancelar");
    }
}
