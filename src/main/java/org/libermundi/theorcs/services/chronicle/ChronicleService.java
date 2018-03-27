package org.libermundi.theorcs.services.chronicle;

import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;
import org.libermundi.theorcs.services.base.BaseService;

public interface ChronicleService extends BaseService<Chronicle> {
    Chronicle findByTitle(String title);
}
