package org.irods;

import org.irods.serialize.ModifyPermissionsOperations;
import org.irods.util.Permission;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThrows;

public class ModifyPermissionsOperationsTest {
    @Test
    public void testUnsupportedPermissionValueThrowsError() {
        // Try to set ModifyPermissionsOperations permission to `create_object`, which is not supported
        List<ModifyPermissionsOperations> operation = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> {
            operation.add(new ModifyPermissionsOperations("rods", Permission.CREATE_OBJECT));
        });
    }
}