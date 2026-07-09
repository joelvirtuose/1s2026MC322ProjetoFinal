package com.ecommerce.view.statescli;

import com.ecommerce.view.PromptService;
import java.util.List;

public interface ViewState {
    
    /**
     * Renderiza o cabeçalho e as opções visuais específicas do estado.
     */
    void renderHeader(PromptService prompt);

    /**
     * Captura a entrada do usuário, executa os comandos de negócio e
     * retorna o próximo estado lógico da aplicação.
     * * @return O próximo ViewState, ou 'null' se o usuário desejar encerrar a sessão.
     */
    ViewState handleInput(PromptService prompt);

    /**
     * Fornece a lista de comandos ou opções disponíveis neste estado
     * para alimentar dinamicamente o autocompletar do JLine.
     */
    List<String> getAutocompleteCommands();
}