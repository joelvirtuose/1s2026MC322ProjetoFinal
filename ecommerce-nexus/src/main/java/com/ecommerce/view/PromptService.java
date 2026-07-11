package com.ecommerce.view;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável por encapsular toda a lógica de Input/Output do terminal.
 * Protege o resto do sistema de lidar diretamente com a API complexa do JLine.
 */
public class PromptService {
    private Terminal terminal;
    private LineReader lineReader;

    public PromptService() {
        try {
            // Constrói o terminal capturando o console nativo do Sistema Operacional
            this.terminal = TerminalBuilder.builder().system(true).build();
            
            // Inicializador padrão sem comandos de autocompletar iniciais
            this.lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar o terminal JLine", e);
        }
    }

    /**
     * Atualiza dinamicamente os completers do JLine dependendo do estado da View ativa.
     * Resolve o erro de tipagem forçando a conversão limpa para array de interfaces.
     */
    public void updateCompleters(List<String> commands) {
        if (commands == null || commands.isEmpty()) return;
        
        List<StringsCompleter> stringsCompleters = commands.stream()
                .map(StringsCompleter::new)
                .collect(Collectors.toList());

        // Solução crucial de conversão de tipos para evitar o erro do "varargs mismatch" do Maven
        Completer aggregate = new AggregateCompleter(stringsCompleters.toArray(new Completer[0]));
        
        this.lineReader = LineReaderBuilder.builder()
                .terminal(this.terminal)
                .completer(aggregate)
                .build();
    }

    public String readString(String promptLabel) {
        String input = lineReader.readLine(promptLabel);
        return input == null ? "" : input.trim();
    }

    public String readMenuOption(String promptLabel) {
        while (true) {
            String input = lineReader.readLine(promptLabel);
            if (input == null) return "";
            
            input = input.trim();
            if (!input.isEmpty()) {
                return input;
            }
        }
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void printHeader(String title) {
        System.out.println("=================================================================");
        System.out.println(" >> " + title);
        System.out.println("=================================================================");
    }

    public void printFooter() {
        System.out.println("=================================================================");
    }

    public void printInfo(String message) { System.out.println("[INFO] " + message); }
    public void printSuccess(String message) { System.out.println("\u001B[32m[SUCESSO] " + message + "\u001B[0m"); }
    public void printWarning(String message) { System.out.println("\u001B[33m[AVISO] " + message + "\u001B[0m"); }
    public void printError(String message) { System.out.println("\u001B[31m[ERRO] " + message + "\u001B[0m"); }

    public void printNumberedList(String label, List<String> options) {
        System.out.println(label);
        for (int i = 0; i < options.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + options.get(i));
        }
    }
}
