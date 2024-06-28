package org.example.Mapper;

import org.example.Mapper.Mapped;
//import org.example.Mapper.IrodsResponse;

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
