package org.libermundi.theorcs.domain.jpa;

import org.hibernate.dialect.MySQL57Dialect;
import java.sql.Types;

public class MySQL57SafeDialect extends MySQL57Dialect {

    public MySQL57SafeDialect() {
        super();

        registerColumnType(Types.BOOLEAN,"tinyint(1)");
    }
}
