package org.libermundi.theorcs.services;

import org.libermundi.theorcs.domain.jpa.chronicle.Chronicle;

public interface ChronicleService extends BaseService<Chronicle> {
    Chronicle findByName();
}
