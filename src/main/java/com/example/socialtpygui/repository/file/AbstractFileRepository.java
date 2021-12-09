package com.example.socialtpygui.repository.file;


import com.example.socialtpygui.domain.Entity;
import com.example.socialtpygui.repository.memory.InMemoryRepository;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractFileRepository<ID,E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    private String fileName;

    public AbstractFileRepository(String fileName) {
        super();
        this.fileName = fileName;
        loadData();
    }
    private void loadData(){
        try(BufferedReader bufferedReader= new BufferedReader(new FileReader(fileName))){
            String line;
            while ((line=bufferedReader.readLine())!=null){
                List<String> atributes= Arrays.asList(line.split(","));
                E element= extractEntity(atributes);
                super.save(element);
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Fisierul nu a fost gasit");
            e.printStackTrace();
        }
        catch (IOException e){
            System.out.println("Eroare la citire!");
            e.printStackTrace();
        }
    }

    protected abstract String createEntityAsString(E entity);

    protected void writeToFile(E entity){

        try(BufferedWriter writer= new BufferedWriter(new FileWriter(fileName,true))){
            writer.write(createEntityAsString(entity));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void removeFromFile(E entity){
        try(BufferedWriter writer= new BufferedWriter(new FileWriter(fileName))){
            writer.write("");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        entities.values().forEach(ent->writeToFile(ent));
    }

    protected abstract E extractEntity(List<String> atributes);

    @Override
    public E save(E entity) {
        E results= super.save(entity);
        if(results==null)
            writeToFile(entity);
        return results;
    }

    @Override
    public E remove(ID id) {
        E results= super.remove(id);
        removeFromFile(results);
        return results;
    }
}
