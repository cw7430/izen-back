package com.izen.common.config.logging;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.github.vertical_blank.sqlformatter.languages.Dialect;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

public class P6SpySqlFormat implements MessageFormattingStrategy {

    @Override
    public String formatMessage(
            int connectionId,
            String now,
            long elapsed,
            String category,
            String prepared,
            String sql,
            String url
    ) {
        if (sql == null || sql.isBlank()) {
            return "";
        }

        String formattedSql = SqlFormatter.of(Dialect.PostgreSql).format(sql);

        return """
                
                --------------------------------------------------
                Execution Time : %d ms
                --------------------------------------------------
                %s
                --------------------------------------------------""".formatted(elapsed, formattedSql);
    }

}