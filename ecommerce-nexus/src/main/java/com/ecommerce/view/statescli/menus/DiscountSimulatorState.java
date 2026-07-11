package com.ecommerce.view.statescli.menus;

import com.ecommerce.view.PromptService;
import com.ecommerce.view.statescli.ViewState;
import com.ecommerce.service.Marketplace;
import com.ecommerce.model.entity.Product;
import com.ecommerce.model.entity.User;
import com.ecommerce.model.strategy.DiscountStrategy;
import com.ecommerce.model.strategy.FixedAmountDiscountStrategy;
import com.ecommerce.model.strategy.PercentageDiscountStrategy;
import com.ecommerce.exception.InvalidDiscountException;

import java.util.List;

/**
 * Demonstra ao vivo o padrão Strategy e o late binding: o tipo concreto de
 * desconto só é resolvido em runtime, sem alterar a entidade Product.
 */
public class DiscountSimulatorState implements ViewState {
    private final Marketplace marketplace;
    private final User loggedUser;

    public DiscountSimulatorState(Marketplace marketplace, User loggedUser) {
        this.marketplace = marketplace;
        this.loggedUser = loggedUser;
    }

    @Override
    public void renderHeader(PromptService prompt) {
        prompt.clearScreen();
        prompt.printHeader("SIMULADOR DE DESCONTO // STRATEGY");
        prompt.printInfo("Aplique uma estratégia de desconto e veja o preço recalculado.");
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

        prompt.printInfo("Tipo de desconto: [1] Percentual  [2] Valor fixo");
        String tipo = prompt.readMenuOption("Opção > ");

        try {
            double valor = Double.parseDouble(prompt.readString("Valor do desconto: "));

            // LATE BINDING: a implementação concreta é escolhida agora, em runtime.
            DiscountStrategy strategy;
            switch (tipo) {
                case "1": strategy = new PercentageDiscountStrategy(valor); break;
                case "2": strategy = new FixedAmountDiscountStrategy(valor); break;
                default:
                    prompt.printError("Tipo de desconto inválido.");
                    prompt.readString("Pressione ENTER para continuar...");
                    return this;
            }

            // A entidade aplica a estratégia sem conhecer a implementação concreta.
            Product comDesconto = product.withDiscount(strategy);
            prompt.printSuccess(String.format("%s: preço original R$ %.2f  ->  com desconto R$ %.2f",
                product.getName(), product.getPrice(), comDesconto.getPrice()));

        } catch (NumberFormatException e) {
            prompt.printError("Valor inválido. Introduza um número.");
        } catch (InvalidDiscountException e) {
            // Fail-Fast: preço final ficaria negativo.
            prompt.printError("Desconto rejeitado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Fail-Fast das estratégias (ex.: percentual fora de 0..100).
            prompt.printError("Desconto rejeitado: " + e.getMessage());
        }

        prompt.readString("Pressione ENTER para continuar...");
        return this;
    }

    @Override
    public List<String> getAutocompleteCommands() {
        return List.of("voltar");
    }
}
