package step.learning.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Role {
    private UUID id;
    private String name;
    private byte canCreate;
    private byte canRead;
    private byte canUpdate;
    private byte canDelete;

    public Role() {

    }
    public Role(ResultSet resultSet) throws SQLException {
        setId(UUID.fromString(resultSet.getString("id")));
        setName(resultSet.getString("name"));
        setCanCreate(resultSet.getByte("can_create"));
        setCanUpdate(resultSet.getByte("can_update"));
        setCanDelete(resultSet.getByte("can_delete"));
        setCanRead(resultSet.getByte("can_read"));


    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getCanCreate() {
        return canCreate;
    }

    public void setCanCreate(byte canCreate) {
        this.canCreate = canCreate;
    }

    public byte getCanRead() {
        return canRead;
    }

    public void setCanRead(byte canRead) {
        this.canRead = canRead;
    }

    public byte getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(byte canUpdate) {
        this.canUpdate = canUpdate;
    }

    public byte getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(byte canDelete) {
        this.canDelete = canDelete;
    }
}
