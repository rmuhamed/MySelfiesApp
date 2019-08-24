package com.rmuhamed.sample.myselfiesapp.repository;

import com.rmuhamed.sample.myselfiesapp.application.Configuration;

import org.jetbrains.annotations.NotNull;

public class RepositoryFactory {

    public enum Type {ALBUM, CAMERA, GALLERY, LOGIN}

    public static @NotNull
    IRepository get(Type type, Configuration configuration) {
        IRepository repo = null;
        switch (type) {
            case LOGIN:
                repo = new LoginRepository(configuration.getApiDataSource(), configuration.getDBDataSource());
                break;
            case GALLERY:
                break;
            case ALBUM:
                break;
            case CAMERA:
                break;
            default:
                throw new UnsupportedOperationException("NO REPOSITORY ASSOCIATED WITH: " + type.name());
        }
        return repo;
    }
}
