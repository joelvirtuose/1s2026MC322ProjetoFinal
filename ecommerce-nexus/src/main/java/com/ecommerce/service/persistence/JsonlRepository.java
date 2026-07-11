package com.ecommerce.service.persistence;

import com.google.gson.Gson;
import com.ecommerce.model.entity.Entity;

import java.io.*;
import java.util.*;

public class JsonlRepository<T extends Entity> implements Repository<T, String> {
    private final Map<String, T> memoryCache = new HashMap<>();
    private final String filePath;
    private final Class<T> type;
    private final Gson gson;

    public JsonlRepository(String filePath, Class<T> type, Gson gson) {
        this.filePath = filePath;
        this.type = type;
        this.gson = gson;
        this.rehydrate(); // Dispara a reidratação do estado ao inicializar
    }

    // 1. REIDRATAÇÃO: Reconstrói o estado projetando o histórico cronológico do disco
    private void rehydrate() {
        File file = new File(filePath);
        if (!file.exists()) {
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                T entity = gson.fromJson(line, type);
                // Mecanismo de State Sourcing: os estados mais recentes (linhas abaixo)
                // sobrepõem as entradas mais antigas para o mesmo ID no cache em memória.
                memoryCache.put(entity.getId(), entity);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao reidratar o repositório: " + filePath, e);
        }
    }

    @Override
    public T findById(String id) {
        return memoryCache.get(id); // Resolução O(1) direta em memória
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(memoryCache.values());
    }

    // 2. PERSISTÊNCIA APPEND-ONLY
    @Override
    public void save(T entity) {
        String json = gson.toJson(entity);
        try (FileWriter fw = new FileWriter(filePath, true); // 'true' ativa o modo append-only
             PrintWriter out = new PrintWriter(new BufferedWriter(fw))) {
            
            out.println(json); // Consolida o snapshot de estado no log de disco
            memoryCache.put(entity.getId(), entity); // Projeta o estado no cache de memória
            
        } catch (IOException e) {
            throw new RuntimeException("Falha ao persistir a entidade: " + entity.getId(), e);
        }
    }
}