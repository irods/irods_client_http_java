package org.example.Mapper;

/**
 * For JSON responses that ONLY include an irods_response and no other items. Empty because it's an extension
 * from Mapped.
 * Used in:
 *  - collections.remove()
 *  - collections.set_permission()
 *  - collections.set_inheritance()
 */
public class IrodsResponse extends Mapped {
}
