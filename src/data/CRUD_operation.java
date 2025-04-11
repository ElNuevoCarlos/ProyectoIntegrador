package data;

import java.util.ArrayList;

public interface CRUD_operation<S,T> {
	// C: Create
    void save(S entity);
    ArrayList<S> fetch();
    void update(S entity);
    void delete(T id);
    boolean authenticate(T id);

}