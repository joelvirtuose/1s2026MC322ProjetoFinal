package com.ecommerce.service.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Motor unificado de despacho (Invoker do padrão Command).
 * Mapeia cada tipo de Command ao seu Handler e resolve o executor correto
 * em runtime por polimorfismo, sem cadeias de if/switch.
 *
 * OCP: registrar um novo par Command+Handler não exige alterar esta classe.
 */
public class CommandDispatcher {

    // O curinga é fechado com segurança em register/dispatch pela relação de tipos.
    private final Map<Class<? extends Command>, Handler<? extends Command>> handlers = new HashMap<>();

    /** Vincula um tipo de comando ao seu handler (pareamento verificado pelo compilador). */
    public <C extends Command> void register(Class<C> commandType, Handler<C> handler) {
        handlers.put(commandType, handler);
    }

    /**
     * Localiza o handler pelo tipo dinâmico do comando e delega a execução.
     * Cast não verificado, porém seguro: register() só permite pares coerentes.
     */
    @SuppressWarnings("unchecked")
    public <C extends Command> void dispatch(C command) throws Exception {
        Handler<C> handler = (Handler<C>) handlers.get(command.getClass());
        if (handler == null) {
            throw new IllegalStateException(
                "Nenhum handler registrado para o comando: " + command.getClass().getSimpleName());
        }
        handler.handle(command);
    }
}
