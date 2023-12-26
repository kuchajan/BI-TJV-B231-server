package cz.cvut.fit.tjv.kuchaj19.carleaseapi.domain;

public interface EntityWithId<ID> {
    ID getId();

    void update(EntityWithId<ID> updateWith);
}