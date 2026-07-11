package com.ecommerce.service.core;

/**
 * Contrato genérico para todo executor de comando (Command Pattern - Handler).
 * O parâmetro C garante em tempo de compilação que um handler só processe o
 * comando para o qual foi projetado, eliminando casts inseguros no domínio.
 *
 * @param <C> o tipo concreto de Command tratado por este handler
 */
public interface Handler<C extends Command> {
    void handle(C command) throws Exception;
}
