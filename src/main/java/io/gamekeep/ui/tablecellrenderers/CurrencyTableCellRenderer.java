package io.gamekeep.ui.tablecellrenderers;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;

public class CurrencyTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column
    ) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setText(String.format("$%,.2f", (BigDecimal) value));
        setHorizontalAlignment(JLabel.RIGHT);

        return this;
    }
}
