package io.cityos.cityosair.data.model.schema;

/**
 * Created by Andrej on 07/02/2017.
 */

public class Schema {

    private int id;
    private Layout layout;

    public Schema() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }
}