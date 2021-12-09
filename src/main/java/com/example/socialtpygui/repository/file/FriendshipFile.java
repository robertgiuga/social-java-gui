package com.example.socialtpygui.repository.file;

import com.example.socialtpygui.domain.Friendship;
import com.example.socialtpygui.domain.TupleOne;

import java.time.LocalDate;
import java.util.List;

public class FriendshipFile extends AbstractFileRepository<TupleOne<String>, Friendship> {

    public FriendshipFile(String fileName) {
        super(fileName);
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId().getLeft()+","+entity.getId().getRight()+","+entity.getDate();
    }

    @Override
    protected Friendship extractEntity(List<String> atributes) {
        Friendship frnd= new Friendship(atributes.get(0),atributes.get(1), LocalDate.parse(atributes.get(2)));
        return frnd;
    }

}
